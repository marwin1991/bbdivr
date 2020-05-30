package pl.marwin1991.bbdivr.engine.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marwin1991.bbdivr.clair.service.ClairLayerAnalyseService;
import pl.marwin1991.bbdivr.engine.client.DockerClientProvider;
import pl.marwin1991.bbdivr.engine.util.FilesUtils;
import pl.marwin1991.bbdivr.model.Manifest;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.provider.TempDirLocationProvider;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScannerService {
    private final DockerClientProvider dockerClientProvider;
    private final ObjectMapper objectMapper;
    private final ClairLayerAnalyseService layerAnalyseService;
    private final TempDirLocationProvider tempDirLocationProvider;

    @SneakyThrows
    public ScanResult scan(String imageName) {
        Path tmpPath = FilesUtils.createTmpPath();
        try {
            tempDirLocationProvider.addPath(tmpPath);
            imageName = "libamtrack/libamtrackforjs:latest";
            DockerClient client = dockerClientProvider.getClient();

            InputStream inputStream = client.saveImageCmd(imageName).exec();
            FilesUtils.decompress(inputStream, tmpPath.toFile());
            inputStream.close();

            List<String> layerIds = getLayersId(tmpPath);

            return layerAnalyseService.analyse(layerIds);
        } finally {
            tempDirLocationProvider.removePath(tmpPath);
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    private List<String> getLayersId(Path path) throws IOException {
        Path manifestPath = Path.of(path.toAbsolutePath() + "/manifest.json");
        String content = Files.readString(manifestPath, StandardCharsets.UTF_8);

        Manifest manifest = objectMapper.readValue(content, new TypeReference<List<Manifest>>() {
        }).get(0);
        return manifest
                .getLayers()
                .stream()
                .map(s -> s.replace("/layer.tar", ""))
                .collect(Collectors.toList());
    }
}

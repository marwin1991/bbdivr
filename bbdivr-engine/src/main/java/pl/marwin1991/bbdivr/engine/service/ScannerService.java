package pl.marwin1991.bbdivr.engine.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marwin1991.bbdivr.anchore.service.AnchoreLayerAnalyseService;
import pl.marwin1991.bbdivr.clair.provider.TempDirLocationProvider;
import pl.marwin1991.bbdivr.clair.service.ClairLayerAnalyseService;
import pl.marwin1991.bbdivr.client.DockerClientProvider;
import pl.marwin1991.bbdivr.engine.chaincode.converter.RequestToChainCodeConverter;
import pl.marwin1991.bbdivr.engine.chaincode.layer.LayerService;
import pl.marwin1991.bbdivr.engine.util.FilesUtils;
import pl.marwin1991.bbdivr.model.Manifest;
import pl.marwin1991.bbdivr.model.ScanResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScannerService {
    private final DockerClientProvider dockerClientProvider;
    private final ObjectMapper objectMapper;

    private final ClairLayerAnalyseService clairLayerAnalyseService;
    private final AnchoreLayerAnalyseService anchoreLayerAnalyseService;


    private final TempDirLocationProvider tempDirLocationProvider;
    private final LayerService layerService;
    private final RequestToChainCodeConverter converter;

    @SneakyThrows
    public ScanResult scan(String imageName) {
        log.info("Start analysing image " + imageName);
        Path tmpPath = FilesUtils.createTmpPath();
        try {
            tempDirLocationProvider.addPath(tmpPath);
            DockerClient client = dockerClientProvider.getClient();

            checkIfExistLocallyAndPull(imageName);

            InputStream inputStream = client.saveImageCmd(imageName).exec();
            FilesUtils.decompress(inputStream, tmpPath.toFile());
            inputStream.close();

            List<String> layerIds = getLayersId(tmpPath);

            ScanResult scanResult1 = clairLayerAnalyseService.analyse(imageName, layerIds);
            ScanResult scanResult2 = anchoreLayerAnalyseService.analyse(imageName, layerIds);

//            for (Layer layer : scanResult.getLayers()) {
//                try {
//                    layerService.addLayer(converter.convert(layer));
//                } catch (IOException | InterruptedException | TimeoutException | ContractException e) {
//                    log.error("Failed to add to blockchain", e);
//                    throw e;
//                }
//            }

            return scanResult1;
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

    private void checkIfExistLocallyAndPull(String imageName) {
        try {
            dockerClientProvider.getClient().inspectImageCmd(imageName).exec();
        } catch (NotFoundException e) {
            log.info(imageName + " not found locally, image will ne pulled from remote");
            try {
                dockerClientProvider.getClient().pullImageCmd(imageName).start().awaitCompletion();
            } catch (InterruptedException interruptedException) {
                log.error("Error during pulling image " + imageName, interruptedException);
            }
        }
    }
}

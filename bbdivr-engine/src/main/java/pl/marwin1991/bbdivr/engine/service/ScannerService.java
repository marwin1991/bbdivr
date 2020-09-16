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
import pl.marwin1991.bbdivr.model.Severity;
import pl.marwin1991.bbdivr.model.SumScanResult;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
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

            //ScanResult scanResult1 = clairLayerAnalyseService.analyse(imageName, layerIds);
            ScanResult scanResult2 = anchoreLayerAnalyseService.analyse(imageName, layerIds);

//            for (Layer layer : scanResult.getLayers()) {
//                try {
//                    layerService.addLayer(converter.convert(layer));
//                } catch (IOException | InterruptedException | TimeoutException | ContractException e) {
//                    log.error("Failed to add to blockchain", e);
//                    throw e;
//                }
//            }

            return scanResult2;
        } finally {
            tempDirLocationProvider.removePath(tmpPath);
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @SneakyThrows
    public List<SumScanResult> scanSum(String imageName) {
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

            List<SumScanResult> toReturn = new LinkedList<>();

            SumScanResult clairScanResult = clairLayerAnalyseService.analyseAndSum(imageName, layerIds);
            SumScanResult anchoreScanResult = anchoreLayerAnalyseService.analyseAndSum(imageName, layerIds);

            toReturn.add(clairScanResult);
            toReturn.add(anchoreScanResult);

            SumScanResult sum = sum(anchoreScanResult, clairScanResult);
            toReturn.add(sum);


            printSumToCsv(toReturn);

            return toReturn;
        } finally {
            tempDirLocationProvider.removePath(tmpPath);
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @SneakyThrows
    private void printSumToCsv(List<SumScanResult> listOfVul) {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.csv", true)));

        Arrays.stream(Severity.values()).forEach(sev -> {
            if (sev != Severity.DEFCON) {
                String tmp_result = sev.name() + "," + getSize(listOfVul.get(0).getVulnerabilitiesIds().get(sev)) +
                        "," + getSize(listOfVul.get(1).getVulnerabilitiesIds().get(sev)) + "," +
                        getSize(listOfVul.get(2).getVulnerabilitiesIds().get(sev)) + "\n";
                out.print(tmp_result);
            }
        });
        out.flush();
        out.close();
    }

    private String getSize(Set<String> s) {
        if (s == null)
            return String.valueOf(0);
        return String.valueOf(s.size());
    }

    private SumScanResult sum(SumScanResult anchoreScanResult, SumScanResult clairScanResult) {
        Map<Severity, Set<String>> vulMap = new HashMap<>();
        Arrays.stream(Severity.values()).forEach(sev -> {
            Set<String> tmp1 = clairScanResult.getVulnerabilitiesIds().get(sev);
            Set<String> tmp2 = anchoreScanResult.getVulnerabilitiesIds().get(sev);

            if (tmp1 != null && tmp2 != null) {
                tmp1.addAll(tmp2);
                vulMap.put(sev, tmp1);
            } else {
                if (tmp1 != null) {
                    vulMap.put(sev, tmp1);
                }
                if (tmp2 != null) {
                    vulMap.put(sev, tmp2);
                }
            }

        });

        return SumScanResult.builder()
                .id(anchoreScanResult.getId())
                .scanToolName("SUM")
                .vulnerabilitiesIds(vulMap)
                .build();
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

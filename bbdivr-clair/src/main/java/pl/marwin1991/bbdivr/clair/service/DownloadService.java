package pl.marwin1991.bbdivr.clair.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import pl.marwin1991.bbdivr.provider.TempDirLocationProvider;

import java.net.MalformedURLException;
import java.nio.file.Path;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DownloadService {

    private final TempDirLocationProvider tempDirLocationProvider;

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.tempDirLocationProvider.getPaths().get(0).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                log.error("File not found " + fileName);
                return null;
            }
        } catch (MalformedURLException ex) {
            log.error("File not found " + fileName, ex);
            return null;
        }
    }
}

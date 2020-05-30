package pl.marwin1991.bbdivr.engine.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
public class FilesUtils {

    public static Path createTmpPath() throws IOException {
        return Files.createTempDirectory("bbdivr-clair-layer-tmp");
    }

    public static void decompress(InputStream in, File out) throws IOException {
        try (TarArchiveInputStream fin = new TarArchiveInputStream(in)) {
            TarArchiveEntry entry;
            while ((entry = fin.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File curfile = new File(out, entry.getName());
                File parent = curfile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(curfile);
                IOUtils.copy(fin, fos);
                fos.close();
            }
        }
    }

}

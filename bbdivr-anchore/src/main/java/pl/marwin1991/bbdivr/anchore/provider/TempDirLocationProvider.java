package pl.marwin1991.bbdivr.anchore.provider;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

@Component
@Scope("singleton")
public class TempDirLocationProvider {

    private final List<Path> paths = new LinkedList<>();

    public synchronized void addPath(Path path) {
        paths.add(path);
    }

    public synchronized void removePath(Path path) {
        paths.remove(path);
    }

    public List<Path> getPaths() {
        return paths;
    }
}

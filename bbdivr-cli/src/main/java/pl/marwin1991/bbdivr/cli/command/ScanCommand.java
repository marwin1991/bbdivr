package pl.marwin1991.bbdivr.cli.command;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import pl.marwin1991.bbdivr.cli.client.EngineClient;

@ShellComponent
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScanCommand {

    private final EngineClient engineClient;

    @ShellMethod("Scan docker images")
    public String scan(String imageName) {
        if (!engineClient.isEnrolled()) {
            return "FAIL!!! First use enroll command";
        }

        return imageName;
    }
}

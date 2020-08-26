package pl.marwin1991.bbdivr.cli.command;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import pl.marwin1991.bbdivr.cli.client.EngineClient;

@ShellComponent
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class EnrollCommand {

    private final EngineClient engineClient;

    @ShellMethod("Enroll to blockchain")
    public String enroll() {
        return engineClient.enroll();
    }
}

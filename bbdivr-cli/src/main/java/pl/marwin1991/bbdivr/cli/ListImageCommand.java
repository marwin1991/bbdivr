package pl.marwin1991.bbdivr.cli;

import com.github.dockerjava.api.model.Image;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModel;
import pl.marwin1991.bbdivr.client.DockerClientProvider;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@ShellComponent
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ListImageCommand {

    private static final int NUMBER_OF_COLUMNS = 3;
    private final DockerClientProvider dockerClientProvider;

    @ShellMethod("List docker images in local repository")
    public String images() {
        List<Image> imageList = dockerClientProvider.getClient().listImagesCmd().exec();

        Object[][] sampleData = new String[imageList.size()][NUMBER_OF_COLUMNS];

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        int counter = 0;
        for (Image i : imageList) {
            sampleData[counter] = new String[]{
                    i.getId().replace("sha256:", "").substring(0, 20),
                    Arrays.toString(Arrays.stream(i.getRepoTags())
                            .map(s -> {
                                if (s.length() > 30) {
                                    return s.substring(0, 28) + "...";
                                } else {
                                    return s;
                                }
                            })
                            .toArray(String[]::new))
                            .replace("[", "")
                            .replace("]", "")
                            .replace(",", ""),

                    LocalDateTime.ofEpochSecond(i.getCreated(), 0, ZoneOffset.UTC).format(formatter)
            };
            counter++;
        }

        TableModel model = new ArrayTableModel(sampleData);
        TableBuilder tableBuilder = new TableBuilder(model);
        tableBuilder.addFullBorder(BorderStyle.oldschool);
        return tableBuilder.build().render(80);
    }
}
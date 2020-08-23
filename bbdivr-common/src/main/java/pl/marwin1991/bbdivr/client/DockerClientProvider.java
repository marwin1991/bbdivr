package pl.marwin1991.bbdivr.client;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.DockerCmdExecFactory;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.okhttp.OkHttpDockerCmdExecFactory;
import org.springframework.stereotype.Controller;

@Controller
public class DockerClientProvider {

    public DockerClient getClient() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("npipe:////./pipe/docker_engine") //TODO linux
                //.withDockerTlsVerify(false) //TODO tls
                //.withDockerCertPath("C:\\Users\\Piotr\\.docker\\certs")
                .withDockerConfig("C:\\Users\\Piotr\\.docker")
                .build();

        DockerCmdExecFactory dockerCmdExecFactory = new OkHttpDockerCmdExecFactory();

        return DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();
    }
}

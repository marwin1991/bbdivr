package pl.marwin1991.bbdivr.engine.chaincode.controller;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.chaincode.layer.ChainCodeLayer;
import pl.marwin1991.bbdivr.engine.chaincode.layer.LayerService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/time-test")
public class TimeTestController {

    private final LayerService layerService;

    @Autowired
    public TimeTestController(LayerService layerService) {
        this.layerService = layerService;
    }

    @GetMapping("/1")
    public String test1() throws InterruptedException, ContractException, TimeoutException, IOException {
        int reapeatTimes = 2;
        int testNumber = 400;
        long counter = 500;
        String result = "";
        for (int a = 0; a < testNumber; a++) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.csv", true)));
            log.info("loop number:" + a);
            List<Double> times = new LinkedList<>();
            long size = 0;

            List<String> ids = getVulIds(a);
            for (int i = 0; i < reapeatTimes; i++) {
                ChainCodeLayer layer = new ChainCodeLayer();
                layer.setId("test" + counter);
                layer.setParentId("");
                layer.setCreateDate(OffsetDateTime.now());
                layer.setVulnerabilityIds(ids);

                long start = System.nanoTime();
                size = layerService.testAddLayer(layer);
                long end = System.nanoTime();
                Double elapsedTime = (end - start) / 1_000_000_000.0;
                times.add(elapsedTime);
                counter++;
            }

            Double avg = getAvg(times);

            String tmp_result = (String.format("%.6f", avg) + ";" + size + "\n");
            out.print(tmp_result);
            out.flush();
            out.close();
            result += tmp_result;
        }
        return result;
    }

    @GetMapping("/2")
    public String test2() throws InterruptedException, ContractException, TimeoutException, IOException {
        int reapeatTimes = 2;
        int testNumber = 400;
        long counter = 500;
        String result = "";
        for (int a = 0; a < testNumber; a++) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("results.csv", true)));
            log.info("loop number:" + a);
            List<Double> times = new LinkedList<>();
            long size = 0;

            for (int i = 0; i < reapeatTimes; i++) {
                long start = System.nanoTime();
                size = layerService.testQueryLayer("test" + counter);
                long end = System.nanoTime();
                Double elapsedTime = (end - start) / 1_000_000_000.0;
                times.add(elapsedTime);
                counter++;
            }

            Double avg = getAvg(times);

            String tmp_result = (String.format("%.6f", avg) + ";" + size + "\n");
            out.print(tmp_result);
            out.flush();
            out.close();
            result += tmp_result;
        }
        return result;
    }

    private Double getAvg(List<Double> times) {
        Double sum = 0.0;
        for (Double d : times) {
            sum += d;
        }

        return sum / (double) times.size();
    }

    private List<String> getVulIds(int n) {
        List<String> toReturn = new LinkedList<>();
        for (int i = 0; i < 80 * n; i++)
            toReturn.add("12345678990qwertyuiopasdfghjklzxcvbnmzxcvbnmasdfghjklopoiuytrewq");
        return toReturn;
    }
}

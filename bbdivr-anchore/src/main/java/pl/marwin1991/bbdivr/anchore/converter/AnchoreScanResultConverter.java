package pl.marwin1991.bbdivr.anchore.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.model.ScanResult;

import java.util.List;

@Slf4j
@Component
public class AnchoreScanResultConverter {

    private static final String SCAN_TOOL_NAME = "Anchore";

    public ScanResult convert(List<Object> responses) {

        return ScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .layers(null)
                .build();
    }

}

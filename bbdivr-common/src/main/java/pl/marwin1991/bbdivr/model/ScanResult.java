package pl.marwin1991.bbdivr.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScanResult {
    private List<LayerResponse> result;
}

package pl.marwin1991.bbdivr.anchore.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalysisArchiveSource {

  private String digest;

}


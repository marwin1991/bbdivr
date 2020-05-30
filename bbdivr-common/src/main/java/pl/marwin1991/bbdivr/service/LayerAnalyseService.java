package pl.marwin1991.bbdivr.service;


import pl.marwin1991.bbdivr.model.ScanResult;

import java.util.List;

public interface LayerAnalyseService {

    ScanResult analyse(List<String> layerIds);

}

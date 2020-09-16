package pl.marwin1991.bbdivr.service;


import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.model.SumScanResult;

import java.util.List;

public interface LayerAnalyseService {

    ScanResult analyse(String imageName, List<String> layerIds);

    SumScanResult analyseAndSum(String imageName, List<String> layerIds);

}

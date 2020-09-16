package pl.marwin1991.bbdivr.engine.chaincode.converter;

import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.chaincode.layer.ChainCodeLayer;
import pl.marwin1991.bbdivr.chaincode.vulnerability.ChainCodeVulnerability;
import pl.marwin1991.bbdivr.engine.chaincode.model.AddLayerRequest;
import pl.marwin1991.bbdivr.model.Layer;
import pl.marwin1991.bbdivr.model.Vulnerability;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestToChainCodeConverter {

    public ChainCodeLayer convert(Layer layer) {
        ChainCodeLayer c = new ChainCodeLayer();
        c.setId(layer.getId());
        c.setParentId(layer.getParentId() == null ? "" : layer.getParentId());
        c.setVulnerabilityIds(layer.getVulnerabilities().stream().map(Vulnerability::getId).collect(Collectors.toList()));
        c.setFixedVulnerabilityIdsFromParents(layer.getFixedVulnerabilitiesFromParents().stream().map(Vulnerability::getId).collect(Collectors.toList()));
        return c;
    }

    public ChainCodeLayer convert(AddLayerRequest layer) {
        ChainCodeLayer c = new ChainCodeLayer();
        c.setId(layer.getId());
        c.setParentId(layer.getParentId() == null ? "" : layer.getParentId());
        c.setVulnerabilityIds(layer.getVulnerabilities().stream().map(Vulnerability::getId).collect(Collectors.toList()));
        c.setFixedVulnerabilityIdsFromParents(layer.getFixedVulnerabilitiesFromParents().stream().map(Vulnerability::getId).collect(Collectors.toList()));
        return c;
    }

    private List<ChainCodeVulnerability> convert(List<Vulnerability> vulnerabilities) {
//        if(vulnerabilities.size() > 0)
//            return Collections.singletonList(convert(vulnerabilities.get(0)));
//
//        return Collections.emptyList();

//        return vulnerabilities.stream().map(this::convert).collect(Collectors.toList());
        return vulnerabilities.stream().map(this::convert).collect(Collectors.toList());
    }

    private ChainCodeVulnerability convert(Vulnerability v) {
        ChainCodeVulnerability vul = new ChainCodeVulnerability();
        vul.setId(v.getId());

        return vul;
    }
}

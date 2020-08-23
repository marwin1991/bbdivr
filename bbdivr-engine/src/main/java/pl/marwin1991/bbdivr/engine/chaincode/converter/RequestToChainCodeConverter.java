package pl.marwin1991.bbdivr.engine.chaincode.converter;

import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.chaincode.ChainCodeLayer;
import pl.marwin1991.bbdivr.chaincode.ChainCodeVulnerability;
import pl.marwin1991.bbdivr.engine.chaincode.model.AddLayerRequest;
import pl.marwin1991.bbdivr.engine.chaincode.model.VulnerabilityDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestToChainCodeConverter {

    public ChainCodeLayer convert(AddLayerRequest request) {
        return ChainCodeLayer.builder()
                .id(request.getId())
                .parentId(request.getParentId())
                .vulnerabilities(convert(request.getVulnerabilities()))
                .build();
    }

    private List<ChainCodeVulnerability> convert(List<VulnerabilityDTO> vulnerabilities) {
        return vulnerabilities.stream().map(this::convert).collect(Collectors.toList());
    }

    private ChainCodeVulnerability convert(VulnerabilityDTO v) {
        return ChainCodeVulnerability.builder()
                .vulnerabilityName(v.getVulnerabilityName())
                .featureName(v.getFeatureName())
                .featureVersion(v.getFeatureVersion())
                .featureVersionFormat(v.getFeatureVersionFormat())
                .namespaceName(v.getNamespaceName())
                .description(v.getDescription())
                .link(v.getLink())
                .severity(v.getSeverity())
                .fixedBy(v.getFixedBy())
                .build();
    }
}

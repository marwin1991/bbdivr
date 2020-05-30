package pl.marwin1991.bbdivr.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.engine.service.ScannerService;
import pl.marwin1991.bbdivr.model.ScanResult;

@RestController
public class ScannerController {

    private final ScannerService scannerService;

    @Autowired
    public ScannerController(ScannerService scannerService) {
        this.scannerService = scannerService;
    }

    @GetMapping("/{imageName}")
    private ResponseEntity<ScanResult> scan(@PathVariable String imageName) {
        return ResponseEntity.ok(scannerService.scan(imageName));
    }
}

package pl.marwin1991.bbdivr.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.engine.service.ScannerService;
import pl.marwin1991.bbdivr.model.ScanResult;

@RestController
@RequestMapping("/scan")
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

    @GetMapping("/{imageRepo}/{imageName}")
    private ResponseEntity<ScanResult> scan(@PathVariable String imageRepo, @PathVariable String imageName) {
        String finalName = imageRepo + "/" + imageName;
        return ResponseEntity.ok(scannerService.scan(finalName));
    }

    @GetMapping("/{imageRepo1}/{imageRepo2}/{imageName}")
    private ResponseEntity<ScanResult> scan(@PathVariable String imageRepo1, @PathVariable String imageRepo2, @PathVariable String imageName) {
        String finalName = imageRepo1 + "/" + imageRepo2 + "/" + imageName;
        return ResponseEntity.ok(scannerService.scan(finalName));
    }
}

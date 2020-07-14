package pl.marwin1991.bbdivr.engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.engine.chaincode.enroll.EnrollService;

@RestController
@RequestMapping("/enroll")
public class EnrollController {

    private static final String SUCCESSFUL_ENROLLMENT = "success";

    private final EnrollService enrollService;

    @Autowired
    public EnrollController(EnrollService enrollService) {
        this.enrollService = enrollService;
    }

    @GetMapping
    public ResponseEntity<String> enroll() throws Exception {
        enrollService.enrollAdmin();
        enrollService.registerUser();
        return ResponseEntity.ok(SUCCESSFUL_ENROLLMENT);
    }
}

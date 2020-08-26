package pl.marwin1991.bbdivr.engine.chaincode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.engine.chaincode.enroll.EnrollService;
import pl.marwin1991.bbdivr.engine.chaincode.model.EnrollmentStatus;

@RestController
@RequestMapping("/enroll")
public class EnrollController {

    private final EnrollService enrollService;

    @Autowired
    public EnrollController(EnrollService enrollService) {
        this.enrollService = enrollService;
    }

    @GetMapping
    public ResponseEntity<EnrollmentStatus> isEnroll() throws Exception {
        if (enrollService.isEnrolled()) {
            return ResponseEntity.ok(EnrollmentStatus.ENROLLED);
        } else {
            return ResponseEntity.ok(EnrollmentStatus.NOT_ENROLLED);
        }
    }

    @PostMapping
    public ResponseEntity<EnrollmentStatus> enroll() throws Exception {
        enrollService.enrollAdmin();
        enrollService.registerUser();
        return ResponseEntity.ok(EnrollmentStatus.ENROLLED);
    }
}

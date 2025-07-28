package fr.ippon.iroco2.access.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.ippon.iroco2.access.jwt.ScannerJwtGenerator;
import fr.ippon.iroco2.access.jwt.ScannerJwtVerifier;
import fr.ippon.iroco2.access.presentation.request.GenerateTokenRequest;
import fr.ippon.iroco2.common.presentation.security.IsMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class ScannerJwtController {

    private final ScannerJwtGenerator scannerJwtGenerator;
    private final ScannerJwtVerifier scannerJwtVerifier;

    @IsMember
    @PostMapping("/generate")
    public ResponseEntity<String> generateApiKey(@RequestBody GenerateTokenRequest generateTokenRequest)
        throws JsonProcessingException {
        var apiKey = scannerJwtGenerator.generate(
            generateTokenRequest.awsAccountId(),
            generateTokenRequest.expireInSeconds()
        );
        return ResponseEntity.ok(apiKey);
    }

    @IsMember
    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifyApiKey(String apiKey) {
        var result = scannerJwtVerifier.verify(apiKey);
        return ResponseEntity.ok(result);
    }
}

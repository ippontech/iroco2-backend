package fr.ippon.iroco2.scanner.presentation;

import fr.ippon.iroco2.common.presentation.security.IsMember;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import fr.ippon.iroco2.domain.scanner.exception.ScanNotFoundException;
import fr.ippon.iroco2.scanner.presentation.mapper.ScanMapper;
import fr.ippon.iroco2.scanner.presentation.response.ScanDetailResponse;
import fr.ippon.iroco2.scanner.presentation.response.ScanListElementResponse;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/scanner")
@RequiredArgsConstructor
public class ScannerController {

    private final ScannerSvc scannerSvc;
    private final ScanMapper scanMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UUID> save() {
        UUID savedScan = scannerSvc.create();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedScan);
    }

    @IsMember
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable UUID id) throws ScanNotFoundException {
        scannerSvc.delete(id);
    }

    @IsMember
    @GetMapping
    public ResponseEntity<List<ScanListElementResponse>> findAll() {
        var scans = scannerSvc.findAll();
        var responses = scanMapper.toResponse(scans);
        return ResponseEntity.ok(responses);
    }

    @IsMember
    @GetMapping("/{id}")
    public ResponseEntity<ScanDetailResponse> findById(@PathVariable UUID id) throws ScanNotFoundException {
        var scan = scannerSvc.findById(id);
        var response = scanMapper.toDetailResponse(scan);
        return ResponseEntity.ok(response);
    }
}

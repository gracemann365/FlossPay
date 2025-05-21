package com.openpay.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class StatusController {

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatus(@PathVariable Long id) {
        //  Replace with real fetch from DB
        return ResponseEntity.ok("Status for ID " + id + ": queued (stub)");
    }
}

package com.jeffjackson;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("API is up and running v1.2");
    }
}

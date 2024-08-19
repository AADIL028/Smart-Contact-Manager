package com.scm.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RestController
public class HealthController {
    @RequestMapping(path="/health", method=RequestMethod.GET)
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Everything looking OK......",HttpStatus.OK);
    }
    
}

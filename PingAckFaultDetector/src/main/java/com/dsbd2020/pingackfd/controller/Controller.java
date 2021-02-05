package com.dsbd2020.pingackfd.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@RestController
public class Controller {
    
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public void Ping() {
    }

}

package com.dsbd2020.testheartbeat.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
public class Controller {
    
    @PostMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public void Ping(@RequestBody String text) {
        System.out.println(text);
    }

}

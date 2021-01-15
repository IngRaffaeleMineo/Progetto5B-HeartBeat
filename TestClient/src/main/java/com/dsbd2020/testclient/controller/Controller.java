package com.dsbd2020.testclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;

@RestController
public class Controller {
    
    @GetMapping("/ping")
    public String Ping() {
        System.out.println("Request /ping");
        return new Gson().toJson(new Ack());
    }

}

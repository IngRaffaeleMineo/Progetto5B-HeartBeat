package com.dsbd2020.testclient.controller;

import java.lang.Math;

public class Ack {
    private String serviceStatus;
    private String dbStatus;
    
    public Ack() {
        this.serviceStatus = ((Math.random()<0.7) ? "up" : "down");
        this.dbStatus = ((Math.random()<0.7) ? "up" : "down");
    }
}

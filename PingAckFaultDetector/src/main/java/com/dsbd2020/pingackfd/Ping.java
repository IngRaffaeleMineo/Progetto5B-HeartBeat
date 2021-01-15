package com.dsbd2020.pingackfd;

public class Ping {
    private String service;
    private String serviceStatus;
    private String dbStatus;
    
    public Ping() {
        this.service = "PingAckFaultDetector";
        this.serviceStatus = "up";
        this.dbStatus = "up";
    }
}

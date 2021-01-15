package com.dsbd2020.pingackfd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import dsbd2020.pingackfd.kafka.*;
import com.google.gson.Gson;
import java.util.Map;

@SpringBootApplication
public class PingAckFaultDetectorApplication {

    @Value("${kafkaTopic}")
    private static String topicName;

    @Autowired
    private static KafkaTemplate<String, String> kafkaTemplate;

    public static void sendMessage(String msg) {
        kafkaTemplate.send(topicName, msg);
    }

    public static void main(String[] args) {
        SpringApplication.run(PingAckFaultDetectorApplication.class, args);

        //@Value("${env.HBFDHOSTPORT}") // non va perchè sono su @SpringBootApplication e non su @Component
        String HBFDHostPort = System.getenv("HBFDHOSTPORT");

        //@Value("${env.HEARTBEAT_RESEND_TIME}")
        long TimeHeartBeat = Integer.parseInt(System.getenv("HEARTBEAT_RESEND_TIME"));

        //@Value("${env.PING_POOLING_TIME}")
        long TimePingPolling = Integer.parseInt(System.getenv("PING_POOLING_TIME"));

        //@Value("${env.HOST_LIST}".split(","))
        String hostList[] = System.getenv("HOST_LIST").split(",");

        long startTimeHeartBeat = System.currentTimeMillis();
        long startTimePingPolling = System.currentTimeMillis();
        while (true){
            //HeartBeat check
            if((System.currentTimeMillis() - startTimeHeartBeat) >= TimeHeartBeat){
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> request = new HttpEntity<String>(new Gson().toJson(new Ping()).toString(), headers);
                try{
                    ResponseEntity<String> postRes = new RestTemplate().postForEntity("http://" + HBFDHostPort + "/ping", request, String.class);
                    if (postRes.getStatusCode().is2xxSuccessful()){
                        System.out.println("HeartBeat: Send.");
                    }
                    else{
                        System.out.println("HeartBeat: Fault Detector not found.");
                    }
                }
                catch (Exception e){
                    System.out.println("HeartBeat: Fault Detector not found.");
                }
                startTimeHeartBeat = System.currentTimeMillis();
            }

            //Ping verso lista di microservizi
            if((System.currentTimeMillis() - startTimePingPolling) >= TimePingPolling){
                for (String host : hostList) {
                    System.out.println("Ping to " + host + "...");
                    try{
                        ResponseEntity<AckResponse> responseEntity = new RestTemplate().getForEntity("http://" + host + "/ping", AckResponse.class);
                        if(!(responseEntity.getStatusCode().is2xxSuccessful())){
                            System.out.println("Ping to " + host + " failed: destination not Found");
                            sendMessage(new Gson().toJson(new Message(new Gson().toJson(new MessageServerUnavailable(responseEntity.getStatusCode().toString())),host)).toString());
                        }
                        else {
                            String serviceStatus = responseEntity.getBody().serviceStatus;
                            String dbStatus = responseEntity.getBody().dbStatus;
                            if((!serviceStatus.equals("up")) || (!dbStatus.equals("up"))){
                                System.out.println("Ping to " + host + " failed: serviceStatus->" + serviceStatus + " - dbStatus->" + dbStatus + ".");
                                sendMessage(new Gson().toJson(new Message(new Gson().toJson(responseEntity.getBody()),host)).toString());
                            }
                            else {
                                System.out.println("Ping to " + host + " OK.");
                            }
                        }
                    }
                    catch (Exception e){
                        System.out.println("Ping to " + host + " failed: destination not Found or kafka not found");
                    }
                }
                startTimePingPolling = System.currentTimeMillis();
            }
        }
    }

}

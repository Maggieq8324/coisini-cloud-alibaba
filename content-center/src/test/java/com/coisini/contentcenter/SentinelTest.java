package com.coisini.contentcenter;

import org.springframework.web.client.RestTemplate;

public class SentinelTest {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for (int i = 0; i < 1000; i ++) {
            String url = "http://localhost:8010/test";
            restTemplate.getForObject(url, String.class);
            Thread.sleep(500);
        }
    }
}

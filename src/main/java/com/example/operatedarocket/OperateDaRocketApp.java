package com.example.operatedarocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.example.operatedarocket.utils.UtilFuncs;

@SpringBootApplication
public class OperateDaRocketApp {
    public static UtilFuncs util;

    public static void main(String[] args) {
        util = new UtilFuncs();
        System.setProperty("java.awt.headless", "false");
        new SpringApplicationBuilder(OperateDaRocketApp.class)
            .web(WebApplicationType.NONE)
            .run(args);
    }
}
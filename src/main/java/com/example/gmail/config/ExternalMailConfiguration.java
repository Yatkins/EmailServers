package com.example.gmail.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Data
@Configuration
@ConfigurationProperties("mail.external")
public class ExternalMailConfiguration {

    //private String ip;
    private String url;
    private String key;

}
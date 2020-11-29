package com.example.gmail.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Data
@Configuration
@ConfigurationProperties("feature-switch.send-external-mail")
public class FeatureSwitchSendExternalMailConfig {
    private boolean sendExternalMail;
}

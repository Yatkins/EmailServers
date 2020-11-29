package com.example.gmail.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@RequiredArgsConstructor
@Configuration
@ConfigurationProperties("feature-switch.receive-external-mail")
public class FeatureSwitchReceiveExternalMailConfig {
    private boolean receiveExternalMailOn;
}

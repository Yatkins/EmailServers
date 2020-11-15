package com.example.gmail.controller;

import com.example.gmail.config.ExternalMailConfiguration;
import com.example.gmail.config.FeatureSwitchConfiguration;
import com.example.gmail.model.*;
import com.example.gmail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class MailController {

    private final MailService mailService;
    private final ExternalMailConfiguration externalMailConfiguration;
    private final FeatureSwitchConfiguration featureSwitchConfiguration;

    @PostMapping("/login")
    public Object login(@RequestBody Login login) {
        try{
            if(featureSwitchConfiguration.isEmailUp()){
                return new ResponseEntity<>(mailService.loginToEmail(login), HttpStatus.OK);
            }
            return new ResponseEntity<>("Error", HttpStatus.SERVICE_UNAVAILABLE);
        }catch(HttpClientErrorException e){
            return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
        }
    }

    @PostMapping("/send")
    public String send(@RequestBody Email email, String receiver){
        return mailService.sendEmail(email.getFrom(), receiver, email.getMessage());
    }

    //UUID from, String to, String message

    @PostMapping("/inbox")
    public ArrayList<Inbox> checkInbox(@RequestBody UUID uuid) {
        return mailService.showInbox(uuid);
    }

    @PostMapping("/outbox")
    public ArrayList<Outbox> checkOutbox(@RequestBody UUID uuid) {
        return mailService.showOutbox(uuid);
    }
}
package com.example.gmail.controller;

import com.example.gmail.config.ExternalMailConfiguration;
import com.example.gmail.config.FeatureSwitchConfiguration;
import com.example.gmail.model.*;
import com.example.gmail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailController {

    private final MailService mailService;
    private final FeatureSwitchConfiguration featureSwitchConfig;

    @PostMapping("/login")
    public Object login(@RequestBody Login login) {
        try{
            if(featureSwitchConfig.isEmailUp()){
                return mailService.loginToEmail(login);
            }
            return "Error";
        }catch(HttpClientErrorException e){
            return e.getMessage() + e.getStatusCode();
        }
    }

    @PostMapping("/send")
    public String send(@RequestBody EmailReceiverString emailReceiverString){
        return mailService.sendEmail(emailReceiverString);
    }

    @PostMapping("/inbox")
    public ArrayList<Inbox> showInbox(@RequestBody GetUUID uuid) {
        return mailService.showInbox(uuid.getPrimaryKey());
    }

    @PostMapping("/outbox")
    public ArrayList<Outbox> showOutbox(@RequestBody GetUUID uuid) {
        return mailService.showOutbox(uuid.getPrimaryKey());
    }

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                      @RequestHeader("api-key") String key) {
        return mailService.receiveExternalMail(externalEmail, key);
    }



}
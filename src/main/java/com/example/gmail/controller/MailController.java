package com.example.gmail.controller;

import com.example.gmail.config.ExternalMailConfiguration;
import com.example.gmail.config.FeatureSwitchConfiguration;
import com.example.gmail.config.FeatureSwitchSendExternalMailConfig;
import com.example.gmail.model.*;
import com.example.gmail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Base64;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailController {

    private final MailService mailService;

    private final ExternalMailConfiguration externalMailConfig;
    private final FeatureSwitchConfiguration featureSwitchConfig;

    @PostMapping("/login")
    public String login(@RequestBody Login login) {
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

    //UUID from, String to, String message

//    @ResponseBody
    @PostMapping("/inbox")
    public ArrayList<Inbox> showInbox(@RequestBody GetUUID uuid) {
        return mailService.showInbox(uuid.getPrimaryKey());
    }

//    @ResponseBody
    @PostMapping("/outbox")
    public ArrayList<Outbox> showOutbox(@RequestBody GetUUID uuid) {
        return mailService.showOutbox(uuid.getPrimaryKey());
    }

    @PostMapping("/receiveMail")
    public Object receiveMail(@RequestBody ExternalEmail externalEmail) {
        return mailService.receiveExternalMail(externalEmail);
    }

    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                      @RequestHeader("api-key") String key) {
        if(!"letMeIn".equals(new String(Base64.getDecoder().decode(key)))) {
            return new ResponseEntity<>("Incorrect Api Key", HttpStatus.UNAUTHORIZED);
        }
        if("NonUser".equals(externalEmail.getTo())) {
            return new ResponseEntity<>("User does not exist", HttpStatus.BAD_REQUEST);
        }
        if(!externalEmail.getMessage().isEmpty() && !externalEmail.getFrom().isEmpty()) {
            return new ResponseEntity<>("Message Received and Saved", HttpStatus.OK);
        }
        return new ResponseEntity<>("Missing fields", HttpStatus.BAD_REQUEST);
    }


}
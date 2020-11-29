package com.example.gmail.service;

import com.example.gmail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private Users users = new Users();
    private final RestTemplate restTemplate;

    public String loginToEmail(Login login)
    {
        UUID userUUID = getUUID(login.getUsername());

        if (userUUID != null) {
            if(login.getPassword().equals(users.getUsers().get(userUUID).getPassword())) {
                return  userUUID.toString();
            } else{
                return "Error";
            }
        } else {
            return "Error";
        }
    }

    private UUID getUUID(String userName) {
        for(Map.Entry<UUID, Login> entry: users.getUsers().entrySet())  {
            if (entry.getValue().getUsername().equals(userName)){
                return entry.getKey();
            }
        }return null;
    }


    public String sendEmail(EmailReceiverString sendEmail){
        if(getUUID(sendEmail.getTo()) != null){
            Email email = Email.builder()
                    .from(sendEmail.getFrom())
                    .to(getUUID(sendEmail.getTo()))
                    .message(sendEmail.getMessage())
                    .build();
            users.getUsers().get(getUUID(sendEmail.getTo())).updateInbox(email);
            users.getUsers().get(sendEmail.getFrom()).updateOutbox(email);
            return "Sent!";
        }else{
            return "Error";
        }
    }

    public ArrayList<Inbox> showInbox(UUID uuid){
        ArrayList<Email> personsInbox = users.getUsers().get(uuid).getInbox();
        ArrayList<Inbox> display = new ArrayList<>();
        for (Email email : personsInbox) {
            display.add(Inbox.builder()
                    .from(users.getUsers().get(email.getFrom()).getUsername()) //login cant be converted to string...
                    .message(email.getMessage())
                    .build());
        }return display;
    }

    public ArrayList<Outbox> showOutbox(UUID uuid){
        Login login = users.getUsers().get(uuid);
        ArrayList<Email> emailInbox = login.getOutbox();

        ArrayList<Outbox> display = new ArrayList<>();
        for (Email email : emailInbox) {
            display.add(Outbox.builder()
                    .to(users.getUsers().get(email.getTo()).getUsername())
                    .message(email.getMessage())
                    .build());
        }return display;
    }


    public Object receiveExternalMail(ExternalEmail externalEmail)
    {
        if (getUUID(externalEmail.getTo()) != null){
            Email email = Email.builder()
                    .from(ExternalUsers
                            .externalUsers
                            .get(externalEmail.getFrom()))
                    .to(getUUID(externalEmail.getTo()))
                    .message(externalEmail.getMessage())
                    .build();

            users.getUsers().get(getUUID(externalEmail.getTo())).updateInbox(email);

            return new ResponseEntity<>("Yay", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }
}


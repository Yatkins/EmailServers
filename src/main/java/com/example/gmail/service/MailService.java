package com.example.gmail.service;

import com.example.gmail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    private Users users = new Users();

    public Object loginToEmail(Login login)
    {
        ResponseEntity<String> responseEntity;
        UUID userUUID = getUUID(login.getUsername());

        if (userUUID != null) {
            if(login.getPassword().equals(users.getUsers().get(userUUID).getPassword())) {
                responseEntity = new ResponseEntity<>(userUUID.toString(), HttpStatus.OK);
            } else{
                responseEntity = new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
            }
        } else {
            responseEntity = new ResponseEntity<>("Error", HttpStatus.UNAUTHORIZED);
        }
        return responseEntity;
    }

    private UUID getUUID(String userName) {
        for(Map.Entry<UUID, Login> entry: users.getUsers().entrySet())  {
            if (entry.getValue().getUsername().equals(userName)){
                return entry.getKey();
            }
        }return null;
    }

    public String sendEmail(UUID from, String to, String message){
        if(getUUID(to) != null){
            Email email = Email.builder()
                    .from(from)
                    .to(getUUID(to))
                    .message(message)
                    .build();
            users.getUsers().get(getUUID(to)).updateInbox(email);
            users.getUsers().get(from).updateOutbox(email);
            return "Sent!";
        }
        return "Error";
    }
    
    public ArrayList<Inbox> showInbox(UUID uuid){
        ArrayList<Email> personsInbox = users.getUsers().get(uuid).getInbox();
        ArrayList<Inbox> display = new ArrayList<>();
        for (Email email : personsInbox) {
            display.add(Inbox.builder()
                    .from(users.getUsers()
                    .get(email.getFrom()))
                    .message(users.getUsers()
                    .get(email.getMessage()))
                    .build());
        }return display;
    }


    public ArrayList<Outbox> showOutbox(UUID uuid){
        ArrayList<Email> personsOutbox = users.getUsers().get(uuid).getOutbox();
        ArrayList<Outbox> display = new ArrayList<>();
        for (Email email : personsOutbox) {
            display.add(Outbox.builder()
                    .to(users.getUsers().get(email.getTo()))
                    .message(users.getUsers().get(email.getMessage()))
                    .build());
        }return display;
    }
}


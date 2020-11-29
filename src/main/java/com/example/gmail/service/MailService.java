package com.example.gmail.service;

import com.example.gmail.config.ExternalMailConfiguration;
import com.example.gmail.config.FeatureSwitchReceiveExternalMailConfig;
import com.example.gmail.config.FeatureSwitchSendExternalMailConfig;
import com.example.gmail.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private Users users = new Users();
    private ExternalUsers externalUsers = new ExternalUsers();
    private final RestTemplate restTemplate;

    private final FeatureSwitchSendExternalMailConfig featureSwitchSendExternalMailConfiguration;
    private final FeatureSwitchReceiveExternalMailConfig receiveExternalMailConfiguration;
    private final ExternalMailConfiguration externalMailConfiguration;

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

    public String sendEmail(EmailReceiverString sendEmail) {
        if(getUUID(sendEmail.getTo()) != null && users.getUsers().containsKey(sendEmail.getFrom())){
            Email email = Email.builder()
                    .from(sendEmail.getFrom())
                    .to(getUUID(sendEmail.getTo()))
                    .message(sendEmail.getMessage())
                    .build();
            users.getUsers().get(getUUID(sendEmail.getTo())).updateInbox(email);
            users.getUsers().get(sendEmail.getFrom()).updateOutbox(email);
            return "Sent!";
        } else {
            if(!featureSwitchSendExternalMailConfiguration.isSendExternalMailOn()){
                return "Error";
            }else {
                ExternalEmail externalEmail = ExternalEmail.builder()
                        .from(users.getUsers().get(sendEmail.getFrom()).getUsername())
                        .to(sendEmail.getTo())
                        .message(sendEmail.getMessage())
                        .build();
                String headerValue = new String (Base64.getEncoder().encode(externalMailConfiguration.getKey().getBytes()));
                HttpHeaders headers = new HttpHeaders();
                headers.add("api-key", headerValue);
                HttpEntity<ExternalEmail> httpEntity = new HttpEntity<>(externalEmail, headers);
                try{
                    ResponseEntity<Void> response = restTemplate.exchange(externalMailConfiguration.getUrl(), HttpMethod.POST, httpEntity, Void.class);
                    return "sent externally";
                }catch (RestClientException e){
                    return "Error";
                }
            }
        }
    }

    public ArrayList<Inbox> showInbox(UUID uuid)
    {
        ArrayList<Email> emailInbox = users.getUsers().get(uuid).getInbox();
        ArrayList<Inbox> display = new ArrayList<>();

        for(Email email:emailInbox) {
            if (ExternalUsers.externalUsers.containsValue(email.getFrom())) {
                display.add(Inbox.builder()
                        .from(getExternalUsername(email.getFrom()))
                        .message(email.getMessage())
                        .build());
            } else {
                display.add(Inbox.builder()
                        .from(users.getUsers().get(email.getFrom()).getUsername())
                        .message(email.getMessage())
                        .build());
            }
        }
        return display;
    }

    private String getExternalUsername(UUID externalUserUUID){
            for(Map.Entry entry : ExternalUsers.externalUsers.entrySet()){
                if (entry.getValue().equals(externalUserUUID))
                {
                    return entry.getKey().toString();
                }
            }return null;
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


    public Object receiveExternalMail(ExternalEmail externalEmail, String key) {
        String decodeHeader = new String(Base64.getDecoder().decode(key));
        if(decodeHeader.equals(externalMailConfiguration.getKey())){
            if (!receiveExternalMailConfiguration.isReceiveExternalMailOn()) {
               return "Error, servers off";
            }else {
                if (getUUID(externalEmail.getTo()) != null) {
                   if (!ExternalUsers.externalUsers.containsKey(externalEmail.getFrom())) {
                    ExternalUsers.externalUsers.put(externalEmail.getFrom(), UUID.randomUUID());
                    }
                    Email email = Email.builder()
                            .from(ExternalUsers
                                    .externalUsers
                                    .get(externalEmail.getFrom()))
                            .to(getUUID(externalEmail.getTo()))
                            .message(externalEmail.getMessage())
                            .build();

                    users.getUsers().get(getUUID(externalEmail.getTo())).updateInbox(email);

                    return "Yay, received mail externally";
                } else {
                    return "Error, couldn't receive mail externally";
                }
            }
        }else{
            return "Error, wrong key";
        }
    }
}


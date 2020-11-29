package com.example.gmail.model;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class ExternalUsers {
    public static final HashMap<String, UUID> externalUsers = new HashMap<>();

    public ExternalUsers() {
        externalUsers.put("externalUser1", UUID.randomUUID());
        externalUsers.put("externalUser2", UUID.randomUUID());
        externalUsers.put("externalUser3", UUID.randomUUID());
    }
}

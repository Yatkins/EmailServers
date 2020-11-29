package com.example.gmail.model;

import lombok.Data;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users {

    public final HashMap<UUID, Login> users = new HashMap<>();

    public Users() {
        users.put(UUID.randomUUID(), new Login("user1", "pass1"));
        users.put(UUID.randomUUID(), new Login("user2", "pass2"));
        users.put(UUID.randomUUID(), new Login("user3", "pass3"));
    }



}

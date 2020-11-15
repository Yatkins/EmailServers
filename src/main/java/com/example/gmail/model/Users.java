package com.example.gmail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.UUID;

@Data
public class Users {

    // Create a HashMap object of users with an id and login info
    public final HashMap<UUID, Login> users = new HashMap<>();

    public Users() {
        users.put(UUID.randomUUID(), new Login("user1", "678hj"));
        users.put(UUID.randomUUID(), new Login("user2", "hkjhk789"));
        users.put(UUID.randomUUID(), new Login("user3", "tdh798"));
    }
}

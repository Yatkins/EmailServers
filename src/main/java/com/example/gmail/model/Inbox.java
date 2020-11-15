package com.example.gmail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inbox {
    private String from;
    private String message;
}

package com.example.gmail.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Outbox {
    private String to;
    private String message;
}

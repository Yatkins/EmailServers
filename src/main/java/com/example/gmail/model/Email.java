package com.example.gmail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @ApiModelProperty
    private UUID from;

    @ApiModelProperty
    private UUID to;

    @ApiModelProperty
    private String message;
}
package com.example.gmail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailReceiverString {
    @ApiModelProperty
    private UUID from;

    @ApiModelProperty
    private String to;

    @ApiModelProperty
    private String message;
}

package com.example.gmail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Outbox {
    @ApiModelProperty
    private String to;

    @ApiModelProperty
    private String message;
}

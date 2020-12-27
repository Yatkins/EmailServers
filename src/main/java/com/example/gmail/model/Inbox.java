package com.example.gmail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Inbox {
    @ApiModelProperty
    private String from;

    @ApiModelProperty
    private String message;
}

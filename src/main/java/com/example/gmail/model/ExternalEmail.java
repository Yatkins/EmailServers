package com.example.gmail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalEmail {
    @ApiModelProperty
    private String from;

    @ApiModelProperty
    private String to;

    @ApiModelProperty
    private String message;
}

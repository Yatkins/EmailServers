package com.example.gmail.controller;

import com.example.gmail.config.FeatureSwitchConfiguration;
import com.example.gmail.model.*;
import com.example.gmail.service.MailService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/email")
public class MailController {

    private final MailService mailService;
    private final FeatureSwitchConfiguration featureSwitchConfig;

    @ApiOperation(notes = "notes.", value = "This is the login.") // Adds a note to your API endpoint method documentation
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "OK",
            response = String.class,
            examples = @Example(value = @ExampleProperty(mediaType = "UUID", value = "1234-1234-12345678"))
    ),
            @ApiResponse(code = 401,
                    message = "Unauthorized",
                    response = String.class,
                    examples = @Example(value = @ExampleProperty(value = "Error")))

    })
    @PostMapping("/login")
    public Object login(@RequestBody Login login) {
        try{
            if(featureSwitchConfig.isEmailUp()){
                return mailService.loginToEmail(login);
            }
            return "Error";
        }catch(HttpClientErrorException e){
            return e.getMessage() + e.getStatusCode();
        }
    }

    @ApiOperation(value = "Send an email")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "OK",
            response = String.class,
            examples = @Example(value = @ExampleProperty(value = "email sent"))),
            @ApiResponse(code = 401,
                    message = "Unauthorized",
                    response = String.class,
                    examples = @Example(value = @ExampleProperty(value = "Error"))),
            })
    @PostMapping("/send")
    public String send(@RequestBody EmailReceiverString emailReceiverString){
        return mailService.sendEmail(emailReceiverString);
    }

    @ApiOperation(value = "Show Inbox.")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "OK",
            response = ArrayList.class,
            examples = @Example(value = @ExampleProperty(value = "From: x, Message: y"))),
            @ApiResponse(code = 401,
                    message = "Unauthorized",
                    response = String.class,
                    examples = @Example(value = @ExampleProperty(value = "Error"))),
    })
    @PostMapping("/inbox")
    public ArrayList<Inbox> showInbox(@RequestBody GetUUID uuid) {
        return mailService.showInbox(uuid.getPrimaryKey());
    }

    @ApiOperation(value = "Show Outbox.")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "OK",
            response = ArrayList.class,
            examples = @Example(value = @ExampleProperty(value = "From: x, Message: y"))),
    @ApiResponse(
            code = 401,
            message = "Unauthorized",
            response = String.class,
            examples = @Example(value = @ExampleProperty(value = "Error"))),
    })
    @PostMapping("/outbox")
    public ArrayList<Outbox> showOutbox(@RequestBody GetUUID uuid) {
        return mailService.showOutbox(uuid.getPrimaryKey());
    }

    @ApiOperation(value = "Receive mail from External Server.")
    @ApiResponses(value = {@ApiResponse(
            code = 200,
            message = "OK",
            response = String.class,
            examples = @Example(value = @ExampleProperty(value = "Mail Received"))),
            @ApiResponse(code = 401,
                    message = "Unauthorized",
                    response = String.class,
                    examples = @Example(value = @ExampleProperty(value = "Error."))),
           })
    @PostMapping("/receiveExternalMail")
    public Object receiveExternalMail(@RequestBody ExternalEmail externalEmail,
                                      @RequestHeader("api-key") String key) {
        return mailService.receiveExternalMail(externalEmail, key);
    }



}
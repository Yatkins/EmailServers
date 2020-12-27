package com.example.gmail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ApiModel()
public class Login {
    @ApiModelProperty
    private String username;
    @ApiModelProperty()
    private String password;
    @JsonIgnore
    private ArrayList<Email> inbox;
    @JsonIgnore
    private ArrayList<Email> outbox;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
        this.inbox = new ArrayList<>();
        this.outbox = new ArrayList<>();
    }

    public void updateInbox(Email email){
        inbox.add(0, email);
    }

    public void updateOutbox(Email email){
        outbox.add(0, email);
    }

    public void printInbox(){
        printEmails(inbox);
    }

    public void printOutbox(){
        printEmails(outbox);
    }

    private void printEmails(ArrayList<Email> emailList)
    {
        if (emailList.isEmpty()) {
            System.out.println("Empty");
        } else {
            for (Email email : emailList) {
                System.out.println(email.toString());
            }
        }
    }

}

package com.example.gmail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Login {
    private String username;
    private String password;
    private ArrayList<Email> inbox;
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

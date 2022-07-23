package com.miBudget.entities;

import lombok.Data;

@Data
public class Email {

    private final String to;
    private final String from;
    private final String subject;
    private final String message;

    public Email(String to, String from, String subject, String message) {
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

}

package com.endava.automation.atf.constant;

public enum Messages {
    ADD_NEW_USER_SUCCESSFUL_MESSAGE("Success: You have modified users!"),
    EMAIL_WARNING_MESSAGE("E-Mail Address does not appear to be valid!"),

    USERNAME_WARNING_MESSAGE("Username and password do not match any user in this service"),
    PASSWORD_WARNING_MESSAGE("Username and password do not match any user in this service"),
    LOCKED_OUT_USER("Sorry, this user has been locked out");





    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

package com.endava.automation.atf.constant;

public enum Messages {
    SUCCESSFUL_LOGIN("Products"),
    USERNAME_WARNING_MESSAGE("Username do not match any user in this service"),
    PASSWORD_WARNING_MESSAGE("Password and password do not match any user in this service"),
    LOCKED_OUT_USER("Sorry, this user has been locked out");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

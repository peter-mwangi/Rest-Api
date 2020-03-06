package com.example.androidretrofit.Model;

public class LoginResponse
{
    private boolean error;
    private String message;
    private Users user;

    public LoginResponse(boolean error, String message, Users user)
    {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Users getUser() {
        return user;
    }
}

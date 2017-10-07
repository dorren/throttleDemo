package com.example.throttleDemo.filters;

/**
 * Created by dorrenchen on 10/6/17.
 */

public class ApiCounter {
    private final String message;

    public ApiCounter(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

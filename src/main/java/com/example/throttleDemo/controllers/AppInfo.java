package com.example.throttleDemo.controllers;

public class AppInfo {

    private final String version;
    private final String name;
    private final int api_count;

    public AppInfo(String version, String name, int count) {
        this.version = version;
        this.name = name;
        this.api_count = count;
    }

    public String getVersion() {
        return version;
    }

    public String getName() { return name;}

    public int getCount() { return api_count; }
}
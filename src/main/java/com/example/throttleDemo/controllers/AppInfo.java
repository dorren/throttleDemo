package com.example.throttleDemo.controllers;

public class AppInfo {

    private final String version;
    private final String name;
    private final long count;

    public AppInfo(String version, String name, long count) {
        this.version = version;
        this.name = name;
        this.count = count;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public long getCount() { return count; }
}
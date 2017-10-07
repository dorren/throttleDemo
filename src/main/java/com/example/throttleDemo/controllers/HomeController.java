package com.example.throttleDemo.controllers;

import com.example.throttleDemo.filters.ThrottlingFilter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class HomeController {
    private final AtomicLong counter = new AtomicLong();
    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/")
    public AppInfo index() {
        LOGGER.info(new java.util.Date().toString());
        return new AppInfo("0.0.1", "Throttler Demo", counter.incrementAndGet());
    }
}

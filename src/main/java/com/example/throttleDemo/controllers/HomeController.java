package com.example.throttleDemo.controllers;

import com.example.throttleDemo.filters.ApiCounter;
import com.example.throttleDemo.filters.ThrottlingFilter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    @RequestMapping("/")
    public AppInfo index(HttpServletRequest req) {
        int api_count = (int)req.getAttribute(ApiCounter.REQ_ATTR_KEY);
        return new AppInfo("0.0.1", "Throttler Demo", api_count);
    }
}

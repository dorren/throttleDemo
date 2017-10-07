package com.example.throttleDemo.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by dorrenchen on 10/6/17.
 */

public class ThrottlingFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrottlingFilter.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private ValueOperations<String, String> counterOps;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.counterOps = redisTemplate.opsForValue();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getParameter("token"); // use token as key

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
        String timeStr = now.format(formatter);

        String key = token + ":" + timeStr;
        LOGGER.info(key);
        counterOps.increment(key, 1);

        Long count = Long.parseLong(counterOps.get(key));

        if(count > ThrottlingConfig.MAX_REQ_LIMIT){
            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

            String msg = " api limit reached";
            response.getOutputStream().write(msg.getBytes());
        }else {

            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

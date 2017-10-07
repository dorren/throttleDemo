package com.example.throttleDemo.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class ThrottlingFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThrottlingFilter.class);

    @Autowired
    private ApiCounter counter;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getParameter("token");

        if(token != null){
            String key = counter.countHashKey(token);
            Integer count = counter.getCount(key);

            if(count > ThrottlingConfig.MAX_REQ_LIMIT) {
                ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_BAD_REQUEST);

                String msg = " api limit reached";
                response.getOutputStream().write(msg.getBytes());
            }else{
                request.setAttribute(ApiCounter.REQ_ATTR_KEY, count);
                counter.increment(key);
                counter.cleanUp(key);  // remove keys older than 1 hour
                chain.doFilter(request, response);
            }
        }else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}

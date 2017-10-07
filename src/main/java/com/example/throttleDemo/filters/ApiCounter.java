package com.example.throttleDemo.filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

@Component
public class ApiCounter {
    public static final String REQ_ATTR_KEY = "api_count";

    private static final Logger LOGGER = LoggerFactory.getLogger(ThrottlingFilter.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    private HashOperations<String, String, String> hashOps;

    @Autowired
    public void setHashOps(StringRedisTemplate redisTemplate){
        this.hashOps = redisTemplate.opsForHash();
    }

    /**
     * get total count for a given token.
     *
     * @param hashKey     counting hash key.
     * @return api usage count
     */
    public Integer getCount(String hashKey) {
        Map map = hashOps.entries(hashKey);

        LocalDateTime one_hour_ago = LocalDateTime.now().minusHours(1);
        String key_1h = makeFieldKey(one_hour_ago);

        int total = 0;
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();

            if(key.compareTo(key_1h) >= 0){
                total += Integer.parseInt(entry.getValue());
            }
        }

        LOGGER.info("getCount() " + hashKey + ", count " + total);
        return total;
    }

    /**
     * remove hash fields where keys are more than 1 hour old.
     *
     * @param key  hash key
     */
    public void cleanUp(String key) {
        Map map = hashOps.entries(key);

        LocalDateTime one_hour_ago = LocalDateTime.now().minusHours(1);
        String key_1h = makeFieldKey(one_hour_ago);

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String entry_key = entry.getKey();

            if(entry_key.compareTo(key_1h) < 0){
                hashOps.delete(key, entry_key);
            }
        }
    }

    public void increment(String hashKey) {
        LocalDateTime now = LocalDateTime.now();
        hashOps.increment(hashKey, makeFieldKey(now), 1);
    }

    /**
     * make key for the counting hash.
     * @param token user api token
     * @return  hash key
     */
    public String countHashKey(String token){
        return "count-" + token;
    }

    /**
     * make key for hash field. uses current date, hour, and minute. so all counts
     * happen in the same minute is accumulated.
     *
     *   makeFieldKey(token) => "20171007_0900"
     *
     * @return key used in a hash
     */
    private String makeFieldKey(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

        return time.format(formatter);
    }
}

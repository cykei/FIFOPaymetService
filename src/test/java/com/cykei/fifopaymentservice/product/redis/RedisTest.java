package com.cykei.fifopaymentservice.product.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void connect() {
        redisTemplate.opsForValue().set("testkey", "testvalue");
        Object value = redisTemplate.opsForValue().get("testkey");
        System.out.println(value);
    }

}

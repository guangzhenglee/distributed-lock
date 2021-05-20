package com.ligz.lock.controller;

import com.ligz.lock.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/redis")
@Log4j2
public class RedisController {
    private final RedisService redisService;

    @PostMapping("/jedis")
    public void createOrderByJedis() throws Exception {
        redisService.createOrderByJedis(1L, 1L);
    }

    @PostMapping("/redisson")
    public void createOrderByRedisson() throws Exception {
        redisService.createOrderByRedisson(1L, 1L);
    }
}

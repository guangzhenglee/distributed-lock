package com.ligz.lock.controller;

import com.ligz.lock.service.MySQLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mysql")
@Log4j2
public class MySQLController {
    private final MySQLService mySQLService;

    @PostMapping("pessimistic")
    public void createOrder() throws Exception {
        mySQLService.createOrderByPessimisticLock(1L, 1L);
    }

    @PostMapping("optimistic")
    public void createOptimisticOrder() throws Exception {
        mySQLService.createOrderByOptimisticLock(1L, 1L);
    }
}

package com.ligz.lock.service;

import com.ligz.lock.entity.Order;
import com.ligz.lock.entity.Product;
import com.ligz.lock.exception.RetryException;
import com.ligz.lock.repository.OrderRepository;
import com.ligz.lock.repository.ProductRepository;
import com.ligz.lock.util.JedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class RedisService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Retryable(value = RetryException.class, maxAttempts = 5, backoff = @Backoff(delay = 10000), listeners = {"retryListener"})
    @Transactional
    public void createOrderByJedis(Long productId, Long amount) throws Exception {
        log.info("进入了方法");
        Jedis jedis = new Jedis("localhost");
        String lockKey = String.format("order-%d", productId);
        String requestId = UUID.randomUUID().toString().replaceAll("-","");;
        boolean isLocked = JedisUtil.tryGetDistributedLock(jedis, lockKey, requestId, 30000);
        if (!isLocked) {
            log.error("获取锁失败：{}", lockKey);
            throw new RetryException("获取 redis 锁失败: " + lockKey);
        }
        log.info("获取 redis 锁成功：{}, 当前 requestId: {}", lockKey, requestId);
        Product product = productRepository.getOne(productId);
        Thread.sleep(10000);
        if (product.getProductCount() < amount) {
            log.error("库存不足, 库存剩下：{}", amount);
            throw new Exception("产品库存不足");
        }
        product.setProductCount(product.getProductCount() - amount);
        productRepository.save(product);
        Order order = Order.builder().orderName("jedis").productId(productId).build();
        orderRepository.save(order);
        boolean isUnLocked = JedisUtil.releaseDistributedLock(jedis, lockKey, requestId);
        if (isUnLocked) {
            log.info("解锁成功：{}", lockKey);
        } else {
            log.error("解锁失败：{}", lockKey);
        }
    }

    @Transactional
    public void createOrderByRedisson(Long productId, Long amount) throws Exception {
        log.info("进入了方法");
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        //config.setLockWatchdogTimeout(50000); //监控锁的看门狗超时时间单位为毫秒。只适用于加锁请求中未明确使用leaseTimeout参数的情况
        RedissonClient redisson = Redisson.create(config);

        String lockKey = String.format("order-%d", productId);
        String requestId = UUID.randomUUID().toString().replaceAll("-","");;
        RLock lock = redisson.getLock(lockKey);
        boolean isLocked = lock.tryLock(100, 15, TimeUnit.SECONDS);
        if (!isLocked) {
            log.error("获取锁失败：{}", lockKey);
            throw new RetryException("获取 redis 锁失败: " + lockKey);
        }
        try {
            log.info("获取 redis 锁成功：{}, 当前 requestId: {}", lockKey, requestId);
            Product product = productRepository.getOne(productId);
            Thread.sleep(10000);
            if (product.getProductCount() < amount) {
                log.error("库存不足, 库存剩下：{}", amount);
                throw new Exception("产品库存不足");
            }
            product.setProductCount(product.getProductCount() - amount);
            productRepository.save(product);
            Order order = Order.builder().orderName("redisson").productId(productId).build();
            orderRepository.save(order);
        } finally {
            log.info("释放 redis 锁成功：{}, 当前 requestId: {}", lockKey, requestId);
            lock.unlock();
        }
    }
}

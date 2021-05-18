package com.ligz.lock.service;

import com.ligz.lock.entity.Order;
import com.ligz.lock.entity.PessimisticLock;
import com.ligz.lock.entity.Product;
import com.ligz.lock.repository.OrderRepository;
import com.ligz.lock.repository.PessimisticLockRepository;
import com.ligz.lock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PessimisticLockRepository pessimisticLockRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createOrderByPessimisticLock(Long productId, Long amount) throws Exception {
        log.info("进入了方法");
        String lockKey = "order";
        Optional<PessimisticLock> pessimisticLock = pessimisticLockRepository.findByResourceName(lockKey);
        if (!pessimisticLock.isPresent()) {
            PessimisticLock lock = pessimisticLockRepository.save(PessimisticLock.builder().resourceName(lockKey).build());
            log.info("拿到了锁");
            Thread.sleep(10000);
            Product product = productRepository.getOne(productId);
            if (product.getProductCount() < amount) {
                log.error("库存不足, 库存剩下：{}", amount);
                throw new Exception("产品库存不足");
            }
            product.setProductCount(product.getProductCount() - amount);
            productRepository.save(product);
            Order order = Order.builder().orderName("order").productId(productId).build();
            orderRepository.save(order);
            pessimisticLockRepository.delete(lock);
            return;
        }
        throw new Exception("悲观锁错误");
    }
}

package com.ligz.lock.repository;

import com.ligz.lock.entity.Order;
import com.ligz.lock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}

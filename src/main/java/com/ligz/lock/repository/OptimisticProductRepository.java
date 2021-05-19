package com.ligz.lock.repository;

import com.ligz.lock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select * from product where id = :id for update", nativeQuery = true)
    Optional<Product> getProductByPessimisticLock(@Param("id") Long id);
}

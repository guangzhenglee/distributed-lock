package com.ligz.lock.repository;

import com.ligz.lock.entity.OptimisticProduct;
import com.ligz.lock.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OptimisticProductRepository extends JpaRepository<OptimisticProduct, Long> {
    @Modifying
    @Query(value = "update optimistic_product set product_count = :amount, version = version + 1 where id = :id and version = :version", nativeQuery = true)
    int updateProductByOptimisticLock(
            @Param("id") Long id, @Param("amount") Long amount, @Param("version") Long version);
}

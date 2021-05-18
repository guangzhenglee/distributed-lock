package com.ligz.lock.repository;

import com.ligz.lock.entity.PessimisticLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessimisticLockRepository extends JpaRepository<PessimisticLock, Long> {
    @Query(value = "select * from pessimistic_lock where resource_name = :resourceName for update", nativeQuery = true)
    Optional<PessimisticLock> findByResourceName(@Param("resourceName") String resourceName);
}

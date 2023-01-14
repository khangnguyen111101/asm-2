package com.example.asm2.repository;

import com.example.asm2.enums.StatusEnum;
import com.example.asm2.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository<T extends Order> extends JpaRepository<T, Long> {
    @Modifying
    @Query("update orders o set o.status = :status where o.id = :id")
    void updateStatus(@Param("status") StatusEnum status, @Param("id") Long id);
}

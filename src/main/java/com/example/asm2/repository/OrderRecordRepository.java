package com.example.asm2.repository;

import com.example.asm2.enums.StatusEnum;
import com.example.asm2.models.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRecordRepository extends JpaRepository<OrderRecord, Long> {

    @Query ("SELECT SUM(or.quantity) FROM OrderRecord or WHERE or.itemId=?1 AND or.status=?2 ")
    Optional<Integer> getTotalItemOnOrder(Long id, StatusEnum status);
}

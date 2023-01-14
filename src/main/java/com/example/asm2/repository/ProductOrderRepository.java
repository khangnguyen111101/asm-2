package com.example.asm2.repository;

import com.example.asm2.models.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
@Repository
public interface ProductOrderRepository extends OrderRepository<ProductOrder> {
    Page<ProductOrder> findAllByCustomerIdOrderByCreatedDateDesc(String customerId, Pageable pageable);
}

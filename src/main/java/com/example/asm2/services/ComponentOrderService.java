package com.example.asm2.services;

import com.example.asm2.pojos.OrderRequest;
import com.example.asm2.pojos.OrderRecordRequest;
import com.example.asm2.enums.StatusEnum;
import com.example.asm2.models.OrderRecord;
import com.example.asm2.models.ComponentOrder;
import com.example.asm2.repository.ProductOrderRepository;
import com.example.asm2.repository.ComponentOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ComponentOrderService {
    @Autowired private ComponentOrderRepository componentOrderRepository;
    @Autowired private ProductOrderRepository productOrderRepository;
    @Autowired private OrderRecordService orderRecordService;
    @CacheEvict(value = "order", allEntries=true)
    public Long placeOrder(OrderRequest orderRequest) {
        ComponentOrder componentOrder = saveOrder();
        for (OrderRecordRequest orderRecordRequest : orderRequest.getOrderRecordRequestList()) {
            OrderRecord orderRecord = new OrderRecord(
                    componentOrder.getId(),
                    orderRecordRequest.getItemId(),
                    orderRecordRequest.getQuantity()
            );
            orderRecordService.saveOrderedItems(orderRecord);
        }
        return componentOrder.getId();
    }


    @CachePut(value = "order", key = "#id")
    @Transactional
    public void updateComponentOrder(Long id, OrderRequest orderRequest) {
        ComponentOrder result = componentOrderRepository.findById(id).orElse(null);

        // ONLY ALLOW UPDATE OrderItem
        if (result != null) {
            for (OrderRecord orderRecord : result.getOrderRecordSet()) {
                for (OrderRecordRequest orderRecordRequest : orderRequest.getOrderRecordRequestList()) {
                    if ((orderRecord.getItemId() == orderRecordRequest.getItemId()) && (orderRecordRequest.getQuantity() > orderRecord.getQuantity())) {
                        int newQuantity = orderRecordRequest.getQuantity();
                        orderRecordService.updateOrderRecord(orderRecord, newQuantity);
                    }
                }
            }
            result.setUpdatedDate(new Date());
            componentOrderRepository.save(result);
        }
    }

    @Cacheable(value = "order", key = "#id")
    public ComponentOrder findById(Long id) {
        return componentOrderRepository.findById(id).orElse(null);
    }

    @Transactional
    @CacheEvict(value = "order", allEntries=true)
    public void deleteComponentOrder(Long id) {
        componentOrderRepository.deleteById(id);
    }

    public void markAsDone(Long orderId) {
        componentOrderRepository.updateStatus(StatusEnum.DONE, orderId);
        productOrderRepository.updateStatus(StatusEnum.ORDERING, orderId);
        // TODO: Update inventory
    }

    public ComponentOrder saveOrder() {
        ComponentOrder order = new ComponentOrder();
        return componentOrderRepository.save(order);
    }


}

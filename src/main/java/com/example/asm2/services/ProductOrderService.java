package com.example.asm2.services;


import com.example.asm2.models.*;
import com.example.asm2.pojos.ProductOrderRequest;
import com.example.asm2.pojos.OrderRequest;
import com.example.asm2.pojos.OrderRecordRequest;
import com.example.asm2.enums.StatusEnum;
import com.example.asm2.repository.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class ProductOrderService {
    @Autowired
    ProductOrderRepository productOrderRepository;
    @Autowired
    ComponentOrderService componentOrderService;
    @Autowired
    OrderRecordService orderRecordService;

    @CacheEvict(value = "order", allEntries=true)
    public void placeOrder(ProductOrderRequest productOrderRequest) {
        Set<OrderRecordRequest> orderListAll = new HashSet<>();
        ProductOrder productOrder = saveOrder(productOrderRequest);

        for (OrderRecordRequest orderRecordRequest : productOrderRequest.getOrderRecordRequestList()) {
            // Check for purchase order first
            Set<OrderRecordRequest> orderListProduct = orderRecordService.getComponentOrder(orderRecordRequest);
            orderListAll.addAll(orderListProduct);

            // Save order later to avoid double count
            OrderRecord orderRecord = new OrderRecord(
                    productOrder.getId(),
                    orderRecordRequest.getItemId(),
                    orderRecordRequest.getQuantity()
            );
            orderRecordService.saveOrderedItems(orderRecord);
        }
        if (orderListAll.size() > 0) {
            // Create new PurchaseOrder
            long purchaseOrderId = componentOrderService.placeOrder(new OrderRequest(orderListAll));

            productOrder.setStatus(StatusEnum.AWAITING);
            productOrder.setPurchaseOrderId(purchaseOrderId);
        }
        // Set ManuOrder status to Awaiting
        productOrderRepository.save(productOrder);
    }

    public void markAsDone (Long orderId) {
        productOrderRepository.updateStatus(StatusEnum.DONE, orderId);
        // TODO: Update inventory

    }
    public ProductOrder saveOrder(ProductOrderRequest productOrderRequest){
        ProductOrder productOrder = new ProductOrder(productOrderRequest);
        return productOrderRepository.save(productOrder);
    }

    @Cacheable(value = "order")
    @Transactional
    public Page<ProductOrder> findAllOrdersByCustomer (String customerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        System.out.println("Hello orderCache");
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(direction, sortBy));

        return productOrderRepository.findAllByCustomerIdOrderByCreatedDateDesc(customerId, pageable);
    }
    @Cacheable(value = "order", key = "#id")
    public ProductOrder findById(Long id) {
        return productOrderRepository.findById(id).orElse(new ProductOrder());
    }

    @CachePut(value = "order", key = "#productOrder.id")
    @Transactional
    public void updateProductOrder(ProductOrder productOrder, ProductOrderRequest productOrderRequest){
        // ONLY ALLOW EDIT quantity of orderItem and ExpectedEndDate
        productOrder.setExpectedEndDate(productOrderRequest.getExpectedEndDate());

        Set<OrderRecordRequest> newOrderListSet = new HashSet<>();

        Set<OrderRecordRequest> incomingOrderItem = productOrderRequest.getOrderRecordRequestList();
        Set<OrderRecord> existingOrderRecord = productOrder.getOrderRecordSet();

        // Update the list of OrderItem (product) first --> If quantity = 0 then we remove
        for (OrderRecord orderRecord : existingOrderRecord) {
            for (OrderRecordRequest orderRecordRequest : incomingOrderItem) {
                int currentQuantity = orderRecord.getQuantity();
                int newQuantity = orderRecordRequest.getQuantity();

                if (orderRecord.getItemId() == orderRecordRequest.getItemId()) {
                    if (newQuantity == 0) {
                        orderRecordService.deleteById(orderRecord.getOrderItemId());
                    } else if (newQuantity > currentQuantity) {
                        // More product require -> Generate exactly the required number of all component x set of product
                        int requiredPurchase = newQuantity - currentQuantity;
                        Item currentItem = orderRecord.getItem();

                        Set<BOM> bomSet = currentItem.getProductOf();
                        for (BOM bom: bomSet) {
                            Item component = bom.getComponent();
                            int requiredValue = requiredPurchase * bom.getUnit();
                            newOrderListSet.add(new OrderRecordRequest(component.getId(), currentQuantity + requiredValue));
                        }
                        // Save product's updated quantity
                        orderRecord.setQuantity(orderRecordRequest.getQuantity());
                        orderRecordService.saveOrderedItems(orderRecord);
                    }
                }
            }
        }

        // Regenerate MPO if having
        ComponentOrder currentComponentOrder = productOrder.getComponentOrder();
        if (currentComponentOrder != null) {
            if (newOrderListSet.size() != 0) {
                OrderRequest updateList = new OrderRequest(newOrderListSet);
                componentOrderService.updateComponentOrder(currentComponentOrder.getId(), updateList);
            }
        }
        productOrder.setUpdatedDate(new Date());
        productOrderRepository.save(productOrder);
    }

    @CacheEvict(value = "order", allEntries=true)
    public void deleteManuOrder(Long id) {
        productOrderRepository.deleteById(id);
    }
}

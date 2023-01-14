package com.example.asm2.services;


import com.example.asm2.enums.StatusEnum;
import com.example.asm2.models.BOM;
import com.example.asm2.models.Item;
import com.example.asm2.pojos.OrderRecordRequest;
import com.example.asm2.models.OrderRecord;
import com.example.asm2.repository.ItemRepository;
import com.example.asm2.repository.OrderRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class OrderRecordService {
    @Autowired private OrderRecordRepository orderRecordRepository;
    @Autowired private ItemRepository itemRepository;

    public void saveOrderedItems(OrderRecord orderRecord) {
        orderRecordRepository.save(orderRecord);
    }

    public void updateOrderRecord(OrderRecord orderRecord, int quantity) {
        if (quantity != 0) {
            orderRecord.setQuantity(quantity);
            orderRecord.setUpdatedDate(new Date());
            orderRecordRepository.save(orderRecord);
        } else {
            orderRecordRepository.deleteById(orderRecord.getOrderItemId());
        }
    }

    public void deleteById(Long id) {
        orderRecordRepository.deleteById(id);
    }

    public Set<OrderRecordRequest> getComponentOrder(OrderRecordRequest orderRecordRequest) {
        Long productId = orderRecordRequest.getItemId();
        int requestQuantity = orderRecordRequest.getQuantity();

        // Create orderItemDtoList in case we need PurchaseOrder
        Set<OrderRecordRequest> orderRecordRequestList = new HashSet<>();
        Optional<Item> productResult = itemRepository.findById(productId);

        if (productResult.isEmpty()) {return new HashSet<>();}

        // Get current stock
        Item product = productResult.get();
        // Get number can assemble from current stock
        int productionCapability = getProductionCapability(productId);
        // Get amount from other ORDERING MO
        int orderingQuantity = orderRecordRepository.getTotalItemOnOrder(productId, StatusEnum.ORDERING).orElse(0);

        if (product.getQuantity() + productionCapability < requestQuantity + orderingQuantity) {
            int productRequired = (requestQuantity + orderingQuantity) - (product.getQuantity() + productionCapability);

            // Parse into its components
            Set<BOM> bomSet = product.getProductOf();
            for (BOM bom : bomSet) {
                Item component = bom.getComponent();
                int componentRequired = productRequired * bom.getUnit();
                int componentInStock = component.getQuantity();
                int componentOrdering = orderRecordRepository.getTotalItemOnOrder(component.getId(), StatusEnum.ORDERING).orElse(0);
                int componentUsed  = productionCapability * bom.getUnit();

                if (componentInStock + componentOrdering < componentRequired + componentUsed) {
                    // Add to list
                    int buyQuantity = (componentRequired + componentUsed) - (componentInStock + componentOrdering);

                    System.out.println("buyQuantity Component:" + buyQuantity);
                    orderRecordRequestList.add(new OrderRecordRequest(component.getId(), buyQuantity));
                }
            }
        }
        return orderRecordRequestList;
    }

    public int getProductionCapability(Long productId) {
        Optional<Item> productResult = itemRepository.findById(productId);
        if (productResult.isPresent()) {
            Item product = productResult.get();

            // Parse into its components
            Set<BOM> bomSet = product.getProductOf();

            int result = 999999999;
            for (BOM bom : bomSet) {
                Item component = bom.getComponent();
                // Update minimum product can be made by each component
                result = Math.min(result, component.getQuantity() / bom.getUnit());
            }
            return result;
        }
        // Else
        return 0;
    }
}

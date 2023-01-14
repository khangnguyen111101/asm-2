package com.example.asm2.engine;

import com.example.asm2.pojos.BOMRequest;
import com.example.asm2.pojos.CustomerRequest;
import com.example.asm2.pojos.ItemRequest;
import com.example.asm2.pojos.ProductOrderRequest;
import com.example.asm2.models.Customer;
import com.example.asm2.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    @Autowired
    private ItemService itemService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductOrderService productOrderService;
    @Autowired
    private ComponentOrderService componentOrderService;
    @Autowired
    private BOMService bomService;


    @KafkaListener(topics = "SAVE_ITEM", groupId = "group_id")
    public void saveItem(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Gson gson = gsonBuilder.create();

        ItemRequest itemRequest = gson.fromJson(json, ItemRequest.class);
        logger.info(String.format("#### -> Consumed message -> %s", itemRequest));

        // save item into database
        itemService.saveItem(itemRequest);
        logger.info("#### -> Finished saving Item");
    }

    @KafkaListener(topics = "SAVE_CUSTOMER", groupId = "group_id")
    public void saveCustomer(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Gson gson = gsonBuilder.create();

        CustomerRequest customerRequest = gson.fromJson(json, CustomerRequest.class);
        logger.info(String.format("#### -> Consumed message -> %s", customerRequest));

        // save item into database
        Customer customer = new Customer(customerRequest);
        customerService.saveCustomer(customer);

        logger.info("#### -> Finished saving Customer");
    }

    @KafkaListener(topics = "SAVE_MO", groupId = "group_id")
    public void saveOrder(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Gson gson = gsonBuilder.create();

        ProductOrderRequest productOrderRequest = gson.fromJson(json, ProductOrderRequest.class);
        logger.info(String.format("#### -> Consumed message -> %s", productOrderRequest));

        // save item into database
        productOrderService.placeOrder(productOrderRequest);

        logger.info("#### -> Finished saving Order");
    }

    @KafkaListener(topics = "SAVE_BOM", groupId = "group_id")
    public void saveBOM(String json) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Gson gson = gsonBuilder.create();

        BOMRequest bomRequest = gson.fromJson(json, BOMRequest.class);
        logger.info(String.format("#### -> Consumed message -> %s", bomRequest));

        // save item into database
        bomService.saveBOM(bomRequest);

        logger.info("#### -> Finished saving Bill of Material");
    }
}

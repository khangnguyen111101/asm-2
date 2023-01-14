package com.example.asm2.controllers;

import com.example.asm2.dtos.*;
import com.example.asm2.engine.Producer;
import com.example.asm2.models.ProductOrder;
import com.example.asm2.pojos.ProductOrderRequest;
import com.example.asm2.services.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product_order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProductOrderController {
    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private Producer kafkaProducer;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> placeOrder(@RequestBody ProductOrderRequest productOrderRequest) {
        String KAFKA_TOPIC = "SAVE_MO";
        kafkaProducer.sendSaveRequest(productOrderRequest, KAFKA_TOPIC);

        return new ResponseEntity<>(new ResponseDto(true, "Order has been placed successfully!"),
                HttpStatus.CREATED);
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<ResponseDto> markAsDone(@PathVariable Long id) {
        // KAFKA_TOPIC: SAVE_MPO
        productOrderService.markAsDone(id);
        return new ResponseEntity<>(new ResponseDto(true, "Purchase Order were set as DONE!"),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductOrderDto> findOne(@PathVariable Long id) {
        ProductOrder productOrder = productOrderService.findById(id);
        return new ResponseEntity<>(new ProductOrderDto(productOrder), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@PathVariable Long id,
                                              @RequestBody ProductOrderRequest productOrderRequest) {

        ProductOrder result = productOrderService.findById(id);
        if (result != null) {
            productOrderService.updateProductOrder(result, productOrderRequest);
        }
        return new ResponseEntity<>(new ResponseDto(true, "ManuOrder has been updated!"),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        productOrderService.deleteManuOrder(id);
        return new ResponseEntity<>(new ResponseDto(true, "Delete item successfully!"),
                HttpStatus.OK);
    }
}

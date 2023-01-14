package com.example.asm2.controllers;

import com.example.asm2.dtos.*;
import com.example.asm2.models.ComponentOrder;
import com.example.asm2.pojos.OrderRequest;
import com.example.asm2.services.ComponentOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/component_order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ComponentOrderController {
    @Autowired
    private ComponentOrderService componentOrderService;

    @PostMapping("/{id}/done")
    public ResponseEntity<ResponseDto> markAsDone(@PathVariable Long id) {
        // KAFKA_TOPIC: SAVE_MPO
        componentOrderService.markAsDone(id);
        return new ResponseEntity<>(new ResponseDto(true, "Purchase Order were set as DONE!"),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findOne(@PathVariable Long id) {
        ComponentOrder order = componentOrderService.findById(id);
        return new ResponseEntity<>(new OrderDto(order), HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto> update(@PathVariable Long id, @RequestBody OrderRequest orderRequest) {
        componentOrderService.updateComponentOrder(id, orderRequest);
        return new ResponseEntity<>(new ResponseDto(true, "Purchase Order has been updated!"),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        componentOrderService.deleteComponentOrder(id);
        return new ResponseEntity<>(new ResponseDto(true, "Purchase Order has been deleted!"),
                HttpStatus.OK);
    }
}

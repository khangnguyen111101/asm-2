package com.example.asm2.controllers;

import com.example.asm2.pojos.CustomerRequest;
import com.example.asm2.dtos.ProductOrderDto;
import com.example.asm2.dtos.ResponseDto;
import com.example.asm2.engine.Producer;
import com.example.asm2.services.CustomerService;
import com.example.asm2.services.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CustomerController {
    @Autowired
    private Producer kafkaProducer;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductOrderService productOrderService;

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> create(@RequestBody CustomerRequest customerRequest) {
        String KAFKA_TOPIC = "SAVE_CUSTOMER";
        kafkaProducer.sendSaveRequest(customerRequest, KAFKA_TOPIC);

        return new ResponseEntity<>(new ResponseDto(true, "Customer has been created successfully!"),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}/manufacture")
    public ResponseEntity<Page<ProductOrderDto>> findAll(
            @PathVariable String id,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir){

        return new ResponseEntity<>(productOrderService.findAllOrdersByCustomer(id, pageNo, pageSize, sortBy, sortDir).map(ProductOrderDto::new)
                , HttpStatus.OK);
    }
}

package com.example.asm2.controllers;

import com.example.asm2.dtos.*;
import com.example.asm2.engine.Producer;
import com.example.asm2.pojos.BOMRequest;
import com.example.asm2.services.BOMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bom")
@CrossOrigin
public class BOMController {
    @Autowired
    private BOMService bomService;

    @Autowired
    private Producer kafkaProducer;

    @GetMapping("/")
    public ResponseEntity<Page<BOMDto>> findAll(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize){

        return new ResponseEntity<>(bomService.findAll(text, PageRequest.of(pageNo, pageSize, Sort.by("product"))).map(BOMDto::new),
                HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addBOM(@RequestBody BOMRequest bomRequest) {
        String KAFKA_TOPIC = "SAVE_BOM";
        kafkaProducer.sendSaveRequest(bomRequest, KAFKA_TOPIC);

//        bomService.saveBOM(bomDto);
        String message = "Added BOM for " + bomRequest.getProductId();
        return new ResponseEntity<>(new ResponseDto(true, message),
                HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Page<BOMDto>> getBOMByProductId(@PathVariable Long id) {
        return new ResponseEntity<>(bomService.findBOMByProductId(id, Pageable.unpaged()).map(BOMDto::new), HttpStatus.OK);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDto> updateBOM(@RequestBody BOMRequest bomRequest) {
        bomService.updateBOM(bomRequest);
        String message = "Updated BOM for " + bomRequest.getProductId();
        return new ResponseEntity<>(new ResponseDto(true, message),
                HttpStatus.CREATED);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<ResponseDto> updateBOMByProductId(@PathVariable Long id, @RequestBody List<BOMRequest> bomRequests) {
        bomService.updateBOMForProduct(id, bomRequests);
        String message = "Updated BOM for " + bomRequests.get(0).getProductId();
        return new ResponseEntity<>(new ResponseDto(true, message),
                HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseDto> deleteBOM(@RequestBody BOMRequest bomRequest) {
        bomService.deleteBOM(bomRequest);
        String message = "Deleted BOM for " + bomRequest.getProductId();
        return new ResponseEntity<>(new ResponseDto(true, message),
                HttpStatus.CREATED);
    }
}

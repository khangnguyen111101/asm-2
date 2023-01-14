package com.example.asm2.controllers;

import com.example.asm2.pojos.ItemRequest;
import com.example.asm2.dtos.ItemDto;
import com.example.asm2.dtos.ResponseDto;
import com.example.asm2.engine.Producer;
import com.example.asm2.models.Item;
import com.example.asm2.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/item")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Autowired
    private Producer kafkaProducer;

    @GetMapping("/")
    public ResponseEntity<Page<ItemDto>> findAll(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir){

        return new ResponseEntity<>(itemService.findAll(text, pageNo, pageSize, sortBy, sortDir).map(ItemDto::new),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> findOne(@PathVariable Long id) {
        Item result = itemService.findOne(id);
        return new ResponseEntity<>(new ItemDto(result), HttpStatus.OK);
    }

    @PostMapping ("/add")
    public ResponseEntity<ResponseDto> create(@RequestBody ItemRequest itemRequest) {
        String KAFKA_TOPIC = "SAVE_ITEM";
        kafkaProducer.sendSaveRequest(itemRequest, KAFKA_TOPIC);

//        Item item = itemService.saveItem(itemDto);
        return new ResponseEntity<>(new ResponseDto(true, "Item has been created successfully!"),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> update(@RequestBody ItemRequest itemRequest,
                                          @PathVariable Long id) {
        Item result = itemService.findOne(id);
        return new ResponseEntity<>(new ItemDto(
                itemService.updateItem(result, itemRequest)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> delete(@PathVariable Long id) {
        itemService.deleteItem(id);
        return new ResponseEntity<>(new ResponseDto(true, "Delete item successfully!"),
                HttpStatus.OK);
    }
}

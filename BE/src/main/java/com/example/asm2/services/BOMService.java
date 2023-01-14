package com.example.asm2.services;

import com.example.asm2.pojos.BOMRequest;
import com.example.asm2.models.BOM;
import com.example.asm2.models.BOMKey;
import com.example.asm2.models.Item;
import com.example.asm2.repository.BOMRepository;
import com.example.asm2.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BOMService {
    @Autowired
    private BOMRepository bomRepository;
    @Autowired
    private ItemRepository itemRepository;

    @Cacheable(value = "bom")
    public Page<BOM> findAll(String text, Pageable pageable) {
        return bomRepository.findAllByProductOrComponentLike(text, pageable);
    }
    @CacheEvict(value = "bom", allEntries=true)
    public void saveBOM(BOMRequest bomRequest) {
        BOMKey bomKey = new BOMKey(bomRequest.getProductId(), bomRequest.getComponentId());
        Optional<Item> product = itemRepository.findById(bomRequest.getProductId());
        Optional<Item> component = itemRepository.findById(bomRequest.getProductId());

        bomRepository.save(new BOM(bomKey, product.get(), component.get(), bomRequest.getUnit()));
    }

    @Cacheable(value = "bom", key = "#id", unless="#result==null")
    public Page<BOM> findBOMByProductId(Long id, Pageable pageable) {
        return bomRepository.findAllByProductId(id, pageable);
    }

    @CachePut(value = "bom", key = "#id")
    public void updateBOMForProduct(Long id, List<BOMRequest> bomRequests) {
        bomRepository.deleteByProductId(id);

        for (BOMRequest b : bomRequests) {
            this.saveBOM(b);
        }
    }

    @CacheEvict(value = "bom", allEntries=true)
    public void updateBOM(BOMRequest bomRequest) {
        if (bomRequest.getUnit() == 0) {
            bomRepository.deleteById(bomRequest.getProductId(), bomRequest.getComponentId());
        } else {
            saveBOM(bomRequest);
        }
    }

    @CacheEvict(value = "bom", allEntries=true)
    public void deleteBOM(BOMRequest bomRequest) {
        bomRepository.deleteById(bomRequest.getProductId(), bomRequest.getComponentId());
    }
}

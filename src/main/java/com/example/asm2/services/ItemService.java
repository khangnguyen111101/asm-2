package com.example.asm2.services;

import com.example.asm2.pojos.ItemRequest;
import com.example.asm2.models.BOM;
import com.example.asm2.models.Category;
import com.example.asm2.models.Item;
import com.example.asm2.repository.CategoryRepository;
import com.example.asm2.repository.ItemRepository;
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

import java.util.*;

@Service
public class ItemService {
    @Autowired private ItemRepository itemRepository;
    @Autowired private CategoryRepository categoryRepository;

    @Cacheable(value = "item")
    public Page<Item> findAll(String text, int pageNo, int pageSize, String sortBy, String sortDir) {
        System.out.println("All item cache");

        Pageable pageable = sortDir.equalsIgnoreCase("desc") ?
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending()):
                PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());

        if (!text.equals("")) {
            return itemRepository.findByNameOrDescription(text, pageable);
        } else {
            return itemRepository.findAll(pageable);
        }
    }
    @Cacheable(value = "item", key = "#id" , unless="#result==null")
    public Item findOne(Long id) {
        System.out.println("Item cache with key: " + id);
        return itemRepository.findById(id).orElse(null);
    }

    @CacheEvict(value = "item", allEntries=true)
    public void saveItem(ItemRequest itemRequest){
        Item item = new Item(itemRequest);

        Set<Category> categories = new HashSet<>();
        for (Long categoryId : itemRequest.getCategories()) {
            categories.add(categoryRepository.findById(categoryId)
                    .orElseGet(Category::new));
        }

        item.setCategories(categories);
        itemRepository.save(item);
    }

    @CachePut(value = "item", key = "#item.id")
    public Item updateItem(Item item, ItemRequest itemRequest){
        Set<Category> categoriesUpdate = new HashSet<>();
        for (Long categoryId: itemRequest.getCategories()) {
            categoriesUpdate.add(categoryRepository.findById(categoryId)
                    .orElseGet(Category::new));
        }

        item.setName(itemRequest.getName());
        item.setDescription(itemRequest.getDescription());
        item.setQuantity(itemRequest.getQuantity());
        item.setProduct(itemRequest.isProduct());
        item.setCategories(categoriesUpdate);

        return itemRepository.save(item);
    }

    @CacheEvict(value = "item", allEntries=true)
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional
    public void assembleProduct(Long productId, int quantity) {
        Optional<Item> productResult = itemRepository.findById(productId);
        if (productResult.isPresent()) {
            // Assemble
            Item product = productResult.get();
            int currentProductStock = product.getQuantity();
            product.setQuantity(currentProductStock + quantity);
            itemRepository.save(product);

            // Parse into its components
            Set<BOM> bomSet = product.getProductOf();

            for (BOM bom : bomSet) {
                Item component = bom.getComponent();
                int currentComponentStock = product.getQuantity();
                // Remove stock
                component.setQuantity(currentComponentStock - quantity * bom.getUnit());
                itemRepository.save(component);
            }
        }
    }
}

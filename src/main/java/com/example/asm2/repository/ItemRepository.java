package com.example.asm2.repository;

import com.example.asm2.models.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query ("SELECT i FROM Item i WHERE LOWER(i.name) LIKE %:text% OR LOWER(i.description) LIKE %:text%")
    Page<Item> findByNameOrDescription(@Param("text") String text, Pageable pageable);
}

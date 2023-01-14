package com.example.asm2.repository;

import com.example.asm2.models.BOM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BOMRepository extends JpaRepository<BOM, Long> {
    @Query ("SELECT b FROM BOM b WHERE LOWER(b.product.name) LIKE %:text% OR LOWER(b.component.name) LIKE %:text%")
    Page<BOM> findAllByProductOrComponentLike(@Param("text") String text, Pageable pageable);

    @Query("SELECT b FROM BOM b WHERE b.id.productId = :id")
    Page<BOM> findAllByProductId(@Param("id") Long id, Pageable pageable);

    @Transactional
    @Modifying
    @Query("DELETE FROM BOM b WHERE b.id.productId=:pid AND b.id.componentId=:cid")
    void deleteById(@Param("pid") Long pid, @Param("cid") Long cid);

    @Transactional
    @Modifying
    @Query("DELETE FROM BOM b WHERE b.id.productId=:pid")
    void deleteByProductId(@Param("pid") Long pid);
}

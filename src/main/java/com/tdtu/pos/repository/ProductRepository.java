package com.tdtu.pos.repository;

import com.tdtu.pos.entity.Product;
import com.tdtu.pos.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContaining(String keyword);
    List<Product> findByCategoryOrderByNameAsc(String category);
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();
}

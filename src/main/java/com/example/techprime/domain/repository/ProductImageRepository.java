package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.InvoiceAddress;
import com.example.techprime.domain.entities.Product;
import com.example.techprime.domain.entities.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    Optional<ProductImage> findById(int id);
    List<ProductImage> findByProduct(Product product);
}

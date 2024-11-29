package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByName(String name);
    Optional<Product> findById(int id);
    @Query("SELECT e FROM Product e WHERE e.deleted = FALSE OR e.deleted IS NULL")
    List<Product> findAllWhereActiveIsFalseOrNull();
}

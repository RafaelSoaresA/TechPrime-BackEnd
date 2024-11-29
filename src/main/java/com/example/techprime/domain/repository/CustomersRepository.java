package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.CustomerAddress;
import com.example.techprime.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomersRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUser(User user);
    Optional<Customer> findById(int id);
    Optional<Customer> findByDocument(String document);
}

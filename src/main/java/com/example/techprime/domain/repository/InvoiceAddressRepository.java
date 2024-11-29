package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.CustomerAddress;
import com.example.techprime.domain.entities.InvoiceAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceAddressRepository extends JpaRepository<InvoiceAddress, Integer> {
    Optional<InvoiceAddress> findById(int id);
}

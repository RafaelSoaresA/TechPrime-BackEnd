package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.Customer;
import com.example.techprime.domain.entities.TaxInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxInvoiceRepository extends JpaRepository<TaxInvoice, Integer> {
    Optional<TaxInvoice> findByid(Integer id);
    List<TaxInvoice> findByCustomer(Customer customer);
}

package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.TaxInvoice;
import com.example.techprime.domain.entities.TaxInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxInvoiceItemRepository extends JpaRepository<TaxInvoiceItem, Integer> {
    Optional<TaxInvoiceItem> findById(Integer id);
    List<TaxInvoiceItem> findByTaxInvoice(TaxInvoice taxInvoice);
}

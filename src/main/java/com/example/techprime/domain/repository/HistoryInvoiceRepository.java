package com.example.techprime.domain.repository;

import com.example.techprime.domain.entities.HistoryInvoice;
import com.example.techprime.domain.entities.TaxInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryInvoiceRepository extends JpaRepository<HistoryInvoice, Integer> {
    List<HistoryInvoice> findByTaxInvoice(TaxInvoice taxInvoice);
}

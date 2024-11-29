package com.example.techprime.web;

import com.example.techprime.domain.entities.HistoryInvoice;
import com.example.techprime.domain.entities.Product;
import com.example.techprime.domain.entities.TaxInvoice;
import com.example.techprime.domain.entities.TaxInvoiceItem;
import com.example.techprime.domain.service.TaxInvoiceService;
import com.example.techprime.dto.TaxInvoiceDto;
import com.example.techprime.dto.UpdateOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/shop")
public class TaxInvoiceController {
    private final TaxInvoiceService taxInvoiceService;

    @PostMapping("/buy")
    public ResponseEntity<TaxInvoice> createTaxInvoice (@RequestBody TaxInvoiceDto taxInvoiceDto) {
        return ResponseEntity.ok(taxInvoiceService.createTaxInvoice(taxInvoiceDto));
    }

    @GetMapping("/customerOrders")
    public ResponseEntity<List<TaxInvoice>> getCustomerOrders() {
        return ResponseEntity.ok(taxInvoiceService.getCustomerOrders());
    }

    @GetMapping("/allOrders")
    public ResponseEntity<List<TaxInvoice>> getAllOrders() {
        return ResponseEntity.ok(taxInvoiceService.getAllOrders());
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<TaxInvoice> getOrder(@PathVariable int id) {
        return ResponseEntity.ok(taxInvoiceService.getOrder(id));
    }

    @PostMapping("/order/update")
    public ResponseEntity<TaxInvoice> updateOrder(@RequestBody UpdateOrderDto updateOrderDto) {
        return ResponseEntity.ok(taxInvoiceService.orderUpdate(updateOrderDto));
    }

    @GetMapping("/order/{id}/history")
    public ResponseEntity<List<HistoryInvoice>> getOrderHistory(@PathVariable int id) {
        return ResponseEntity.ok(taxInvoiceService.getOrderHistory(id));
    }

    @GetMapping("/order/{id}/details")
    public ResponseEntity<List<TaxInvoiceItem>> getOrderDetails(@PathVariable int id) {
        return ResponseEntity.ok(taxInvoiceService.getOrderDetails(id));
    }

}

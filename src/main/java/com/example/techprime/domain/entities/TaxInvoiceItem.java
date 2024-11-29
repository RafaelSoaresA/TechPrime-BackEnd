package com.example.techprime.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

import static java.lang.String.valueOf;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity

@Table(name = "tax_invoice_item", schema = "dbo")
public class TaxInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "tax_invoice_id", nullable = false)
    private TaxInvoice taxInvoice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private double productPrice;

    @Column(name = "quantity")
    private int quantity;



    public TaxInvoiceItem(Product product) {
        this.productName = product.getName();
        this.productPrice = product.getPrice();
        this.quantity = product.getQuantity();
    }
}

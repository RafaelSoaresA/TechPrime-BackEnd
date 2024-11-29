package com.example.techprime.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity

@Table(name = "tax_invoice", schema = "dbo")
public class TaxInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "document")
    private String document;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "total_cost")
    private double totalCost;

    @Column(name = "shipping_cost")
    private double shippingCost;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "invoice_status")
    private String invoiceStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();


    @Column(name = "issue_date")
    private LocalDateTime issueDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "billing_address_id", nullable = false)
    private InvoiceAddress billingAddress;

    @ManyToOne
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private InvoiceAddress deliveryAddress;

    @Column(name = "payment_type")
    private String paymentType;

    @ManyToOne
    @JoinColumn(name = "token_credit_card", nullable = false)
    private CreditCard creditCard;

    @Column(name = "installments_number")
    private int installmentsNumber;

    @Column(name = "shipping_type")
    private String shippingType;

}

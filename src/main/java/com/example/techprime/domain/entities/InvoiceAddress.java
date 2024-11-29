package com.example.techprime.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity

@Table(name = "invoice_address", schema = "dbo")
public class InvoiceAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "customer_address_type")
    private String customerAddressType;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address_type")
    private String addressType;

    @Column(name = "address")
    private String address;

    @Column(name = "number")
    private String number;

    @Column(name = "complement")
    private String complement;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;
}

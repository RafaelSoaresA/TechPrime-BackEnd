package com.example.techprime.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity

@Table(name = "credit_card", schema = "dbo")
public class CreditCard {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "expire_date")
    private String expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

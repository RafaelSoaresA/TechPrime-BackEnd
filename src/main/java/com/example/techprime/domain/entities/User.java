package com.example.techprime.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

@Table(name = "users", schema = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "credit")
    private double credit;

    @Column(name = "role")
    private String role;

    @Column(name = "deleted_at", columnDefinition = "Bit")
    private Boolean deleted;

    public User(String s) {
    }

    public User(String mail, String newuser, String newpassword, String user) {
    }
}
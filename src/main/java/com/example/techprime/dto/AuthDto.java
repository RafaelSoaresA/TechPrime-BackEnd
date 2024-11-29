package com.example.techprime.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AuthDto {

    private String email;
    private String password;
    private long id;
    private String role;
    private String token;

    public AuthDto(String email, String password, long id, String role, String token) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.role = role;
        this.token = token;
    }

    public AuthDto() {}
}

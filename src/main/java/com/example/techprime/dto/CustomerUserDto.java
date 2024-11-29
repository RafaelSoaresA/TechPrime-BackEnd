package com.example.techprime.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUserDto {

    private String email;
    private String name;
    private String password;
}

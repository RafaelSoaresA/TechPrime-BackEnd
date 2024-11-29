package com.example.techprime.dto;


import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;
    private String name;
    private Integer id;
    private String role;
    private double credit;
    private Boolean status;
    private String password;
    private CustomerDto customerDto;
}

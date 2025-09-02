package com.example.Restapis_ProjectPractice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String email;
    private int age;
    private String phoneNumber;
    private String status;
}

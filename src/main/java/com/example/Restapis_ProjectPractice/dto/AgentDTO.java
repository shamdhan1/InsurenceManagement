package com.example.Restapis_ProjectPractice.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDTO {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private Double commissionRate;

}

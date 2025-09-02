package com.example.Restapis_ProjectPractice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String claimNumber;

    private String status; // pending, approved, rejected

    private Double claimAmount;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;


}

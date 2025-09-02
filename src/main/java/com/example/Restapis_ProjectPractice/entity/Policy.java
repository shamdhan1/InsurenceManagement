package com.example.Restapis_ProjectPractice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNumber;

    private String type; // e.g., health, life, motor

    private String status; // active, expired, cancelled

    private Double sumAssured;

    private LocalDate startDate;

    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer; // Linked with Customer Entity

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    private List<Endorsement> endorsements;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    private List<Claim> claims;



}

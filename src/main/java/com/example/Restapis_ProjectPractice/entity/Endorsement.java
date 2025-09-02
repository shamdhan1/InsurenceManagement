package com.example.Restapis_ProjectPractice.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endorsements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String details; // Example: "Nominee updated", "Address updated"

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private Policy policy;



}

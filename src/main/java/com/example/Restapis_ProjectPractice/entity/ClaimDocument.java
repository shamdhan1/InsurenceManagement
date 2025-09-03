package com.example.Restapis_ProjectPractice.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id")
    private Claim claim;

    @Column(nullable=false)
    private String fileName;

    @Column(nullable=false)
    private String contentType;

    @Column(nullable=false)
    private String storagePath; // store file path (or S3 key)

    @Column(nullable=false, updatable=false)
    private LocalDateTime uploadedAt;

    @PrePersist
    void prePersist() { uploadedAt = LocalDateTime.now();


    }


}

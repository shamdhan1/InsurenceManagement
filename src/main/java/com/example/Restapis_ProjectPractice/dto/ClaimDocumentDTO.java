package com.example.Restapis_ProjectPractice.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimDocumentDTO {
    private Long id;
    private Long claimId;
    private String fileName;
    private String contentType;
    private String storagePath;
    private LocalDateTime uploadedAt;


}

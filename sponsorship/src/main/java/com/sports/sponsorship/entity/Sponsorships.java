package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sponsorships")
@Data
public class Sponsorships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @NotNull
    @DecimalMin(value = "0.0", message = "Contribution amount must be non-negative")
    @Column(name = "contribution_amount")
    private Double contributionAmount;

    @NotBlank
    @Column(columnDefinition = "VARCHAR(50) CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED'))")
    private String status = "PENDING";

    @NotNull
    @Column(name = "request_date")
    private LocalDateTime requestDate = LocalDateTime.now();

    @Column(name = "decision_date")
    private LocalDateTime decisionDate;

    private String notes;
}
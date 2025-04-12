package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sponsorships")
public class Sponsorships {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sponsor_id", nullable = false)
    private Sponsor sponsor;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name = "contribution_amount", nullable = false)
    private BigDecimal contributionAmount;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "request_date", nullable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    // Getters, Setters, Constructors
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Sponsor getSponsor() { return sponsor; }
    public void setSponsor(Sponsor sponsor) { this.sponsor = sponsor; }
    public Event getEvent() { return event; }
    public void setEvent(Event event) { this.event = event; }
    public BigDecimal getContributionAmount() { return contributionAmount; }
    public void setContributionAmount(BigDecimal contributionAmount) { this.contributionAmount = contributionAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
}
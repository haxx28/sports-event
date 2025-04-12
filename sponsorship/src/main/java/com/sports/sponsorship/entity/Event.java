package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "event_head")
    private String eventHead;

    private String description;

    @Column(nullable = false)
    private String venue;

    @Column(name = "sponsor_id")
    private Long sponsorId;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Sponsorships> sponsorships = new ArrayList<>();

    // Getters, Setters, Constructors
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getEventHead() { return eventHead; }
    public void setEventHead(String eventHead) { this.eventHead = eventHead; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }
    public Long getSponsorId() { return sponsorId; }
    public void setSponsorId(Long sponsorId) { this.sponsorId = sponsorId; }
    public List<Sponsorships> getSponsorships() { return sponsorships; }
    public void setSponsorships(List<Sponsorships> sponsorships) { this.sponsorships = sponsorships; }
}
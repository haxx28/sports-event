package com.sports.sponsorship.controller;

import com.sports.sponsorship.entity.Event;
import com.sports.sponsorship.entity.Sponsor;
import com.sports.sponsorship.entity.Sponsorships;
import com.sports.sponsorship.service.SponsorshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/sponsorship")
public class SponsorshipController {
    private final SponsorshipService sponsorshipService;

    @Autowired
    public SponsorshipController(SponsorshipService sponsorshipService) {
        this.sponsorshipService = sponsorshipService;
    }

    // Register a sponsor
    @PostMapping("/register")
    public ResponseEntity<Sponsor> registerSponsor(@RequestBody SponsorRequest request) {
        Sponsor sponsor = sponsorshipService.registerSponsor(request.getName(),
                request.getCompanyName(),
                request.getEmail(),
                request.getPhone());
        return ResponseEntity.ok(sponsor);
    }

    // Sponsor requests to sponsor an event
    @PostMapping("/request")
    public ResponseEntity<Sponsorships> requestSponsorship(@RequestBody SponsorshipRequest request) {
        Sponsorships sponsorship = sponsorshipService.requestSponsorship(request.getSponsorId(),
                request.getEventId(),
                request.getContributionAmount());
        return ResponseEntity.ok(sponsorship);
    }

    // Event master accepts a sponsorship request
    @PostMapping("/accept/{sponsorshipId}")
    public ResponseEntity<Sponsorships> acceptSponsorship(@PathVariable Long sponsorshipId) {
        Sponsorships sponsorship = sponsorshipService.acceptSponsorship(sponsorshipId);
        return ResponseEntity.ok(sponsorship);
    }

    // Get sponsorship status for a sponsor
    @GetMapping("/sponsor/{sponsorId}")
    public ResponseEntity<List<Sponsorships>> getSponsorshipsBySponsor(@PathVariable Long sponsorId) {
        List<Sponsorships> sponsorships = sponsorshipService.getSponsorshipsBySponsor(sponsorId);
        return ResponseEntity.ok(sponsorships);
    }

    // Get all sponsorship requests for an event
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Sponsorships>> getSponsorshipsByEvent(@PathVariable Long eventId) {
        List<Sponsorships> sponsorships = sponsorshipService.getSponsorshipsByEvent(eventId);
        return ResponseEntity.ok(sponsorships);
    }
}

// DTOs for request bodies
class SponsorRequest {
    private String name;
    private String companyName;
    private String email;
    private String phone;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}

class SponsorshipRequest {
    private Long sponsorId;
    private Long eventId;
    private BigDecimal contributionAmount;

    public Long getSponsorId() { return sponsorId; }
    public void setSponsorId(Long sponsorId) { this.sponsorId = sponsorId; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public BigDecimal getContributionAmount() { return contributionAmount; }
    public void setContributionAmount(BigDecimal contributionAmount) { this.contributionAmount = contributionAmount; }
}
package com.sports.sponsorship.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sports.sponsorship.entity.Event;
import com.sports.sponsorship.entity.Sponsor;
import com.sports.sponsorship.entity.Sponsorships;
import com.sports.sponsorship.service.SponsorshipService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/sponsorship")
public class SponsorshipController {
    private final SponsorshipService sponsorshipService;

    public SponsorshipController(SponsorshipService sponsorshipService) {
        this.sponsorshipService = sponsorshipService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new sponsor")
    public ResponseEntity<Sponsor> registerSponsor(@Valid @RequestBody SponsorRegisterRequest request) {
        Sponsor sponsor = new Sponsor();
        sponsor.setName(request.getName());
        sponsor.setCompanyName(request.getCompanyName());
        sponsor.setEmail(request.getEmail());
        sponsor.setPhone(request.getPhone());
        sponsor.setIndustry(request.getIndustry());
        return ResponseEntity
                .ok(sponsorshipService.registerSponsor(sponsor, request.getUsername(), request.getPassword()));
    }

    @PutMapping("/update")
    @Operation(summary = "Update sponsor details")
    public ResponseEntity<Sponsor> updateSponsor(@Valid @RequestBody SponsorRequest request) {
        Sponsor sponsor = new Sponsor();
        sponsor.setName(request.getName());
        sponsor.setCompanyName(request.getCompanyName());
        sponsor.setEmail(request.getEmail());
        sponsor.setPhone(request.getPhone());
        sponsor.setIndustry(request.getIndustry());
        return ResponseEntity.ok(sponsorshipService.updateSponsor(sponsor));
    }

    @PostMapping("/deactivate")
    @Operation(summary = "Deactivate a sponsor account")
    public ResponseEntity<String> deactivateSponsor() {
        sponsorshipService.deactivateSponsor();
        return ResponseEntity.ok("Sponsor deactivated successfully");
    }

    @PostMapping("/reactivate")
    @Operation(summary = "Reactivate a sponsor account")
    public ResponseEntity<String> reactivateSponsor() {
        sponsorshipService.reactivateSponsor();
        return ResponseEntity.ok("Sponsor reactivated successfully");
    }

    @GetMapping("/events")
    @Operation(summary = "Get available events for sponsorship")
    public ResponseEntity<List<Event>> getAvailableEvents() {
        return ResponseEntity.ok(sponsorshipService.getAvailableEvents());
    }

    @PostMapping("/request")
    @Operation(summary = "Request sponsorship for an event")
    public ResponseEntity<Sponsorships> requestSponsorship(@Valid @RequestBody SponsorshipRequest request) {
        Sponsorships sponsorship = sponsorshipService.requestSponsorship(
                request.getEventId(), request.getContributionAmount());
        return ResponseEntity.ok(sponsorship);
    }

    @PostMapping("/accept/{sponsorshipId}")
    @Operation(summary = "Accept a sponsorship request")
    public ResponseEntity<Sponsorships> acceptSponsorship(@PathVariable Long sponsorshipId) {
        return ResponseEntity.ok(sponsorshipService.acceptSponsorship(sponsorshipId));
    }

    @PostMapping("/reject/{sponsorshipId}")
    @Operation(summary = "Reject a sponsorship request")
    public ResponseEntity<Sponsorships> rejectSponsorship(
            @PathVariable Long sponsorshipId,
            @RequestBody RejectionRequest request) {
        return ResponseEntity.ok(sponsorshipService.rejectSponsorship(sponsorshipId, request.getNotes()));
    }

    @GetMapping("/sponsor")
    @Operation(summary = "Get sponsorships for authenticated sponsor")
    public ResponseEntity<List<Sponsorships>> getSponsorshipsBySponsor() {
        return ResponseEntity.ok(sponsorshipService.getSponsorshipsBySponsor());
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Get sponsorships by event ID")
    public ResponseEntity<List<Sponsorships>> getSponsorshipsByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(sponsorshipService.getSponsorshipsByEvent(eventId));
    }
}

// DTOs
class SponsorRegisterRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String companyName;
    @NotBlank
    @jakarta.validation.constraints.Email
    private String email;
    private String phone;
    private String industry;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class SponsorRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String companyName;
    @NotBlank
    @jakarta.validation.constraints.Email
    private String email;
    private String phone;
    private String industry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}

class SponsorshipRequest {
    private Long eventId;
    private Double contributionAmount;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Double getContributionAmount() {
        return contributionAmount;
    }

    public void setContributionAmount(Double contributionAmount) {
        this.contributionAmount = contributionAmount;
    }
}

class RejectionRequest {
    @NotBlank
    private String notes;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
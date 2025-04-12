package com.sports.sponsorship.controller;

import com.sports.sponsorship.entity.Sponsor;
import com.sports.sponsorship.service.SponsorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sponsors")
@CrossOrigin(origins = "http://localhost:4200")
public class SponsorController {

    private final SponsorService sponsorService;

    @Autowired
    public SponsorController(SponsorService sponsorService) {
        this.sponsorService = sponsorService;
    }

    @PostMapping("/register")
    public Sponsor registerSponsor(@RequestBody Sponsor sponsor) {
        return sponsorService.registerSponsor(sponsor);
    }

    @GetMapping
    public List<Sponsor> getAllSponsors() {
        return sponsorService.getAllSponsors();
    }
}

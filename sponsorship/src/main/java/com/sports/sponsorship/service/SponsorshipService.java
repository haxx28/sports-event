package com.sports.sponsorship.service;

import com.sports.sponsorship.entity.Event;
import com.sports.sponsorship.entity.Sponsor;
import com.sports.sponsorship.entity.Sponsorships;
import com.sports.sponsorship.repository.EventRepository;
import com.sports.sponsorship.repository.SponsorRepository;
import com.sports.sponsorship.repository.SponsorshipsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SponsorshipService {
    private final SponsorRepository sponsorRepository;
    private final EventRepository eventRepository;
    private final SponsorshipsRepository sponsorshipsRepository;

    @Autowired
    public SponsorshipService(SponsorRepository sponsorRepository,
                              EventRepository eventRepository,
                              SponsorshipsRepository sponsorshipsRepository) {
        this.sponsorRepository = sponsorRepository;
        this.eventRepository = eventRepository;
        this.sponsorshipsRepository = sponsorshipsRepository;
    }

    // Register a sponsor
    public Sponsor registerSponsor(String name, String companyName, String email, String phone) {
        Sponsor sponsor = new Sponsor();
        sponsor.setName(name);
        sponsor.setCompanyName(companyName);
        sponsor.setEmail(email);
        sponsor.setPhone(phone);
        return sponsorRepository.save(sponsor);
    }

    // Sponsor requests to sponsor an event
    public Sponsorships requestSponsorship(Long sponsorId, Long eventId, BigDecimal contributionAmount) {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(() -> new IllegalArgumentException("Sponsor not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Sponsorships existingRequest = sponsorshipsRepository.findBySponsorIdAndEventId(sponsorId, eventId);
        if (existingRequest != null) {
            throw new IllegalStateException("Sponsorship request already exists for this sponsor and event");
        }

        Sponsorships sponsorship = new Sponsorships();
        sponsorship.setSponsor(sponsor);
        sponsorship.setEvent(event);
        sponsorship.setContributionAmount(contributionAmount);
        sponsorship.setStatus("PENDING");
        return sponsorshipsRepository.save(sponsorship);
    }

    // Event master accepts a sponsorship request
    @Transactional
    public Sponsorships acceptSponsorship(Long sponsorshipId) {
        Sponsorships sponsorship = sponsorshipsRepository.findById(sponsorshipId)
                .orElseThrow(() -> new IllegalArgumentException("Sponsorship request not found"));

        if (!"PENDING".equals(sponsorship.getStatus())) {
            throw new IllegalStateException("Only pending requests can be accepted");
        }

        Event event = sponsorship.getEvent();
        if (event.getSponsorId() != null) {
            throw new IllegalStateException("Event already has an accepted sponsor");
        }

        // Accept the request
        sponsorship.setStatus("ACCEPTED");
        event.setSponsorId(sponsorship.getSponsor().getId());
        eventRepository.save(event);

        // Reject all other pending requests for this event
        List<Sponsorships> otherRequests = sponsorshipsRepository.findByEventId(event.getId());
        for (Sponsorships other : otherRequests) {
            if (!other.getId().equals(sponsorshipId) && "PENDING".equals(other.getStatus())) {
                other.setStatus("REJECTED");
                sponsorshipsRepository.save(other);
            }
        }

        return sponsorshipsRepository.save(sponsorship);
    }

    // Get all sponsorship requests for a sponsor (for sponsor page)
    public List<Sponsorships> getSponsorshipsBySponsor(Long sponsorId) {
        return sponsorshipsRepository.findBySponsorId(sponsorId);
    }

    // Get all sponsorship requests for an event (for event master)
    public List<Sponsorships> getSponsorshipsByEvent(Long eventId) {
        return sponsorshipsRepository.findByEventId(eventId);
    }
}
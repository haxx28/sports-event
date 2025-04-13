package com.sports.sponsorship.service;

import com.sports.sponsorship.entity.Event;
import com.sports.sponsorship.entity.Sponsor;
import com.sports.sponsorship.entity.Sponsorships;
import com.sports.sponsorship.entity.User;
import com.sports.sponsorship.repository.EventRepository;
import com.sports.sponsorship.repository.SponsorRepository;
import com.sports.sponsorship.repository.SponsorshipsRepository;
import com.sports.sponsorship.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Validated
public class SponsorshipService {
    private static final Logger logger = LoggerFactory.getLogger(SponsorshipService.class);
    private final SponsorRepository sponsorRepository;
    private final EventRepository eventRepository;
    private final SponsorshipsRepository sponsorshipsRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public SponsorshipService(SponsorRepository sponsorRepository,
                              EventRepository eventRepository,
                              SponsorshipsRepository sponsorshipsRepository,
                              UserRepository userRepository,
                              AuthService authService) {
        this.sponsorRepository = sponsorRepository;
        this.eventRepository = eventRepository;
        this.sponsorshipsRepository = sponsorshipsRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Transactional
    public Sponsor registerSponsor(@Valid Sponsor sponsor, String username, String password) {
        if (sponsorRepository.existsByEmail(sponsor.getEmail())) {
            logger.warn("Attempt to register duplicate email: {}", sponsor.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }
        sponsor.setStatus("ACTIVE");
        Sponsor savedSponsor = sponsorRepository.save(sponsor);
        authService.createUser(username, password, "SPONSOR", savedSponsor.getId());
        logger.info("Registered sponsor: {}", sponsor.getCompanyName());
        return savedSponsor;
    }

    @Transactional
    public Sponsor updateSponsor(@Valid Sponsor updatedSponsor) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Sponsor sponsor = sponsorRepository.findById(user.getSponsorId())
                .orElseThrow(() -> {
                    logger.error("Sponsor not found: {}", user.getSponsorId());
                    return new IllegalArgumentException("Sponsor not found");
                });

        if (!sponsor.getEmail().equals(updatedSponsor.getEmail()) &&
                sponsorRepository.existsByEmail(updatedSponsor.getEmail())) {
            logger.warn("Attempt to update to duplicate email: {}", updatedSponsor.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        sponsor.setName(updatedSponsor.getName());
        sponsor.setCompanyName(updatedSponsor.getCompanyName());
        sponsor.setEmail(updatedSponsor.getEmail());
        sponsor.setPhone(updatedSponsor.getPhone());
        sponsor.setIndustry(updatedSponsor.getIndustry());
        logger.info("Updated sponsor: {}", sponsor.getCompanyName());
        return sponsorRepository.save(sponsor);
    }

    @Transactional
    public void deactivateSponsor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Sponsor sponsor = sponsorRepository.findById(user.getSponsorId())
                .orElseThrow(() -> {
                    logger.error("Sponsor not found: {}", user.getSponsorId());
                    return new IllegalArgumentException("Sponsor not found");
                });
        sponsor.setStatus("INACTIVE");
        logger.info("Deactivated sponsor: {}", sponsor.getCompanyName());
        sponsorRepository.save(sponsor);
    }

    @Transactional
    public void reactivateSponsor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Sponsor sponsor = sponsorRepository.findById(user.getSponsorId())
                .orElseThrow(() -> {
                    logger.error("Sponsor not found: {}", user.getSponsorId());
                    return new IllegalArgumentException("Sponsor not found");
                });
        if ("ACTIVE".equals(sponsor.getStatus())) {
            logger.warn("Sponsor already active: {}", sponsor.getId());
            throw new IllegalStateException("Sponsor account is already active");
        }
        sponsor.setStatus("ACTIVE");
        logger.info("Reactivated sponsor: {}", sponsor.getCompanyName());
        sponsorRepository.save(sponsor);
    }

    public List<Event> getAvailableEvents() {
        logger.info("Fetching all events");
        return eventRepository.findAllByOrderByDateAsc();
    }

    public Sponsorships requestSponsorship(Long eventId, Double contributionAmount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Sponsor sponsor = sponsorRepository.findById(user.getSponsorId())
                .orElseThrow(() -> {
                    logger.error("Sponsor not found: {}", user.getSponsorId());
                    return new IllegalArgumentException("Sponsor not found");
                });

        if (!"ACTIVE".equals(sponsor.getStatus())) {
            logger.warn("Inactive sponsor attempted request: {}", sponsor.getId());
            throw new IllegalStateException("Sponsor account is inactive");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    logger.error("Event not found: {}", eventId);
                    return new IllegalArgumentException("Event not found");
                });

        if (sponsorshipsRepository.existsBySponsorIdAndEventId(sponsor.getId(), eventId)) {
            logger.warn("Duplicate sponsorship request: sponsorId={}, eventId={}", sponsor.getId(), eventId);
            throw new IllegalStateException("Sponsorship request already exists");
        }

        if (contributionAmount <= 0) {
            logger.error("Invalid contribution amount: {}", contributionAmount);
            throw new IllegalArgumentException("Contribution amount must be positive");
        }

        Sponsorships sponsorship = new Sponsorships();
        sponsorship.setSponsor(sponsor);
        sponsorship.setEvent(event);
        sponsorship.setContributionAmount(contributionAmount);
        sponsorship.setStatus("PENDING");

        logger.info("Creating sponsorship request: sponsorId={}, eventId={}", sponsor.getId(), eventId);
        sponsorshipsRepository.save(sponsorship);
        notifySponsor(sponsor, "Sponsorship request submitted for " + event.getName());
        return sponsorship;
    }

    @Transactional
    public Sponsorships acceptSponsorship(Long sponsorshipId) {
        Sponsorships sponsorship = sponsorshipsRepository.findById(sponsorshipId)
                .orElseThrow(() -> {
                    logger.error("Sponsorship not found: {}", sponsorshipId);
                    return new IllegalArgumentException("Sponsorship request not found");
                });

        if (!"PENDING".equals(sponsorship.getStatus())) {
            logger.warn("Attempt to accept non-pending sponsorship: {}", sponsorshipId);
            throw new IllegalStateException("Only pending requests can be accepted");
        }

        Event event = sponsorship.getEvent();
        if (event.getSponsorId() != null) {
            logger.warn("Event already sponsored: eventId={}", event.getId());
            throw new IllegalStateException("Event already has an accepted sponsor");
        }

        sponsorship.setStatus("ACCEPTED");
        sponsorship.setDecisionDate(LocalDateTime.now());
        event.setSponsorId(sponsorship.getSponsor().getId());
        eventRepository.save(event);

        List<Sponsorships> otherRequests = sponsorshipsRepository.findByEventId(event.getId());
        for (Sponsorships other : otherRequests) {
            if (!other.getId().equals(sponsorshipId) && "PENDING".equals(other.getStatus())) {
                other.setStatus("REJECTED");
                other.setDecisionDate(LocalDateTime.now());
                other.setNotes("Rejected due to another sponsor being accepted");
                sponsorshipsRepository.save(other);
                notifySponsor(other.getSponsor(), "Sponsorship request for " + event.getName() + " was rejected");
            }
        }

        logger.info("Accepted sponsorship: sponsorshipId={}", sponsorshipId);
        sponsorshipsRepository.save(sponsorship);
        notifySponsor(sponsorship.getSponsor(), "Sponsorship request for " + event.getName() + " was accepted");
        return sponsorship;
    }

    @Transactional
    public Sponsorships rejectSponsorship(Long sponsorshipId, String notes) {
        Sponsorships sponsorship = sponsorshipsRepository.findById(sponsorshipId)
                .orElseThrow(() -> {
                    logger.error("Sponsorship not found: {}", sponsorshipId);
                    return new IllegalArgumentException("Sponsorship request not found");
                });

        if (!"PENDING".equals(sponsorship.getStatus())) {
            logger.warn("Attempt to reject non-pending sponsorship: {}", sponsorshipId);
            throw new IllegalStateException("Only pending requests can be rejected");
        }

        sponsorship.setStatus("REJECTED");
        sponsorship.setDecisionDate(LocalDateTime.now());
        sponsorship.setNotes(notes);
        logger.info("Rejected sponsorship: sponsorshipId={}", sponsorshipId);
        sponsorshipsRepository.save(sponsorship);
        notifySponsor(sponsorship.getSponsor(), "Sponsorship request for " + sponsorship.getEvent().getName() + " was rejected: " + notes);
        return sponsorship;
    }

    public List<Sponsorships> getSponsorshipsBySponsor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Long sponsorId = user.getSponsorId();
        if (sponsorId == null) {
            throw new IllegalArgumentException("User is not associated with a sponsor");
        }
        return sponsorshipsRepository.findBySponsorId(sponsorId);
    }

    public List<Sponsorships> getSponsorshipsByEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            logger.error("Event not found: {}", eventId);
            throw new IllegalArgumentException("Event not found");
        }
        return sponsorshipsRepository.findByEventId(eventId);
    }

    private void notifySponsor(Sponsor sponsor, String message) {
        logger.info("Notification to {} ({}): {}", sponsor.getEmail(), sponsor.getCompanyName(), message);
        // TODO: Integrate with email service
    }
}
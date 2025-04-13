package com.sports.sponsorship.repository;

import com.sports.sponsorship.entity.Sponsorships;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SponsorshipsRepository extends JpaRepository<Sponsorships, Long> {
    List<Sponsorships> findBySponsorId(Long sponsorId);
    List<Sponsorships> findByEventId(Long eventId);
    boolean existsBySponsorIdAndEventId(Long sponsorId, Long eventId);
}
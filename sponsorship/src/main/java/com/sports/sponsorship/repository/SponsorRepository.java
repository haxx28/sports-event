package com.sports.sponsorship.repository;


import com.sports.sponsorship.entity.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    boolean existsByEmail(String email);
}
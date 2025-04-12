package com.sports.sponsorship.repository;



import com.sports.sponsorship.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
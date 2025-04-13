package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @Size(max = 255, message = "Event head must be less than 255 characters")
    @Column(name = "event_head")
    private String eventHead;

    private String description;

    @NotBlank(message = "Venue is required")
    @Size(max = 255, message = "Venue must be less than 255 characters")
    private String venue;

    @Column(name = "sponsor_id")
    private Long sponsorId;
}
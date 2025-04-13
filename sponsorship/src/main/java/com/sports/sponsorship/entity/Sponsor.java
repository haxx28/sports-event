package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sponsor")
@Data
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must be less than 255 characters")
    @Column(name = "company_name")
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true)
    private String email;

    @Size(max = 20, message = "Phone must be less than 20 characters")
    private String phone;

    @Size(max = 100, message = "Industry must be less than 100 characters")
    private String industry;

    @NotNull
    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();

    @NotBlank
    @Column(columnDefinition = "VARCHAR(50) CHECK (status IN ('ACTIVE', 'INACTIVE'))")
    private String status = "ACTIVE";
}
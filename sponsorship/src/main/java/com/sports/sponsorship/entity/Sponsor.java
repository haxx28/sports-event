package com.sports.sponsorship.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sponsor")
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    @OneToMany(mappedBy = "sponsor", cascade = CascadeType.ALL)
    private List<Sponsorships> sponsorships = new ArrayList<>();

    // Getters, Setters, Constructors
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public List<Sponsorships> getSponsorships() { return sponsorships; }
    public void setSponsorships(List<Sponsorships> sponsorships) { this.sponsorships = sponsorships; }
}
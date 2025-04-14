import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { SponsorService } from '../../../core/services/sponsor.service';
import { EventService } from '../../../core/services/event.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatIconModule } from '@angular/material/icon';
import { Sponsor } from '../../../core/models/sponsor';
import { Event } from '../../../core/models/event';
import { Sponsorship } from '../../../core/models/sponsorship';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../shared/material.module';

@Component({
  selector: 'app-sponsor-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule, CommonModule, MatIconModule],
})
export class SponsorDashboardComponent implements OnInit {
  updateForm: FormGroup;
  requestForm: FormGroup;
  events: Event[] = [];
  sponsorships: Sponsorship[] = [];
  eventColumns: string[] = ['name', 'date'];
  sponsorshipColumns: string[] = ['eventName', 'amount', 'status', 'requestDate'];
  activeSection: string = 'dashboard'; // Tracks active sidebar section
  isSidebarCollapsed: boolean = false; // Tracks sidebar state
  totalContribution: number = 0; // For Dashboard overview

  constructor(
    private fb: FormBuilder,
    private sponsorService: SponsorService,
    private eventService: EventService,
    private snackBar: MatSnackBar
  ) {
    this.updateForm = this.fb.group({
      name: ['', Validators.required],
      companyName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: [''],
      industry: [''],
    });
    this.requestForm = this.fb.group({
      eventId: ['', Validators.required],
      contributionAmount: ['', [Validators.required, Validators.min(1)]],
    });
  }

  ngOnInit(): void {
    this.loadEvents();
    this.loadSponsorships();
  }

  loadEvents(): void {
    this.eventService.getAvailableEvents().subscribe({
      next: (events) => {
        this.events = events;
      },
      error: () => this.snackBar.open('Failed to load events', 'Close', { duration: 3000 }),
    });
  }

  loadSponsorships(): void {
    this.sponsorService.getSponsorships().subscribe({
      next: (sponsorships) => {
        this.sponsorships = sponsorships;
        this.totalContribution = sponsorships.reduce(
          (sum, s) => sum + (s.contributionAmount || 0),
          0
        );
      },
      error: () => this.snackBar.open('Failed to load sponsorships', 'Close', { duration: 3000 }),
    });
  }

  onUpdate(): void {
    if (this.updateForm.valid) {
      this.sponsorService.update(this.updateForm.value).subscribe({
        next: () => this.snackBar.open('Profile updated', 'Close', { duration: 3000 }),
        error: (err) =>
          this.snackBar.open(err.error || 'Update failed', 'Close', { duration: 3000 }),
      });
    }
  }

  onDeactivate(): void {
    this.sponsorService.deactivate().subscribe({
      next: () => {
        this.snackBar.open('Account deactivated', 'Close', { duration: 3000 });
        this.loadSponsorships();
      },
      error: () => this.snackBar.open('Deactivation failed', 'Close', { duration: 3000 }),
    });
  }

  onReactivate(): void {
    this.sponsorService.reactivate().subscribe({
      next: () => {
        this.snackBar.open('Account reactivated', 'Close', { duration: 3000 });
        this.loadSponsorships();
      },
      error: () => this.snackBar.open('Reactivation failed', 'Close', { duration: 3000 }),
    });
  }

  onRequest(): void {
    if (this.requestForm.valid) {
      const { eventId, contributionAmount } = this.requestForm.value;
      this.sponsorService.requestSponsorship(eventId, contributionAmount).subscribe({
        next: () => {
          this.snackBar.open('Sponsorship requested', 'Close', { duration: 3000 });
          this.requestForm.reset();
          this.loadSponsorships();
        },
        error: (err) =>
          this.snackBar.open(err.error || 'Request failed', 'Close', { duration: 3000 }),
      });
    }
  }

  onLogout(): void {
    // Placeholder: Implement logout logic (e.g., clear auth token, redirect to login)
    this.snackBar.open('Logged out successfully', 'Close', { duration: 3000 });
    // Example: this.authService.logout().subscribe(() => this.router.navigate(['/login']));
  }

  setActiveSection(section: string): void {
    this.activeSection = section;
  }

  toggleSidebar(): void {
    this.isSidebarCollapsed = !this.isSidebarCollapsed;
  }
}
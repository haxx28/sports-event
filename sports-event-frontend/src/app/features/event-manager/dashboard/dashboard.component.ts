import { Component, OnInit } from '@angular/core';
import { EventService } from '../../../core/services/event.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Event } from '../../../core/models/event';
import { Sponsorship } from '../../../core/models/sponsorship';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModule } from '../../../shared/material.module';

@Component({
  selector: 'app-event-manager-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule, CommonModule]
})
export class EventManagerDashboardComponent implements OnInit {
  events: Event[] = [];
  selectedEvent: Event | null = null;
  sponsorships: Sponsorship[] = [];
  displayedColumns: string[] = ['sponsor', 'amount', 'status', 'requestDate', 'actions'];

  constructor(
    private eventService: EventService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  loadEvents(): void {
    this.eventService.getAvailableEvents().subscribe({
      next: (events) => this.events = events,
      error: () => this.snackBar.open('Failed to load events', 'Close', { duration: 3000 })
    });
  }

  selectEvent(eventId: number): void {
    this.selectedEvent = this.events.find(e => e.id === eventId) || null;
    if (this.selectedEvent) {
      this.eventService.getSponsorshipsByEvent(eventId).subscribe({
        next: (sponsorships) => this.sponsorships = sponsorships,
        error: () => this.snackBar.open('Failed to load sponsorships', 'Close', { duration: 3000 })
      });
    }
  }

  acceptSponsorship(sponsorshipId: number): void {
    this.eventService.acceptSponsorship(sponsorshipId).subscribe({
      next: () => {
        this.snackBar.open('Sponsorship accepted', 'Close', { duration: 3000 });
        if (this.selectedEvent) {
          this.selectEvent(this.selectedEvent.id);
        }
      },
      error: () => this.snackBar.open('Failed to accept sponsorship', 'Close', { duration: 3000 })
    });
  }

  rejectSponsorship(sponsorshipId: number): void {
    const notes = prompt('Enter rejection reason:');
    if (notes) {
      this.eventService.rejectSponsorship(sponsorshipId, notes).subscribe({
        next: () => {
          this.snackBar.open('Sponsorship rejected', 'Close', { duration: 3000 });
          if (this.selectedEvent) {
            this.selectEvent(this.selectedEvent.id);
          }
        },
        error: () => this.snackBar.open('Failed to reject sponsorship', 'Close', { duration: 3000 })
      });
    }
  }
}
import { Component } from '@angular/core';
import { SponsorshipService } from '../../services/sponsorship.service';

@Component({
  selector: 'app-sponsor-request',
  template: `
    <h2>Request Sponsorship</h2>
    <form (ngSubmit)="onSubmit()" #requestForm="ngForm">
      <mat-form-field>
        <mat-label>Sponsor ID</mat-label>
        <input matInput [(ngModel)]="request.sponsorId" name="sponsorId" type="number" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Event ID</mat-label>
        <input matInput [(ngModel)]="request.eventId" name="eventId" type="number" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Contribution Amount</mat-label>
        <input matInput [(ngModel)]="request.contributionAmount" name="contributionAmount" type="number" required>
      </mat-form-field>
      <button mat-raised-button color="primary" type="submit" [disabled]="!requestForm.valid">Submit Request</button>
    </form>
    <p *ngIf="message">{{ message }}</p>
  `
})
export class SponsorRequestComponent {
  request = { sponsorId: null, eventId: null, contributionAmount: null };
  message: string = '';

  constructor(private sponsorshipService: SponsorshipService) {}

  async onSubmit() {
    try {
      await this.sponsorshipService.requestSponsorship(
        this.request.sponsorId!,
        this.request.eventId!,
        this.request.contributionAmount!
      );
      this.message = 'Sponsorship request submitted successfully!';
      this.request = { sponsorId: null, eventId: null, contributionAmount: null };
    } catch (error) {
      this.message = 'Failed to submit request';
      console.error(error);
    }
  }
}
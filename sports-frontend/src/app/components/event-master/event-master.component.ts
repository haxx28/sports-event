import { Component } from '@angular/core';
import { SponsorshipService } from '../../services/sponsorship.service';
import { Sponsorship } from '../../models/sponsorship';

@Component({
  selector: 'app-event-master',
  template: `
    <h2>Event Master - Sponsorship Requests</h2>
    <mat-form-field>
      <mat-label>Event ID</mat-label>
      <input matInput [(ngModel)]="eventId" name="eventId" type="number" required>
    </mat-form-field>
    <button mat-raised-button color="primary" (click)="loadSponsorships()">Load Requests</button>

    <table mat-table [dataSource]="sponsorships" *ngIf="sponsorships.length">
      <ng-container matColumnDef="sponsorName">
        <th mat-header-cell *matHeaderCellDef>Sponsor</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.sponsor.name }}</td>
      </ng-container>
      <ng-container matColumnDef="companyName">
        <th mat-header-cell *matHeaderCellDef>Company</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.sponsor.companyName }}</td>
      </ng-container>
      <ng-container matColumnDef="amount">
        <th mat-header-cell *matHeaderCellDef>Amount</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.contributionAmount }}</td>
      </ng-container>
      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef>Status</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.status }}</td>
      </ng-container>
      <ng-container matColumnDef="action">
        <th mat-header-cell *matHeaderCellDef>Action</th>
        <td mat-cell *matCellDef="let sponsorship">
          <button mat-raised-button color="accent" 
                  *ngIf="sponsorship.status === 'PENDING'" 
                  (click)="acceptSponsorship(sponsorship.id!)">
            Accept
          </button>
        </td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
  `
})
export class EventMasterComponent {
  eventId: number | null = null;
  sponsorships: Sponsorship[] = [];
  displayedColumns = ['sponsorName', 'companyName', 'amount', 'status', 'action'];

  constructor(private sponsorshipService: SponsorshipService) {}

  async loadSponsorships() {
    if (this.eventId) {
      try {
        this.sponsorships = await this.sponsorshipService.getSponsorshipsByEvent(this.eventId);
      } catch (error) {
        console.error('Failed to load sponsorships', error);
      }
    }
  }

  async acceptSponsorship(sponsorshipId: number) {
    try {
      await this.sponsorshipService.acceptSponsorship(sponsorshipId);
      this.loadSponsorships();
    } catch (error) {
      console.error('Failed to accept sponsorship', error);
    }
  }
}
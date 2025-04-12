import { Component } from '@angular/core';
import { SponsorshipService } from '../../services/sponsorship.service';
import { Sponsorship } from '../../models/sponsorship';

@Component({
  selector: 'app-sponsor-status',
  template: `
    <h2>Sponsorship Status</h2>
    <mat-form-field>
      <mat-label>Sponsor ID</mat-label>
      <input matInput [(ngModel)]="sponsorId" name="sponsorId" type="number" required>
    </mat-form-field>
    <button mat-raised-button color="primary" (click)="loadSponsorships()">Check Status</button>

    <table mat-table [dataSource]="sponsorships" *ngIf="sponsorships.length">
      <ng-container matColumnDef="eventName">
        <th mat-header-cell *matHeaderCellDef>Event</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.event.name }}</td>
      </ng-container>
      <ng-container matColumnDef="amount">
        <th mat-header-cell *matHeaderCellDef>Amount</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.contributionAmount }}</td>
      </ng-container>
      <ng-container matColumnDef="status">
        <th mat-header-cell *matHeaderCellDef>Status</th>
        <td mat-cell *matCellDef="let sponsorship">{{ sponsorship.status }}</td>
      </ng-container>
      <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
      <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
    </table>
  `
})
export class SponsorStatusComponent {
  sponsorId: number | null = null;
  sponsorships: Sponsorship[] = [];
  displayedColumns = ['eventName', 'amount', 'status'];

  constructor(private sponsorshipService: SponsorshipService) {}

  async loadSponsorships() {
    if (this.sponsorId) {
      try {
        this.sponsorships = await this.sponsorshipService.getSponsorshipsBySponsor(this.sponsorId);
      } catch (error) {
        console.error('Failed to load sponsorships', error);
      }
    }
  }
}
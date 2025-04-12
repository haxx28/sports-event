import { Component } from '@angular/core';
import { SponsorshipService } from '../../services/sponsorship.service';

@Component({
  selector: 'app-sponsor-register',
  template: `
    <h2>Register as Sponsor</h2>
    <form (ngSubmit)="onSubmit()" #registerForm="ngForm">
      <mat-form-field>
        <mat-label>Name</mat-label>
        <input matInput [(ngModel)]="sponsor.name" name="name" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Company Name</mat-label>
        <input matInput [(ngModel)]="sponsor.companyName" name="companyName" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Email</mat-label>
        <input matInput [(ngModel)]="sponsor.email" name="email" type="email" required>
      </mat-form-field>
      <mat-form-field>
        <mat-label>Phone</mat-label>
        <input matInput [(ngModel)]="sponsor.phone" name="phone">
      </mat-form-field>
      <button mat-raised-button color="primary" type="submit" [disabled]="!registerForm.valid">Register</button>
    </form>
    <p *ngIf="registeredSponsor">Registered! Your ID: {{ registeredSponsor.id }}</p>
  `
})
export class SponsorRegisterComponent {
  sponsor = { name: '', companyName: '', email: '', phone: '' };
  registeredSponsor: any = null;

  constructor(private sponsorshipService: SponsorshipService) {}

  async onSubmit() {
    try {
      this.registeredSponsor = await this.sponsorshipService.registerSponsor(this.sponsor);
      this.sponsor = { name: '', companyName: '', email: '', phone: '' };
    } catch (error) {
      console.error('Registration failed', error);
    }
  }
}
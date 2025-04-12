import { Injectable } from '@angular/core';
import axios from 'axios';
import { Sponsor } from '../models/sponsor';
import { Sponsorship } from '../models/sponsorship';

@Injectable({
  providedIn: 'root'
})
export class SponsorshipService {
  private apiUrl = 'http://localhost:8080/api/sponsorship';

  async registerSponsor(sponsor: Sponsor): Promise<Sponsor> {
    const response = await axios.post(`${this.apiUrl}/register`, sponsor);
    return response.data;
  }

  async requestSponsorship(sponsorId: number, eventId: number, contributionAmount: number): Promise<Sponsorship> {
    const response = await axios.post(`${this.apiUrl}/request`, { sponsorId, eventId, contributionAmount });
    return response.data;
  }

  async acceptSponsorship(sponsorshipId: number): Promise<Sponsorship> {
    const response = await axios.post(`${this.apiUrl}/accept/${sponsorshipId}`);
    return response.data;
  }

  async getSponsorshipsBySponsor(sponsorId: number): Promise<Sponsorship[]> {
    const response = await axios.get(`${this.apiUrl}/sponsor/${sponsorId}`);
    return response.data;
  }

  async getSponsorshipsByEvent(eventId: number): Promise<Sponsorship[]> {
    const response = await axios.get(`${this.apiUrl}/event/${eventId}`);
    return response.data;
  }
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Event } from '../models/event';
import { Sponsorship } from '../models/sponsorship';

@Injectable({
  providedIn: 'root'
})
export class EventService {
  private apiUrl = `${environment.apiUrl}/sponsorship`;

  constructor(private http: HttpClient) {}

  getAvailableEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.apiUrl}/events`);
  }

  getSponsorshipsByEvent(eventId: number): Observable<Sponsorship[]> {
    return this.http.get<Sponsorship[]>(`${this.apiUrl}/event/${eventId}`);
  }

  acceptSponsorship(sponsorshipId: number): Observable<Sponsorship> {
    return this.http.post<Sponsorship>(`${this.apiUrl}/accept/${sponsorshipId}`, {});
  }

  rejectSponsorship(sponsorshipId: number, notes: string): Observable<Sponsorship> {
    return this.http.post<Sponsorship>(`${this.apiUrl}/reject/${sponsorshipId}`, { notes });
  }
}
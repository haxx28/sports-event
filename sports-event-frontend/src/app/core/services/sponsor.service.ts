import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Sponsorship } from '../models/sponsorship';
import { Sponsor } from '../models/sponsor';

@Injectable({
  providedIn: 'root'
})
export class SponsorService {
  private apiUrl = `${environment.apiUrl}/sponsorship`;

  constructor(private http: HttpClient) {}

  register(sponsor: Sponsor & { username: string; password: string }): Observable<Sponsor> {
    return this.http.post<Sponsor>(`${this.apiUrl}/register`, sponsor);
  }

  update(sponsor: Sponsor): Observable<Sponsor> {
    return this.http.put<Sponsor>(`${this.apiUrl}/update`, sponsor);
  }

  deactivate(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/deactivate`, {});
  }

  reactivate(): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/reactivate`, {});
  }

  requestSponsorship(eventId: number, amount: number): Observable<Sponsorship> {
    return this.http.post<Sponsorship>(`${this.apiUrl}/request`, { eventId, contributionAmount: amount });
  }

  getSponsorships(): Observable<Sponsorship[]> {
    return this.http.get<Sponsorship[]>(`${this.apiUrl}/sponsor`);
  }
}
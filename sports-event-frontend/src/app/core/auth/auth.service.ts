import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  private tokenSubject = new BehaviorSubject<string | null>(localStorage.getItem('token'));
  private roleSubject = new BehaviorSubject<string | null>(localStorage.getItem('role'));
  token$ = this.tokenSubject.asObservable();
  role$ = this.roleSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.apiUrl}/login`, { username, password })
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          const payload = JSON.parse(atob(response.token.split('.')[1]));
          localStorage.setItem('role', payload.role);
          localStorage.setItem('username', payload.sub);
          this.tokenSubject.next(response.token);
          this.roleSubject.next(payload.role);
        })
      );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
    this.tokenSubject.next(null);
    this.roleSubject.next(null);
  }

  isAuthenticated(): boolean {
    return !!this.tokenSubject.value;
  }

  getRole(): string | null {
    return this.roleSubject.value;
  }

  getUsername(): string | null {
    return localStorage.getItem('username');
  }
  
  getToken(): string | null {
    return this.tokenSubject.value;
  }
}



 
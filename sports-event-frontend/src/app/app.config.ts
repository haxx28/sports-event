import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/sponsor/register/register.component';
import { SponsorDashboardComponent } from './features/sponsor/dashboard/dashboard.component';
import { EventManagerDashboardComponent } from './features/event-manager/dashboard/dashboard.component';
import { AuthGuard } from './core/auth/auth.guard';
import { JwtInterceptor } from './core/auth/jwt.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter([
      { path: '', redirectTo: '/login', pathMatch: 'full' },
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      {
        path: 'sponsor',
        component: SponsorDashboardComponent,
        canActivate: [AuthGuard],
        data: { role: 'SPONSOR' }
      },
      {
        path: 'event-manager',
        component: EventManagerDashboardComponent,
        canActivate: [AuthGuard],
        data: { role: 'EVENT_MANAGER' }
      }
    ]),
    provideHttpClient(withInterceptorsFromDi()),
    provideAnimations(),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi :true }
  ]
};
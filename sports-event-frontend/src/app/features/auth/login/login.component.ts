import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../../core/auth/auth.service';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MaterialModule } from '../../../shared/material.module';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse } from '@angular/common/http';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, MaterialModule, RouterLink]
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;
      this.authService.login(username, password).subscribe({
        next: () => {
          const role = this.authService.getRole();
          if (role === 'SPONSOR') {
            this.router.navigate(['/sponsor']);
          } else if (role === 'EVENT_MANAGER') {
            this.router.navigate(['/event-manager']);
          }
        },
        error: (err: HttpErrorResponse) => {
          console.error('Login error:', err);
          if (err.status === 401) {
            this.snackBar.open('Invalid username or password', 'Close', { duration: 3000 });
          } else {
            this.snackBar.open('Login failed: ' + (err.message || 'Unknown error'), 'Close', { duration: 3000 });
          }
        }
      }
      );
    }
  }
}
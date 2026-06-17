import { HttpClient } from '@angular/common/http';
import { Injectable, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
  name: string;
  avatar: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly apiUrl = 'http://localhost:8080/api/auth';

  readonly token = signal<string | null>(localStorage.getItem('sh_token'));
  readonly currentUser = signal<{ name: string; avatar: string } | null>(
    JSON.parse(localStorage.getItem('sh_user') || 'null'),
  );

  readonly isAuthenticated = computed(() => !!this.token());

  loginWithGoogle(googleIdToken: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/google`, { idToken: googleIdToken }).pipe(
      tap((res) => {
        localStorage.setItem('sh_token', res.token);
        localStorage.setItem('sh_user', JSON.stringify({ name: res.name, avatar: res.avatar }));

        this.token.set(res.token);
        this.currentUser.set({ name: res.name, avatar: res.avatar });
      }),
    );
  }

  logout(): void {
    localStorage.removeItem('sh_token');
    localStorage.removeItem('sh_user');
    this.token.set(null);
    this.currentUser.set(null);
    this.router.navigate(['/auth']);
  }
}

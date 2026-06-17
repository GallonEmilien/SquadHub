import { ChangeDetectionStrategy, Component, inject, NgZone, OnInit } from '@angular/core';
import { MatButton } from '@angular/material/button';
import {
  MatCard,
  MatCardActions,
  MatCardContent,
  MatCardHeader,
  MatCardSubtitle,
  MatCardTitle,
} from '@angular/material/card';
import { Router } from '@angular/router';
import { IonContent } from '@ionic/angular/standalone';
import { AuthService } from '../../../core/services/auth.service';

// On déclare la variable globale Google chargée par le script index.html
declare const google: any;

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [
    IonContent,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatCardActions,
    MatButton,
  ],
  templateUrl: './auth.page.html',
  styleUrls: ['./auth.page.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class AuthPage implements OnInit {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly ngZone = inject(NgZone);

  private readonly googleClientId = '819356916935-ugn9egh0l31t70jjg35t5s1r3pjp8k00.apps.googleusercontent.com';

  ngOnInit(): void {
    if (typeof google !== 'undefined') {
      google.accounts.id.initialize({
        client_id: this.googleClientId,
        callback: (response: any) => this.handleGoogleCredentialResponse(response),
      });
    }
  }

  triggerGoogleLogin(): void {
    if (typeof google !== 'undefined') {
      google.accounts.id.prompt();
      google.accounts.id.requestCode();
    } else {
      console.error("Le SDK Google n'est pas encore chargé.");
    }
  }

  private handleGoogleCredentialResponse(response: any): void {
    const realGoogleIdToken = response.credential;

    if (realGoogleIdToken) {
      this.ngZone.run(() => {
        this.authService.loginWithGoogle(realGoogleIdToken).subscribe({
          next: () => {
            this.router.navigate(['/home']);
          },
          error: (err) => {
            console.error('Erreur authentification SquadHub', err);
          },
        });
      });
    }
  }
}

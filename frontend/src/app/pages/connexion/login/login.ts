import {Component, inject, signal} from '@angular/core';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/input';
import {AuthService} from '../../../base/services/authService';
import { ReactiveFormsModule } from '@angular/forms';
import { FormGroup, FormControl } from '@angular/forms';


@Component({
  selector: 'app-login',
  imports: [
    MatCard,
    MatCardContent,
    MatFormField,
    MatLabel,
    ReactiveFormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {
  private authService = inject(AuthService);

  form = new FormGroup({
    email: new FormControl(''),
    motDePasse: new FormControl('')
  });

  etape = signal<number>(1);
  chargement = signal<boolean>(false);
  erreur = signal<string>('');
  tentative = signal<number>(0);

  onSuivant() {
    this.chargement.set(true);
    this.authService.verifyEmail(this.form.value.email!).subscribe({
      next: (existe) => {
        existe
          ? this.etape.set(2)
          : this.erreur.set('Email introuvable');
        this.chargement.set(false);
      },
      error: () => {
        this.erreur.set('Erreur serveur');
        this.chargement.set(false);
      }
    });
  }
}

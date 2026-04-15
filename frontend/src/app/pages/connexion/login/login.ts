import { Component } from '@angular/core';
import {MatCard, MatCardContent} from '@angular/material/card';
import {MatFormField, MatLabel} from '@angular/material/input';

@Component({
  selector: 'app-login',
  imports: [
    MatCard,
    MatCardContent,
    MatFormField,
    MatLabel
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {

}

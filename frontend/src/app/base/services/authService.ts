import { Injectable, inject } from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8081/api/v1';

  verifyEmail(email: string) {
    return this.http.post<boolean>(`${this.apiUrl}/authentification/verifier-email`, email);
  }
}

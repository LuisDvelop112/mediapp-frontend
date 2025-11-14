// src/app/core/services/auth.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

interface LoginResponse {
  token?: string;          // común: { token: '...' } o { accessToken: '...' }
  accessToken?: string;
  // puedes añadir otros campos que tu API devuelva (user, expiresIn, etc.)
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token'; // nombre en localStorage
  private readonly API_LOGIN_URL = 'https://backendmedia-app-production.up.railway.app/api/auth/login';


  constructor(private http: HttpClient, private router: Router) { }

  /**
   * Realiza login en el backend. Devuelve Observable<boolean> indicando éxito.
   * Ajusta el payload según tu API (email/username + password).
   */
  login(credentials: { email: string; password: string }): Observable<boolean> {
  const payload = {
    email: credentials.email,
    contraseña: credentials.password
  };

  return this.http.post<any>(this.API_LOGIN_URL, payload).pipe(
    map((res) => {
      console.log('Respuesta de login:', res);
      const token = res.token ?? res.accessToken ?? null;
      if (!token) {
        throw new Error('Respuesta de login no contiene token');
      }

      // ✅ Guarda el token
      this.setToken(token);

      // ✅ Guarda datos del usuario directamente (sin "res.usuario")
      localStorage.setItem('user_id', res.idUsuario.toString());
      localStorage.setItem('user_nombre', res.nombre);
      localStorage.setItem('user_apellido', res.apellido);
      localStorage.setItem('user_email', res.email);
      localStorage.setItem('user_fotoPerfil', res.fotoPerfil || '');
      localStorage.setItem('user_role', res.tipoUsuario);
      localStorage.setItem('contrasenia', res.contraseña);

      return true;
    }),
    catchError((err: HttpErrorResponse) => {
      console.error('Error en login:', err);
      return of(false);
    })
  );
}



  /**
   * Cierra sesión: borra token y redirige (opcional).
   */
  logout(redirectTo: string | null = '/landing/home'): void {
    this.clearToken();
    if (redirectTo) {
      this.router.navigate([redirectTo]);
    }
  }

  // ---------- Token storage ----------
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  setToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  clearToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  getUserId(): number | null {
    const id = localStorage.getItem('user_id');
    return id ? Number(id) : null;
  }

  clearStorage(): void {
    console.log('Limpiando almacenamiento local de información del usuario');
    localStorage.removeItem('user_id');
    localStorage.removeItem('user_nombre');
    localStorage.removeItem('user_apellido');
    localStorage.removeItem('user_email');
    localStorage.removeItem('user_fotoPerfil');
    localStorage.removeItem('user_role');
  }

  // ---------- Decodificación y utilidades ----------
  /**
   * Decodifica el JWT y devuelve el payload como objeto (o null).
   */
  getUserFromToken(): any | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload;
    } catch (e) {
      console.error('Error decodificando token:', e);
      return null;
    }
  }

  /**
   * Obtiene el rol principal del token y lo normaliza (quita prefijo ROLE_ si existe).
   */
  getRole(): string | null {
    const payload = this.getUserFromToken();
    if (!payload) return null;

    // Posibles nombres del claim: roles (array), authorities (array), role (string)
    const roleFromToken =
      payload.roles?.[0] ?? payload.authorities?.[0] ?? payload.role ?? null;

    if (!roleFromToken) return null;

    return typeof roleFromToken === 'string' && roleFromToken.startsWith('ROLE_')
      ? roleFromToken.replace('ROLE_', '')
      : roleFromToken;
  }

  /**
   * Comprueba si hay un token válido (existencia y no-expirado).
   */
  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;
    return !this.isTokenExpired(token);
  }

  /**
   * Comprueba si un token JWT está expirado (si incluye claim 'exp').
   */
  isTokenExpired(token?: string): boolean {
    const t = token ?? this.getToken();
    if (!t) return true;
    try {
      const payload = JSON.parse(atob(t.split('.')[1]));
      if (!payload.exp) return false; // si no hay exp asumimos no-expirado
      const nowSec = Math.floor(Date.now() / 1000);
      return payload.exp < nowSec;
    } catch (e) {
      console.error('Error comprobando expiración del token:', e);
      return true;
    }
  }
}

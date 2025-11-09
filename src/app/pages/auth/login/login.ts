import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  email: string = '';
  password: string = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private authService: AuthService
  ) {}

  login() {
    const body = {
      email: this.email,
      contraseña: this.password
    };

    this.http.post<any>('http://localhost:8080/api/auth/login', body)
      .subscribe({
        next: (res) => {

          // ✅ Guardar datos importantes para RoleGuard y toda la app
          this.authService.login(
            res.token,
            res.tipoUsuario
          );

          // ✅ Guardar datos adicionales del usuario
          localStorage.setItem('user_id', res.idUsuario);
          localStorage.setItem('user_email', res.email);
          localStorage.setItem('user_nombre', res.nombre);
          localStorage.setItem('user_apellido', res.apellido);
          localStorage.setItem('refresh_token', res.refreshToken);

          // ✅ Redirigir según rol
          if (res.tipoUsuario === 'PACIENTE') {
            this.router.navigate(['/dashboard/home']);
          } else if (res.tipoUsuario === 'MEDICO') {
            this.router.navigate(['/dashboard/home']);
          } else if (res.tipoUsuario === 'ADMIN') {
            this.router.navigate(['/dashboard/home']);
          }
        },

        error: (error) => {
          console.error('❌ Error al iniciar sesión >>>', error);
          alert('Correo o contraseña incorrectos');
        }
      });
  }
}

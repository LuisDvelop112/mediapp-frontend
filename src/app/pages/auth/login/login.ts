import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class Login {
  email: string = '';
  password: string = '';
  loading: boolean = false;
  errorMsg: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(form: NgForm) {
    if (form.invalid) {
      this.errorMsg = 'Por favor, completa todos los campos.';
      return;
    }

    this.loading = true;
    this.errorMsg = '';

    // El AuthService se encarga de hacer la llamada al backend y guardar el token
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (success) => {
        this.loading = false;

        if (success) {
          // ✅ Obtiene el rol para redirigir al dashboard correspondiente
          const role = this.authService.getRole();

          if (role === 'ADMIN') {
            this.router.navigate(['/admin/home']);
          } else if (role === 'PACIENTE') {
            this.router.navigate(['/dashboard/home']);
          } else if (role === 'MEDICO') {
            this.router.navigate(['/doctor/dashboard']);
          }
        } else {
          this.errorMsg = 'Credenciales inválidas. Intenta nuevamente.';
        }
      },
      error: (err) => {
        console.error('❌ Error al iniciar sesión:', err);
        this.loading = false;
        this.errorMsg = 'Ocurrió un error en el servidor.';
      }
    });
  }
}

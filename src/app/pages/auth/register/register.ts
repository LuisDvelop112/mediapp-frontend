import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {
  registerForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      contraseña: ['', Validators.required],
      confirmar: ['', Validators.required],
      tipoUsuario: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) {
      alert('Por favor completa todos los campos');
      return;
    }

    if (this.registerForm.value.contraseña !== this.registerForm.value.confirmar) {
      alert('Las contraseñas no coinciden');
      return;
    }

    const payload = {
      nombre: this.registerForm.value.nombre,
      apellido: this.registerForm.value.apellido,
      email: this.registerForm.value.email,
      contraseña: this.registerForm.value.contraseña,
      tipoUsuario: this.registerForm.value.tipoUsuario
    };

    this.http.post('http://localhost:8080/api/auth/register', payload).subscribe({
      next: () => {
        alert('✅ Registro exitoso');
        this.router.navigate(['/login']); // <-- Redirige al login
      },
      error: (err) => {
        console.error(err);
        alert('❌ Error registrando usuario');
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UsuarioService } from '../../core/services/usuario.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile.html',
  styleUrls: ['./edit-profile.scss']
})

export class EditProfile implements OnInit {
  formData: any = {};
  userId = localStorage.getItem('user_id');

  constructor(private router: Router, private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    if (!this.userId) {
      alert('No se encontró el ID de usuario.');
      this.router.navigate(['/dashboard/home']);
      return;
    }
    this.usuarioService.obtenerUsuarioPorId(Number(this.userId)).subscribe({
      next: (usuario) => {
        this.formData = { ...usuario, contrasena: '' };
      },
      error: (err) => {
        console.error('Error cargando usuario:', err);
        alert('No se pudo cargar el perfil');
        this.router.navigate(['/dashboard/home']);
      }
    });
  }

  handleInputChange(field: string, value: string) {
    (this.formData as any)[field] = value;
  }

  // Eliminado: handlePhotoUpload. Ahora solo se pega una URL.

  handleSave() {
    if (!this.userId) {
      alert('No se encontró el ID de usuario.');
      return;
    }
    // Si la contraseña está vacía, no la enviamos (para no cambiarla)
    const data = { ...this.formData };
    if (!data.contrasena) delete data.contrasena;
    this.usuarioService.actualizarUsuario(Number(this.userId), data).subscribe({
      next: (res) => {
        localStorage.setItem('user_fotoPerfil', res.fotoPerfil || '');
        localStorage.setItem('user_nombre', res.nombre || '');
        localStorage.setItem('user_apellido', res.apellido || '');
        localStorage.setItem('user_email', res.email || '');
        alert('Perfil actualizado correctamente');
        this.router.navigate(['/dashboard/home']);
      },
      error: (err) => {
        alert('Error al actualizar el perfil');
        console.error(err);
      }
    });
  }

  handleCancel() {
    this.router.navigate(['/dashboard/home']);
  }
}

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EditarPerfilService, Usuario } from '../../core/services/editarperfil.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile.html',
  styleUrls: ['./edit-profile.scss']
})
export class EditProfile implements OnInit {

  formData: Usuario = {
    idUsuario: 0,
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    direccion: '',
    pais: '',
    ciudad: '',
    genero: '',
    fechaNacimiento: '',
    fotoPerfil: '',
    contrasenia: ''
  };

  userId = Number(localStorage.getItem('user_id'));

  constructor(
    private router: Router,
    private editarPerfilService: EditarPerfilService
  ) {}

  ngOnInit(): void {
    if (!this.userId) {
      alert('No se encontró el ID de usuario.');
      this.router.navigate(['/dashboard/home']);
      return;
    }

    this.editarPerfilService.obtenerUsuario(this.userId).subscribe({
      next: (usuario) => {
        this.formData = { ...usuario, contrasenia: '' }; // nunca mostrar contraseña
      },
      error: (err) => {
        console.error('Error cargando usuario:', err);
        alert('No se pudo cargar el perfil');
        this.router.navigate(['/dashboard/home']);
      }
    });
  }

  handleSave() {
    if (!this.formData) {
      alert("Error inesperado: faltan datos.");
      return;
    }

    const data: Partial<Usuario> = { ...this.formData };

    // Si no escribe contraseña, no se actualiza
    if (!data.contrasenia || data.contrasenia.trim() === '') {
      delete data.contrasenia;
    }

    this.editarPerfilService.actualizarUsuario(this.userId, data).subscribe({
      next: (res) => {
        // Actualizar datos en localStorage si aplica
        localStorage.setItem('user_nombre', res.nombre ?? '');
        localStorage.setItem('user_apellido', res.apellido ?? '');
        localStorage.setItem('user_email', res.email ?? '');
        localStorage.setItem('user_fotoPerfil', res.fotoPerfil ?? '');

        alert('Perfil actualizado correctamente');
        this.router.navigate(['/dashboard/home']);
      },
      error: (err) => {
        console.error(err);
        alert('Error al actualizar el perfil');
      }
    });
  }

  handleCancel() {
    this.router.navigate(['/dashboard/home']);
  }
}

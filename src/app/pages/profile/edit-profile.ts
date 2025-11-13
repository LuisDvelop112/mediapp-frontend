import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EditarPerfilService, Paciente } from '../../core/services/editarperfil.service';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './edit-profile.html',
  styleUrls: ['./edit-profile.scss']
})
export class EditProfile implements OnInit {

  formData: Partial<Paciente> = {};
  userId = localStorage.getItem('user_id');
  pacienteId: number | null = null;

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

    this.editarPerfilService.obtenerPaciente(Number(this.userId))
      .subscribe({
        next: (paciente) => {
          this.pacienteId = paciente.idPaciente;
          this.formData = { ...paciente };
        },
        error: (err: any) => {
          console.error('Error cargando paciente:', err);
          alert('No se pudo cargar el perfil del paciente');
          this.router.navigate(['/dashboard/home']);
        }
      });
  }

  handleInputChange(field: string, value: string) {
    (this.formData as any)[field] = value;
  }

  handleSave() {
    if (!this.pacienteId) {
      alert('No se encontró el ID del paciente.');
      return;
    }

    this.editarPerfilService.actualizarPaciente(this.pacienteId, this.formData)
      .subscribe({
        next: () => {
          alert('Perfil de paciente actualizado correctamente');
          this.router.navigate(['/dashboard/home']);
        },
        error: (err: any) => {
          console.error(err);
          alert('Error al actualizar el perfil de paciente.');
        }
      });
  }

  handleCancel() {
    this.router.navigate(['/dashboard/home']);
  }
}

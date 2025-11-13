import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { DoctorsService } from '../../../core/services/doctors.service';
import { SpecialitysService, Especialidad } from '../../../core/services/specialitys.service';

@Component({
  selector: 'app-doctor-speciality',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './speciality.html',
  styleUrls: ['./speciality.scss']
})
export class DoctorSpecialityComponent implements OnInit {

  doctor: any = null;
  especialidades: Especialidad[] = [];
  selectedEspecialidadId: number | null = null;
  mensaje = '';
  loading = true;

  constructor(
    private authService: AuthService,
    private doctorsService: DoctorsService,
    private specialitysService: SpecialitysService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    const userIdRaw = this.authService.getUserId();
    const userId = userIdRaw ? Number(userIdRaw) : null;

    if (!userId) {
      this.mensaje = 'Usuario no autenticado.';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    // 🔹 1️⃣ Obtener datos del médico
    this.doctorsService.getDoctorById(userId).subscribe({
      next: (medico) => {
        this.doctor = medico;
        

        // 🔹 2️⃣ Cargar todas las especialidades activas
        this.specialitysService.getAll().subscribe({
          next: (data) => {
            this.especialidades = data.filter(e => e.estado === 'ACTIVA');
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (err: any) => {
            console.error('❌ Error cargando especialidades:', err);
            this.mensaje = 'No se pudieron cargar las especialidades.';
            this.loading = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err: any) => {
        console.error('❌ Error obteniendo médico:', err);
        this.mensaje = 'No se pudo obtener el médico asociado.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  guardarEspecialidad(): void {
    if (!this.selectedEspecialidadId || !this.doctor?.idUsuario) {
      alert('Debes seleccionar una especialidad.');
      return;
    }

    // 🔹 Enviar el objeto esperado por el backend
    const payload = {
      especialidad: { idEspecialidad: this.selectedEspecialidadId }
    };

    this.doctorsService.asignarEspecialidad(this.doctor.idMedico!, this.selectedEspecialidadId!).subscribe({
      next: () => {
        alert('✅ Especialidad actualizada correctamente');
      },
      error: (err: any) => {
        console.error('❌ Error al asignar especialidad:', err);
        alert('No se pudo actualizar la especialidad.');
      }
    });
  }

  get doctorNombre(): string {
    if (!this.doctor) return '';
    return `${this.doctor.nombre || this.doctor.usuario?.nombre || ''} ${this.doctor.apellido || this.doctor.usuario?.apellido || ''}`.trim();
  }
}

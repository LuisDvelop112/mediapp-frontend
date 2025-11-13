import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DoctorsService, Doctor } from '../../../core/services/doctors.service';

@Component({
  selector: 'app-doctors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctors.html',
  styleUrls: ['./doctors.scss']
})
export class AdminDoctors implements OnInit {

  doctors: Doctor[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private doctorsService: DoctorsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadDoctors();
  }

  // 🔹 Cargar todos los doctores
  loadDoctors(): void {
    this.loading = true;
    this.errorMessage = null;
    console.log('📡 Cargando médicos...');

    this.doctorsService.getAllDoctors().subscribe({
      next: (data) => {
        console.log('✅ Doctores fusionados:', data);
        this.doctors = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('❌ Error al obtener médicos:', error);
        this.errorMessage = 'No se pudieron cargar los médicos.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // 🔹 Eliminar médico con manejo de errores detallado
  deleteDoctor(doctor: Doctor): void {
    if (!confirm(`¿Eliminar al Dr. ${doctor.nombre}?`)) return;

    console.log(`🗑️ Eliminando médico con ID Usuario=${doctor.idUsuario}, Medico=${doctor.idMedico}`);

    this.doctorsService.deleteDoctor(doctor.idUsuario, doctor.idMedico).subscribe({
      next: () => {
        console.log('✅ Médico eliminado correctamente');
        this.doctors = this.doctors.filter(d => d.idUsuario !== doctor.idUsuario);
        this.cdr.detectChanges();
        alert(`✅ El Dr. ${doctor.nombre} ha sido eliminado correctamente.`);
      },
      error: (err) => {
        console.error('❌ Error al eliminar médico:', err);

        // 🧠 Detectar si el error es por citas asociadas
        const backendMessage =
          err.error?.error ||
          err.error?.message ||
          err.message ||
          'No se pudo eliminar el médico.';

        if (backendMessage.includes('citas')) {
          alert(`⚠️ No se puede eliminar al Dr. ${doctor.nombre} porque tiene citas registradas.`);
        } else {
          alert(`❌ Error al eliminar médico: ${backendMessage}`);
        }
      }
    });
  }
}

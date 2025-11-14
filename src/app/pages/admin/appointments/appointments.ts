import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentService, Appointment } from '../../../core/services/appointment.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-admin-appointments',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointments.html',
  styleUrls: ['./appointments.scss']
})
export class AdminAppointments implements OnInit {

  appointments: Appointment[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private appointmentService: AppointmentService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.loadAppointments();
  }

  // 🔹 Cargar todas las citas
  loadAppointments(): void {
    this.loading = true;
    this.errorMessage = null;
    console.log('📡 Cargando todas las citas...');

    // Llamada directa al endpoint global de citas
    this.appointmentService['http'].get<Appointment[]>('https://backendmedia-app-production.up.railway.app/api/citas').subscribe({
      next: (data) => {
        console.log('✅ Citas obtenidas:', data);
        this.appointments = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error al obtener citas:', err);
        this.errorMessage = 'No se pudieron cargar las citas.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // 🔹 Refrescar manualmente
  refreshList(): void {
    console.log('🔄 Refrescando lista de citas...');
    this.loadAppointments();
  }

  // 🔹 Eliminar cita y refrescar lista
  deleteAppointment(cita: Appointment): void {
    if (!confirm(`¿Eliminar la cita del ${cita.fechaCita}?`)) return;

    console.log(`🗑️ Eliminando cita ID=${cita.idCita}...`);

    this.appointmentService.deleteAppointment(Number(cita.idCita)).subscribe({
      next: () => {
        console.log('✅ Cita eliminada correctamente');
        alert('✅ Cita eliminada correctamente');

        // 🔁 Vuelve a cargar los datos desde el backend
        setTimeout(() => {
          this.loadAppointments();
        }, 300);
      },
      error: (err) => {
        console.error('❌ Error al eliminar cita:', err);
        alert('No se pudo eliminar la cita.');
      },
      complete: () => {
        // 🔹 Forzar refresco de vista
        this.cdr.detectChanges();
      }
    });
  }
}

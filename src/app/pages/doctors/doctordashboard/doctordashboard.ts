import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-doctor-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './doctordashboard.html',
  styleUrls: ['./doctordashboard.scss']
})
export class DoctorDashboardComponent implements OnInit {

  doctor: any = null;
  citasProximas: any[] = [];
  citasPasadas: any[] = [];
  totalPendientes = 0;
  totalCompletadas = 0;
  loading = true;
  mensaje = '';

  constructor(
    private appointmentService: AppointmentService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.mensaje = '';

    const userIdRaw = this.authService.getUserId();
    const userId = userIdRaw ? Number(userIdRaw) : null;

    if (!userId) {
      this.mensaje = 'Usuario no autenticado';
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.appointmentService.getMedicoByUsuario(userId).subscribe({
      next: (medico) => {
        this.doctor = medico;
        this.loadCitasPorMedico(medico.idMedico);
      },
      error: (err: any) => {
        console.error('❌ Error obteniendo médico:', err);
        this.mensaje = 'No se pudo obtener el médico asociado.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadCitasPorMedico(idMedico: number): void {
    this.loading = true;

    this.appointmentService.getAppointmentsByDoctor(idMedico).subscribe({
      next: (citas: any[]) => {
        this.citasProximas = citas.filter(c => c.estado === 'PROGRAMADA');
        this.citasPasadas = citas.filter(c =>
          ['COMPLETADA', 'CANCELADA', 'NO_ASISTIO'].includes(c.estado)
        );

        this.totalPendientes = this.citasProximas.length;
        this.totalCompletadas = this.citasPasadas.filter(c => c.estado === 'COMPLETADA').length;

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('❌ Error cargando citas:', err);
        this.mensaje = 'No se pudieron cargar las citas.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  completarCita(idCita: number): void {
    this.appointmentService.completarCita(idCita).subscribe({
      next: () => this.loadAll(),
      error: (err: any) => {
        console.error('❌ Error al completar cita:', err);
        alert('No se pudo completar la cita.');
      }
    });
  }

  cancelarCita(idCita: number): void {
    this.appointmentService.cancelarCita(idCita).subscribe({
      next: () => this.loadAll(),
      error: (err: any) => {
        console.error('❌ Error al cancelar cita:', err);
        alert('No se pudo cancelar la cita.');
      }
    });
  }

  marcarNoAsistio(idCita: number): void {
    this.appointmentService.marcarNoAsistio(idCita).subscribe({
      next: () => this.loadAll(),
      error: (err: any) => {
        console.error('❌ Error al marcar no asistió:', err);
        alert('No se pudo marcar la cita.');
      }
    });
  }

  get doctorNombre(): string {
    if (!this.doctor) return '';
    return `${this.doctor.usuario?.nombre || this.doctor.nombre || ''} ${this.doctor.usuario?.apellido || this.doctor.apellido || ''}`.trim();
  }
}

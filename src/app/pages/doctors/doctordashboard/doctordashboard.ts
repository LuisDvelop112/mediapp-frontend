import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-doctor-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './doctordashboard.html',
  styleUrls: ['./doctordashboard.scss']
})
export class DoctorDashboardComponent implements OnInit {

  citas: any[] = [];
  idMedico!: number;
  cargando = true;
  filtroEstado = 'PROGRAMADA';
  mensaje = '';

  constructor(
    private appointmentService: AppointmentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idUsuario = this.authService.getUserId();

    if (idUsuario) {
      this.appointmentService.getMedicoByUsuario(idUsuario).subscribe({
        next: (medico) => {
          this.idMedico = medico.idMedico;
          this.cargarCitasPendientes();
        },
        error: (err) => {
          console.error('❌ Error obteniendo médico:', err);
          this.cargando = false;
          this.mensaje = 'No se pudo obtener el médico asociado a este usuario.';
        }
      });
    } else {
      this.mensaje = 'Usuario no autenticado.';
      this.cargando = false;
    }
  }

  cargarCitas(): void {
    if (!this.idMedico) return;

    this.cargando = true;
    this.appointmentService.getCitasPorMedicoYEstado(this.idMedico, this.filtroEstado).subscribe({
      next: (data) => {
        this.citas = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('❌ Error al cargar citas:', err);
        this.mensaje = 'Error al cargar las citas del médico.';
        this.cargando = false;
      }
    });
  }

  cargarCitasPendientes(): void {
    this.appointmentService.getCitasPorMedicoYEstado(this.idMedico, 'PROGRAMADA')
      .subscribe({
        next: (citas) => {
          this.citas = citas;
          this.cargando = false;
        },
        error: (err) => {
          console.error('❌ Error cargando citas pendientes:', err);
          this.mensaje = 'No se pudieron cargar las citas pendientes.';
          this.cargando = false;
        }
      });
  }

  cambiarEstado(accion: 'completar' | 'cancelar' | 'no-asistio', idCita: number): void {
    let request$;

    switch (accion) {
      case 'completar':
        request$ = this.appointmentService.completarCita(idCita);
        break;
      case 'cancelar':
        request$ = this.appointmentService.cancelarCita(idCita);
        break;
      case 'no-asistio':
        request$ = this.appointmentService.marcarNoAsistio(idCita);
        break;
    }

    if (!request$) return;

    request$.subscribe({
      next: () => {
        this.mensaje = `✅ Cita ${accion.replace('-', ' ')} con éxito.`;
        this.cargarCitasPendientes();
      },
      error: (err) => {
        console.error(`❌ Error al ${accion.replace('-', ' ')} la cita:`, err);
        this.mensaje = `❌ Error al ${accion.replace('-', ' ')} la cita.`;
      }
    });
  }

}

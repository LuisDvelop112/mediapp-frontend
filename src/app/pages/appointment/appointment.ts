import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../core/services/appointment.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-appointment-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment.html',
  styleUrls: ['./appointment.scss']
})
export class AppointmentCreate implements OnInit {

  appointment = {
    fechaCita: '',
    horaCita: '',
    tipoCita: '',
    motivoConsulta: '',
    medico: { idUsuario: 0 },   // 👈 usamos idUsuario del médico
    paciente: { idUsuario: 0 }  // 👈 usamos idUsuario del paciente logueado
  };

  medicos: any[] = [];
  mensaje = '';

  constructor(
    private appointmentService: AppointmentService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    // ✅ Establecer el idUsuario del paciente autenticado
    const idUsuario = Number(this.authService.getUserId());
    this.appointment.paciente.idUsuario = idUsuario;

    // 🔹 Simulación temporal de médicos (ahora con idUsuario)
    this.cargarMedicos();
  }
  
  cargarMedicos() {
    this.appointmentService.getMedicosActivos().subscribe({
      next: (data) => {
        this.medicos = data;
        console.log('✅ Médicos cargados desde backend:', this.medicos);
      },
      error: (err) => {
        console.error('❌ Error al cargar médicos:', err);
      }
    });
  }


  onSubmit() {
    if (
      !this.appointment.fechaCita ||
      !this.appointment.horaCita ||
      !this.appointment.tipoCita ||
      !this.appointment.medico.idUsuario
    ) {
      this.mensaje = '⚠️ Por favor completa todos los campos obligatorios.';
      return;
    }

    // ✅ Asegurar formato correcto para horaCita (HH:mm:ss)
    if (this.appointment.horaCita.length === 5) {
      this.appointment.horaCita += ':00';
    }

    // ✅ Mostrar payload para depuración
    const payload = { ...this.appointment };
    console.log('📦 Payload enviado a createAppointmentAuto:', JSON.stringify(payload, null, 2));

    // 🔹 Llamamos al método que resuelve los IDs reales
    this.appointmentService.createAppointmentAuto(payload).subscribe({
      next: () => {
        this.mensaje = '✅ Cita agendada correctamente.';
        this.resetForm();
      },
      error: (err) => {
        console.error('❌ Error al crear cita:', err);
        this.mensaje = '❌ Error al agendar la cita. Verifica los datos o el backend.';
      }
    });
  }

  private resetForm() {
    const idUsuario = Number(this.authService.getUserId());
    this.appointment = {
      fechaCita: '',
      horaCita: '',
      tipoCita: '',
      motivoConsulta: '',
      medico: { idUsuario: 0 },
      paciente: { idUsuario }   // mantiene el paciente autenticado
    };
  }
}

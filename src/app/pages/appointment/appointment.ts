import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../core/services/appointment.service';
import { AuthService } from '../../core/services/auth.service';
import { SpecialitysService } from '../../core/services/specialitys.service';
import { DoctorsService } from '../../core/services/doctors.service';

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
    medico: { idUsuario: 0 },
    paciente: { idUsuario: 0 }
  };

  especialidades: any[] = [];
  medicos: any[] = [];
  medicosFiltrados: any[] = [];
  selectedEspecialidadId: number = 0;

  mensaje = '';

  constructor(
    private appointmentService: AppointmentService,
    private authService: AuthService,
    private specialitysService: SpecialitysService,
    private doctorsService: DoctorsService
  ) { }

  ngOnInit(): void {
    const idUsuario = Number(this.authService.getUserId());
    this.appointment.paciente.idUsuario = idUsuario;

    this.cargarEspecialidades();
    this.cargarMedicos();
  }

  cargarEspecialidades() {
    this.specialitysService.getAll().subscribe({
      next: (data) => {
        this.especialidades = data.filter(e => e.estado === 'ACTIVA');
      },
      error: (err) => {
        console.error('❌ Error cargando especialidades:', err);
      }
    });
  }

  cargarMedicos() {
    console.log("ID Usuario Logueado:", this.authService.getUserId());
    this.doctorsService.getAllDoctors().subscribe({
      next: (data) => {
        this.medicos = data;
        this.medicosFiltrados = data; // mostrar todos inicialmente
        console.log('✅ Médicos cargados:', this.medicos);
      },
      error: (err) => {
        console.error('❌ Error al cargar médicos:', err);
      }
    });
  }

  filtrarMedicos() {
    if (!this.selectedEspecialidadId) {
      // Si no hay especialidad seleccionada, mostrar todos
      this.cargarMedicos();
      return;
    }

    this.doctorsService.getDoctorsByEspecialidad(this.selectedEspecialidadId).subscribe({
      next: (data) => {
        this.medicosFiltrados = data;
        console.log('✅ Médicos filtrados:', this.medicosFiltrados);
      },
      error: (err) => {
        console.error('❌ Error al filtrar médicos por especialidad:', err);
        this.medicosFiltrados = [];
      }
    });
  }


  private getEspecialidadNombre(id: number): string {
    return this.especialidades.find(e => e.idEspecialidad === id)?.nombreEspecialidad || '';
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

    if (this.appointment.horaCita.length === 5) {
      this.appointment.horaCita += ':00';
    }

    const payload = { ...this.appointment };
    console.log('📦 Payload enviado:', JSON.stringify(payload, null, 2));

    this.appointmentService.createAppointmentAuto(payload).subscribe({
      next: () => {
        this.mensaje = '✅ Cita agendada correctamente.';

        // 🔥 NUEVO — mensaje emergente
        alert('✅ ¡Tu cita ha sido agendada con éxito!');

        this.resetForm();
      },
      error: (err) => {
        console.error('❌ Error al crear cita:', err);
        this.mensaje = '❌ Error al agendar la cita.';
        alert('❌ Ocurrió un error al agendar la cita. Inténtalo nuevamente.');
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
      paciente: { idUsuario }
    };
    this.selectedEspecialidadId = 0;
    this.medicosFiltrados = this.medicos;
  }
}

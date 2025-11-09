import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-schedule',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './schedule.html',
  styleUrl: './schedule.scss',
})
export class Schedule {
  especialidades = ['Cardiología', 'Pediatría', 'Dermatología', 'Neurología', 'Odontología', 'Ginecología'];
  profesionales = ['Dr. Pérez', 'Dra. Gómez', 'Dr. Torres', 'Dra. Martínez'];

  cita = {
    especialidad: '',
    profesional: '',
    fecha: '',
    hora: '',
  };

  confirmarCita() {
    if (this.cita.especialidad && this.cita.profesional && this.cita.fecha && this.cita.hora) {
      console.log('Cita confirmada:', this.cita);
      alert(`Cita confirmada con ${this.cita.profesional} el ${this.cita.fecha} a las ${this.cita.hora}`);
    } else {
      alert('Por favor, completa todos los campos antes de confirmar.');
    }
  }
}

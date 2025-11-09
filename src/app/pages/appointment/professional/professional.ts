import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-professional',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './professional.html',
  styleUrls: ['./professional.scss'],
})
export class Professional {
  medicos = [
    {
      usuario: { nombre: 'Laura', apellido: 'Castaño' },
      especialidad: { nombre: 'Cardiología' },
      hospitalAfiliado: 'Hospital San José',
      experienciaAnos: 10,
      calificacionPromedio: 4.7,
      foto: 'https://cdn-icons-png.flaticon.com/512/2922/2922510.png'
    },
    {
      usuario: { nombre: 'Carlos', apellido: 'Restrepo' },
      especialidad: { nombre: 'Pediatría' },
      hospitalAfiliado: 'Clínica del Café',
      experienciaAnos: 8,
      calificacionPromedio: 4.9,
      foto: 'https://cdn-icons-png.flaticon.com/512/2922/2922656.png'
    }
  ];

  getStars(calificacion: number) {
    return Array(Math.round(calificacion)).fill(0);
  }
}

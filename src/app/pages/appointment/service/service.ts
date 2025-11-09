import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-service',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './service.html',
  styleUrl: './service.scss',
})
export class Service {
  servicios = [
    {
      nombre: 'Cardiología',
      descripcion: 'Diagnóstico y tratamiento de enfermedades del corazón y sistema circulatorio.',
      icon: 'fa-solid fa-heart-pulse',
    },
    {
      nombre: 'Pediatría',
      descripcion: 'Cuidado médico especializado para niños y adolescentes.',
      icon: 'fa-solid fa-baby',
    },
    {
      nombre: 'Dermatología',
      descripcion: 'Tratamiento de enfermedades de la piel, cabello y uñas.',
      icon: 'fa-solid fa-user-doctor',
    },
    {
      nombre: 'Neurología',
      descripcion: 'Atención especializada para trastornos del sistema nervioso.',
      icon: 'fa-solid fa-brain',
    },
    {
      nombre: 'Odontología',
      descripcion: 'Salud bucal y estética dental con tecnología moderna.',
      icon: 'fa-solid fa-tooth',
    },
    {
      nombre: 'Ginecología',
      descripcion: 'Atención integral en salud femenina y cuidado prenatal.',
      icon: 'fa-solid fa-venus',
    },
  ];
}

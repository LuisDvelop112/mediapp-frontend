import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../../../core/services/dashboard.service';

@Component({
  selector: 'app-dashboardhome',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboardhome.html',
  styleUrls: ['./dashboardhome.scss']
})
export class Dashboardhome implements OnInit {

  userId = Number(localStorage.getItem('user_id'));
  userNombre = localStorage.getItem('user_nombre') || '';
  userApellido = localStorage.getItem('user_apellido') || '';
  userEmail = localStorage.getItem('user_email') || '';
  userFotoPerfil = localStorage.getItem('user_fotoPerfil') || '';

  proximasCitas: any[] = [];
  citasCompletadas = 0;
  totalProfesionales = 0;
  siguienteCitaDias = '-';
  notificaciones: any[] = [];

  constructor(private dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.cargarDatosDashboard();
  }

  cargarDatosDashboard() {
    // ✅ Próximas citas
    this.dashboardService.getProximasCitas(this.userId).subscribe(res => {
      this.proximasCitas = res;

      if (res.length > 0) {
        const hoy = new Date();
        const fechaCita = new Date(res[0].fecha);
        const diff = Math.ceil((fechaCita.getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24));
        this.siguienteCitaDias = diff <= 0 ? 'Hoy' : diff.toString();
      }
    });

    // ✅ Citas completadas
    this.dashboardService.getCitasCompletadas(this.userId).subscribe(res => {
      this.citasCompletadas = res;
    });

    // ✅ Total de profesionales
    this.dashboardService.getTotalProfesionales(this.userId).subscribe(res => {
      this.totalProfesionales = res;
    });

    // ✅ Notificaciones
    this.dashboardService.getNotificaciones(this.userId).subscribe(res => {
      this.notificaciones = res;
    });
  }
}

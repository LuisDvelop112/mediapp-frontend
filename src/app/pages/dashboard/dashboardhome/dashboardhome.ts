import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../../../core/services/dashboard.service';
import { Subscription, interval } from 'rxjs';

@Component({
  selector: 'app-dashboardhome',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboardhome.html',
  styleUrls: ['./dashboardhome.scss']
})
export class Dashboardhome implements OnInit, OnDestroy {

  userId = Number(localStorage.getItem('user_id'));
  userNombre = localStorage.getItem('user_nombre') || '';
  userApellido = localStorage.getItem('user_apellido') || '';
  userEmail = localStorage.getItem('user_email') || '';
  userFotoPerfil = localStorage.getItem('user_fotoPerfil') || '';

  proximasCitas: any[] = [];
  citasCompletadas = 0;
  totalProfesionales = 0;
  siguienteCitaDias: string | number = '-';

  private autoRefresh!: Subscription;

  constructor(
    private dashboardService: DashboardService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargarDatosDashboard();

    // 🔄 Auto-actualización cada 20 segundos
    this.autoRefresh = interval(20000).subscribe(() => {
      console.log("🔄 Auto-refresh del Dashboard");
      this.cargarDatosDashboard();
    });
  }

  ngOnDestroy(): void {
    // 🧹 Evita fugas de memoria
    if (this.autoRefresh) {
      this.autoRefresh.unsubscribe();
    }
  }

  cargarDatosDashboard() {
    console.log('Cargando datos del dashboard para el usuario ID:', this.userId);

    // 🔵 Próximas citas
    this.dashboardService.getTodasCitas().subscribe(citas => {
      console.log("📌 Próximas citas recibidas:", citas);

      this.proximasCitas = citas;

      if (citas.length > 0) {
        const hoy = new Date();
        const fechaCita = new Date(citas[0].fechaCita);

        const diff = Math.ceil(
          (fechaCita.getTime() - hoy.getTime()) / (1000 * 60 * 60 * 24)
        );

        this.siguienteCitaDias = diff <= 0 ? 'Hoy' : diff;
      } else {
        this.siguienteCitaDias = '-';
      }

      this.cdr.detectChanges();
    });

    // 🔵 Citas completadas
    this.dashboardService.getCitasCompletadas().subscribe(count => {
      this.citasCompletadas = count;
      this.cdr.detectChanges();
    });

    // 🔵 Total de profesionales
    this.dashboardService.getTotalProfesionales().subscribe(total => {
      this.totalProfesionales = total;
      this.cdr.detectChanges();
    });
  }
}

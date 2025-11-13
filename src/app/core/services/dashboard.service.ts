import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { AuthService } from './auth.service'; // 👈 importa el servicio de autenticación

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiCitas = 'https://backendmedia-app-production.up.railway.app/api/citas';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // ✅ obtener id del usuario logueado
  private getIdPaciente(): number {
    console.log('Obteniendo ID del usuario logueado desde AuthService', this.authService.getUserId());
    return this.authService.getUserId()!;
  }

  // ✅ Próximas citas del paciente
  getProximasCitas(): Observable<any[]> {
    const idPaciente = this.getIdPaciente();
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}/proximas`);
  }

  // ✅ Citas completadas
  getCitasCompletadas(): Observable<number> {
    const idPaciente = this.getIdPaciente();
    return this.http.get<number>(`${this.apiCitas}/contar/paciente/${idPaciente}/estado/COMPLETADA`);
  }

  // ✅ Todas las citas del paciente
  getTodasCitas(): Observable<any[]> {
    const idPaciente = this.getIdPaciente();
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}`);
  }

  // ✅ Obtener número de profesionales distintos
  getTotalProfesionales(): Observable<number> {
    const idPaciente = this.getIdPaciente();
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}`)
      .pipe(
        map(citas => {
          const medicos = new Set(citas.map(c => c.medico?.id));
          return medicos.size;
        })
      );
  }

  // ✅ Notificaciones: citas confirmadas
  getNotificaciones(): Observable<any[]> {
    const idPaciente = this.getIdPaciente();
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}/estado/CONFIRMADA`);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private API_URL = 'https://backendmedia-app-production.up.railway.app/api/citas';

  constructor(
    private authService: AuthService,
    private http: HttpClient
  ) {}

  // 🔵 Obtener ID real del paciente desde AuthService/localStorage
  private getIdPaciente(): number {
    const id = this.authService.getUserId();
    if (!id) {
      console.error("❌ No se encontró user_id en localStorage");
      return 0;
    }
    return id;
  }

  // 🔵 PROXIMAS CITAS
  getProximasCitas(): Observable<any[]> {
    const id = this.getIdPaciente();
    return this.http.get<any[]>(`${this.API_URL}/paciente/${id}/proximas`);
  }

  // 🔵 CANTIDAD DE CITAS COMPLETADAS
  getCitasCompletadas(): Observable<number> {
    const id = this.getIdPaciente();
    return this.http.get<number>(`${this.API_URL}/contar/paciente/${id}/estado/COMPLETADA`);
  }

  // 🔵 TODAS LAS CITAS DEl PACIENTE
  getTodasCitas(): Observable<any[]> {
    const id = this.getIdPaciente();
    return this.http.get<any[]>(`${this.API_URL}/paciente/${id}`);
  }

  // 🔵 TOTAL DE MÉDICOS DISTINTOS
  getTotalProfesionales(): Observable<number> {
    const id = this.getIdPaciente();
    return this.http.get<any[]>(`${this.API_URL}/paciente/${id}`).pipe(
      map(citas => {
        const medicos = new Set(
          citas.map(c => c.medico?.idMedico)
        );
        return medicos.size;
      })
    );
  }

  // 🔵 NOTIFICACIONES = citas en estado PROGRAMADA
  getNotificaciones(): Observable<any[]> {
    const id = this.getIdPaciente();
    return this.http.get<any[]>(`${this.API_URL}/paciente/${id}/estado/PROGRAMADA`);
  }

}

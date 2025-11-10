import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiCitas = 'http://56.125.172.86:8080/api/citas';

  constructor(private http: HttpClient) {}

  // ✅ Próximas citas del paciente
  getProximasCitas(idPaciente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}/proximas`);
  }

  // ✅ Citas completadas
  getCitasCompletadas(idPaciente: number): Observable<number> {
    return this.http.get<number>(`${this.apiCitas}/contar/paciente/${idPaciente}/estado/COMPLETADA`);
  }

  // ✅ Todas las citas del paciente
  getTodasCitas(idPaciente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}`);
  }

  // ✅ Obtener número de profesionales distintos
  getTotalProfesionales(idPaciente: number): Observable<number> {
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}`)
      .pipe(
        map(citas => {
          const medicos = new Set(citas.map(c => c.medico?.id));
          return medicos.size;
        })
      );
  }

  // ✅ Notificaciones: citas confirmadas
  getNotificaciones(idPaciente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiCitas}/paciente/${idPaciente}/estado/CONFIRMADA`);
  }

}

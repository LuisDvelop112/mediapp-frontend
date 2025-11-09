import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CitasService {

  private apiUrl = 'http://localhost:8080/api/citas';

  constructor(private http: HttpClient) {}

  // ✅ Obtener todas las citas del paciente
  getCitasPorPaciente(idPaciente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paciente/${idPaciente}`);
  }

  // ✅ Próximas citas del paciente
  getProximasCitas(idPaciente: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paciente/${idPaciente}/proximas`);
  }

  // ✅ Citas completadas del paciente
  getCantidadCitasCompletadas(idPaciente: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/contar/paciente/${idPaciente}/estado/COMPLETADA`);
  }

  // ✅ Citas por estado
  getCitasPorEstado(idPaciente: number, estado: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/paciente/${idPaciente}/estado/${estado}`);
  }

  // ✅ Crear nueva cita
  crearCita(body: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}`, body);
  }

  // ✅ Cancelar cita
  cancelarCita(id: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/cancelar`, {});
  }

  // ✅ Completar cita
  completarCita(id: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/completar`, {});
  }

}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Tipo del Paciente basado en tu entidad de backend
export type Paciente = {
  idPaciente: number;
  usuario: any; // puedes tiparlo después si quieres
  numeroIdentificacion?: string;
  tipoSangre?: string;
  alergias?: string;
  enfermedadesCronicas?: string;
  medicamentosActuales?: string;
  contactoEmergencia?: string;
  telefonoEmergencia?: string;
};

@Injectable({
  providedIn: 'root'
})
export class EditarPerfilService {

  private api = 'https://backendmedia-app-production.up.railway.app/api/pacientes';

  constructor(private http: HttpClient) {}

  // ✅ 1. Obtener información del paciente por ID
  obtenerPaciente(id: number): Observable<Paciente> {
    return this.http.get<Paciente>(`${this.api}/${id}`);
  }

  // ✅ 2. Actualizar información del paciente
  actualizarPaciente(id: number, data: Partial<Paciente>): Observable<Paciente> {
    return this.http.put<Paciente>(`${this.api}/${id}`, data);
  }
}

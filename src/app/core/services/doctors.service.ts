import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Puedes definir un modelo de datos básico para los médicos
export interface Doctor {
  id: number;
  nombre: string;
  apellido: string;
  especialidad: string;
  correo: string;
  telefono?: string;
  fotoPerfil?: string;
}

@Injectable({
  providedIn: 'root'
})
export class DoctorsService {

  // ✅ URL base de tu backend local
  private readonly API_URL = 'http://localhost:8080/api/medicos';

  constructor(private http: HttpClient) {}

  /**
   * 🔹 Obtener todos los médicos
   */
  getAllDoctors(): Observable<Doctor[]> {
    return this.http.get<Doctor[]>(this.API_URL);
  }

  /**
   * 🔹 Obtener un médico por ID
   */
  getDoctorById(id: number): Observable<Doctor> {
    return this.http.get<Doctor>(`${this.API_URL}/${id}`);
  }

  /**
   * 🔹 Crear un nuevo médico (si lo necesitas desde el panel admin)
   */
  createDoctor(doctor: Partial<Doctor>): Observable<Doctor> {
    return this.http.post<Doctor>(this.API_URL, doctor);
  }

  /**
   * 🔹 Actualizar datos de un médico
   */
  updateDoctor(id: number, doctor: Partial<Doctor>): Observable<Doctor> {
    return this.http.put<Doctor>(`${this.API_URL}/${id}`, doctor);
  }

  /**
   * 🔹 Eliminar un médico
   */
  deleteDoctor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}

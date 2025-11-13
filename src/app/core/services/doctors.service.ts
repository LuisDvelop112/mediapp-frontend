import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { forkJoin, map, Observable, of, switchMap } from 'rxjs';

// Modelo de doctor unificado
interface MedicoResponse {
  idMedico: number;
  usuario: {
    idUsuario: number;
    nombre: string;
    apellido: string;
    email: string;
    telefono?: string;
    ciudad?: string;
  };
  especialidad?: {
    idEspecialidad: number;
    nombreEspecialidad?: string;
  };
}

export interface Doctor {
  idUsuario: number;
  idMedico?: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono?: string;
  ciudad?: string;
  especialidad?: string | { idEspecialidad: number; nombreEspecialidad?: string };
}

@Injectable({
  providedIn: 'root'
})
export class DoctorsService {

  // ✅ URLs base para usuarios y médicos
  private readonly API_URL_USUARIOS = 'https://backendmedia-app-production.up.railway.app/api/usuarios';
  private readonly API_URL_MEDICOS = 'https://backendmedia-app-production.up.railway.app/api/medicos';

  constructor(private http: HttpClient) { }

  /**
   * 🔹 Obtener todos los doctores (usuarios con rol MEDICO)
   * y combinar con sus datos de la entidad Medico.
   */
  getAllDoctors(): Observable<Doctor[]> {
    return this.http.get<any[]>(`${this.API_URL_USUARIOS}/tipo/MEDICO`).pipe(
      switchMap((usuarios) => {
        if (!usuarios.length) return of([]);

        // 🔁 Crear consultas paralelas para buscar idMedico por usuario
        const medicoRequests = usuarios.map((u) =>
          this.http.get<any>(`${this.API_URL_MEDICOS}/usuario/${u.idUsuario}`).pipe(
            map((medico) => ({
              idUsuario: u.idUsuario,
              idMedico: medico?.idMedico || null,
              nombre: u.nombre,
              apellido: u.apellido,
              email: u.email,
              telefono: u.telefono,
              ciudad: u.ciudad,
              especialidad: medico?.especialidad?.nombreEspecialidad || 'Sin especialidad'
            }))
          )
        );

        return forkJoin(medicoRequests);
      })
    );
  }

  /**
   * 🔹 Obtener doctor por ID de usuario
   */
  getDoctorById(idUsuario: number): Observable<Doctor> {
    return this.http.get<any>(`${this.API_URL_MEDICOS}/usuario/${idUsuario}`).pipe(
      map((medico) => ({
        idUsuario,
        idMedico: medico?.idMedico,
        nombre: medico?.usuario?.nombre || '—',
        apellido: medico?.usuario?.apellido || '—',
        email: medico?.usuario?.email || '—',
        telefono: medico?.usuario?.telefono,
        ciudad: medico?.usuario?.ciudad,
        especialidad: medico?.especialidad || { idEspecialidad: 0, nombreEspecialidad: 'Sin especialidad' }
      }))
    );
  }

  /**
   * 🔹 Crear un nuevo doctor (usuario con tipo MEDICO)
   */
  createDoctor(doctor: Partial<Doctor>): Observable<Doctor> {
    const payload = { ...doctor, tipoUsuario: 'MEDICO' };
    return this.http.post<Doctor>(this.API_URL_USUARIOS, payload);
  }

  /**
   * 🔹 Actualizar datos del doctor
   */
  updateDoctor(id: number, doctor: Partial<Doctor>): Observable<Doctor> {
    return this.http.put<Doctor>(`${this.API_URL_USUARIOS}/${id}`, doctor);
  }

  /**
   * 🔹 Asignar una especialidad a un médico
   */
  asignarEspecialidad(idMedico: number, idEspecialidad: number): Observable<Doctor> {
    return this.http.patch<Doctor>(`${this.API_URL_MEDICOS}/${idMedico}/especialidad/${idEspecialidad}`, {});
  }

  /**
   * 🔹 Eliminar médico y su usuario
   */
  deleteDoctor(idUsuario: number, idMedico?: number): Observable<void> {
    if (idMedico) {
      return this.http.delete<void>(`${this.API_URL_MEDICOS}/${idMedico}`).pipe(
        switchMap(() => this.http.delete<void>(`${this.API_URL_USUARIOS}/${idUsuario}`))
      );
    } else {
      return this.http.delete<void>(`${this.API_URL_USUARIOS}/${idUsuario}`);
    }
  }

  getDoctorsByEspecialidad(idEspecialidad: number): Observable<Doctor[]> {
    return this.http.get<MedicoResponse[]>(`${this.API_URL_MEDICOS}/especialidad/${idEspecialidad}`).pipe(
      map((medicos) =>
        medicos.map((m) => ({
          idUsuario: m.usuario.idUsuario,
          idMedico: m.idMedico,
          nombre: m.usuario.nombre,
          apellido: m.usuario.apellido,
          email: m.usuario.email,
          telefono: m.usuario.telefono,
          ciudad: m.usuario.ciudad,
          especialidad: m.especialidad || { idEspecialidad: 0, nombreEspecialidad: 'Sin especialidad' }
        }))
      )
    );
  }


}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// ---------------------
// TIPOS
// ---------------------
export type Usuario = {
  idUsuario: number;
  nombre: string;
  apellido: string;
  email: string;
  telefono?: string;
  fotoPerfil?: string;
  direccion?: string;
  pais?: string;
  ciudad?: string;
  genero?: string;
  fechaNacimiento?: string;
  contrasenia?: string;
};

@Injectable({
  providedIn: 'root'
})
export class EditarPerfilService {

  private apiUsuarios = 'https://backendmedia-app-production.up.railway.app/api/usuarios';

  constructor(private http: HttpClient) {}

  obtenerUsuario(idUsuario: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUsuarios}/${idUsuario}`);
  }

  actualizarUsuario(idUsuario: number, data: Partial<Usuario>): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUsuarios}/${idUsuario}`, data);
  }
}

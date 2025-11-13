import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type Especialidad = {
  idEspecialidad: number;
  nombreEspecialidad: string;
  descripcion?: string;
  estado: 'ACTIVA' | 'INACTIVA';
};

@Injectable({
  providedIn: 'root'
})
export class SpecialitysService {

  private api = 'https://backendmedia-app-production.up.railway.app/api/especialidades';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Especialidad[]> {
    return this.http.get<Especialidad[]>(this.api);
  }

  getById(id: number): Observable<Especialidad> {
    return this.http.get<Especialidad>(`${this.api}/${id}`);
  }

  create(data: Especialidad): Observable<Especialidad> {
    return this.http.post<Especialidad>(this.api, data);
  }

  update(id: number, data: Especialidad): Observable<Especialidad> {
    return this.http.put<Especialidad>(`${this.api}/${id}`, data);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.api}/${id}`);
  }

  activate(id: number): Observable<Especialidad> {
    return this.http.patch<Especialidad>(`${this.api}/${id}/activar`, {});
  }

  deactivate(id: number): Observable<Especialidad> {
    return this.http.patch<Especialidad>(`${this.api}/${id}/desactivar`, {});
  }
}

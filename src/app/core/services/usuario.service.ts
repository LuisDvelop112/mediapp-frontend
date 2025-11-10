import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private apiUrl = 'http://56.125.172.86:8080/api/usuarios';

  constructor(private http: HttpClient) {}


  private buildAuthHeaders(): { headers?: HttpHeaders } {
    const token = localStorage.getItem('token');
    if (token) {
      return { headers: new HttpHeaders({ Authorization: `Bearer ${token}` }) };
    }
    return {};
  }

  actualizarUsuario(id: number, usuario: any): Observable<any> {
    const opts = this.buildAuthHeaders();
    return this.http.put(`${this.apiUrl}/${id}`, usuario, opts);
  }

  obtenerUsuarioPorId(id: number): Observable<any> {
    const opts = this.buildAuthHeaders();
    return this.http.get(`${this.apiUrl}/${id}`, opts);
  }
}

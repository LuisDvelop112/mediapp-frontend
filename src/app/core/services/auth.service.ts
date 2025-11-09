import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    // ✅ Guardar token y rol
    login(token: string, rol: string) {
        localStorage.setItem('token', token);
        localStorage.setItem('rol', rol);
    }

    // ✅ Obtener el rol
    getRole(): string | null {
        return localStorage.getItem('rol');
    }

    // ✅ Obtener token (por si lo usas en interceptores)
    getToken(): string | null {
        return localStorage.getItem('token');
    }

    // ✅ Cerrar sesión
    logout() {
        localStorage.clear();
        sessionStorage.clear();
    }

    // ✅ Saber si está autenticado
    isLoggedIn(): boolean {
        return !!localStorage.getItem('token');
    }
}

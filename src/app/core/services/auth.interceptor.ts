//Su función es interceptar cada petición HTTP que sale desde la aplicación y, 
//si existe un token, agregarle el header Authorization con el Bearer Token.


import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();
    // DEBUG: mostrar si el token existe y la URL que se intercepta
    // (remover o reducir logging en producción)
    console.debug('[AuthInterceptor] intercepted:', req.url, 'token present:', !!token);
    if (token) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
      return next.handle(cloned);
    }
    return next.handle(req);
  }
}

import { Routes } from '@angular/router';
import { Landinghome } from './pages/landing/landinghome/landinghome';
import { Login } from './pages/auth/login/login';
import { Register } from './pages/auth/register/register';
import { Dashboardhome } from './pages/dashboard/dashboardhome/dashboardhome';
import { EditProfile } from './pages/profile/edit-profile';
import { Service } from './pages/appointment/service/service';
import { Professional } from './pages/appointment/professional/professional';
import { Schedule } from './pages/appointment/schedule/schedule';
import { Confirmation } from './pages/appointment/confirmation/confirmation';

// ✅ Guards
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'landing/home', pathMatch: 'full' },

  // ✅ Rutas públicas
  { path: 'landing/home', component: Landinghome },
  { path: 'auth/login', component: Login },
  { path: 'auth/register', component: Register },

  // ✅ Rutas protegidas (cualquier usuario autenticado)
  {
    path: 'dashboard/home',
    component: Dashboardhome,
    canActivate: [AuthGuard]
  },

  // ✅ Editar perfil (cualquier usuario autenticado)
  {
    path: 'profile/edit',
    component: EditProfile,
    canActivate: [AuthGuard]
  },
  // ✅ Rutas SOLO PARA PACIENTES
  {
    path: 'appointment/service',
    component: Service,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['PACIENTE'] }
  },
  {
    path: 'appointment/professional',
    component: Professional,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['PACIENTE'] }
  },
  {
    path: 'appointment/schedule',
    component: Schedule,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['PACIENTE'] }
  },
  {
    path: 'appointment/confirmation',
    component: Confirmation,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['PACIENTE'] }
  },

  // ✅ wildcard
  { path: '**', redirectTo: 'landing/home' }
];

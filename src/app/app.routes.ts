import { Routes } from '@angular/router';
import { Landinghome } from './pages/landing/landinghome/landinghome';
import { Login } from './pages/auth/login/login';
import { Register } from './pages/auth/register/register';
import { Dashboardhome } from './pages/dashboard/dashboardhome/dashboardhome';
import { EditProfile } from './pages/profile/edit-profile';
import { Appointment } from './core/services/appointment.service';
import { DoctorDashboardComponent } from './pages/doctors/doctordashboard/doctordashboard';

// ✅ Admin pages
import { HomeComponent } from './pages/admin/home/home';
import { AdminAppointments } from './pages/admin/appointments/appointments';
import { AdminDoctors } from './pages/admin/doctors/doctors';
import { AdminSpecialitys } from './pages/admin/specialitys/specialitys';
import { AdminUsers } from './pages/admin/users/users';

// ✅ Guards
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { AppointmentCreate } from './pages/appointment/appointment';

export const routes: Routes = [
  { path: '', redirectTo: 'landing/home', pathMatch: 'full' },

  // ✅ Rutas públicas
  { path: 'landing/home', component: Landinghome },
  { path: 'auth/login', component: Login },
  { path: 'auth/register', component: Register },

  // ✅ Rutas protegidas (usuarios autenticados)
  {
    path: 'dashboard/home',
    component: Dashboardhome,
    canActivate: [AuthGuard]
  },

  // ✅ Editar perfil (usuarios autenticados)
  {
    path: 'profile/edit',
    component: EditProfile,
    canActivate: [AuthGuard]
  },

  // ✅ Rutas SOLO PARA PACIENTES

  {
    path: 'appointment/create',
    component: AppointmentCreate,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['PACIENTE'] }
  },

  // ✅ RUTAS SOLO PARA ADMINISTRADORES
  {
    path: 'admin/home',
    component: HomeComponent,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'admin/appointments',
    component: AdminAppointments,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'admin/doctors',
    component: AdminDoctors,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'admin/specialitys',
    component: AdminSpecialitys,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['ADMIN'] }
  },
  {
    path: 'admin/users',
    component: AdminUsers,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['ADMIN'] }
  },

  // ✅ RUTA SOLO PARA MÉDICOS
  {
    path: 'doctor/dashboard',
    component: DoctorDashboardComponent,
    canActivate: [RoleGuard, AuthGuard],
    data: { roles: ['MEDICO'] }
  },

  // ✅ wildcard
  { path: '**', redirectTo: 'landing/home' }
];

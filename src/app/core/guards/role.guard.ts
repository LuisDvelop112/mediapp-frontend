import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const RoleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const allowedRoles = route.data?.['roles'] as string[];
  const userRole = auth.getRole();

  console.log('Rol permitido:', allowedRoles);
  console.log('Rol del usuario:', userRole);

  if (userRole && allowedRoles.includes(userRole)) {
    return true;
  }

  router.navigate(['/dashboard/home']);
  return false;
};

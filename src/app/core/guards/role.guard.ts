// Protege rutas según el rol del usuario (admin, user, etc.).
// Solo deja entrar si su rol coincide con los roles permitidos.


import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const RoleGuard: CanActivateFn = (route) => {
  const auth = inject(AuthService);
  const router = inject(Router);

  const allowedRoles = route.data?.['roles'] as string[];
  const userRole = auth.getRole();

  console.log('🎯 Roles permitidos:', allowedRoles);
  console.log('👤 Rol del usuario:', userRole);

  // ✅ Comparación flexible (permite ROLE_ADMIN o ADMIN)
  const hasAccess =
    userRole &&
    allowedRoles.some(
      (role) =>
        role === userRole ||
        role === `ROLE_${userRole}` ||
        `ROLE_${role}` === userRole
    );

  if (hasAccess) {
    return true;
  }

  console.warn('🚫 Acceso denegado. Redirigiendo al dashboard.');
  router.navigate(['/dashboard/home']);
  return false;
};

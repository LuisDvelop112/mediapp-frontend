# 🚀 Inicio Rápido - Sistema de Autenticación JWT

## ⚡ En 5 minutos

### 1. Iniciar la aplicación
```bash
./mvnw spring-boot:run
```

### 2. Registrar un usuario de prueba

**Paciente:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María",
    "apellido": "González",
    "email": "maria@example.com",
    "contraseña": "password123",
    "tipoUsuario": "PACIENTE"
  }'
```

**Médico:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Dr. Carlos",
    "apellido": "Ramírez",
    "email": "carlos@example.com",
    "contraseña": "password123",
    "tipoUsuario": "MEDICO"
  }'
```

**Admin:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Admin",
    "apellido": "Sistema",
    "email": "admin@mediapp.com",
    "contraseña": "admin123",
    "tipoUsuario": "ADMIN"
  }'
```

### 3. Hacer login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@example.com",
    "contraseña": "password123"
  }'
```

**Copia el `token` de la respuesta**

### 4. Usar el token
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

---

## 🌐 Swagger UI

Abre en tu navegador:
```
http://localhost:8080/swagger-ui/index.html
```

1. Haz login y copia el token
2. Click en **Authorize** 🔓
3. Pega: `Bearer {token}`
4. Prueba los endpoints

---

## 📊 Estructura de Token

Tu token JWT contiene:
```json
{
  "authorities": ["ROLE_PACIENTE"],
  "sub": "maria@example.com",
  "iat": 1734182400,
  "exp": 1734268800
}
```

- **authorities**: Tus roles
- **sub**: Tu email
- **iat**: Fecha de creación
- **exp**: Fecha de expiración

Verifica tu token en: [jwt.io](https://jwt.io)

---

## 🎯 Roles y Permisos

| Rol | Puede acceder a |
|-----|-----------------|
| **PACIENTE** | `/api/pacientes/**`, sus propias citas |
| **MEDICO** | `/api/medicos/**`, todas las citas |
| **ADMIN** | Todo el sistema |

---

## 🔑 Credenciales de Prueba

Después de ejecutar los registros anteriores:

| Email | Contraseña | Rol |
|-------|-----------|-----|
| maria@example.com | password123 | PACIENTE |
| carlos@example.com | password123 | MEDICO |
| admin@mediapp.com | admin123 | ADMIN |

---

## ⚙️ Configuración

### Cambiar duración del token

En `application.properties`:
```properties
jwt.expiration=86400000        # 24 horas (en milisegundos)
jwt.refresh-expiration=604800000  # 7 días
```

### Cambiar secret (PRODUCCIÓN)

⚠️ **Nunca uses el secret por defecto en producción**

```bash
# Generar nuevo secret
openssl rand -base64 64
```

Luego actualiza:
```properties
jwt.secret=${JWT_SECRET}  # Variable de entorno
```

---

## 🐛 Problemas Comunes

### Error: 403 Forbidden
✅ **Solución**: Agrega el header `Authorization: Bearer {token}`

### Error: Token expirado
✅ **Solución**: Usa el refresh token o haz login nuevamente

### Error: Email ya existe
✅ **Solución**: Usa otro email o elimina el usuario de la BD

### Error: Credenciales inválidas
✅ **Solución**: Verifica email y contraseña

---

## 📖 Documentación Completa

- **Guía detallada**: `AUTHENTICATION_GUIDE.md`
- **Pruebas API**: `API_TESTS.md`
- **Resumen**: `IMPLEMENTATION_SUMMARY.md`
- **Ejemplos código**: `SecurityExampleController.java`

---

## ✅ Verificación Rápida

```bash
# 1. Servidor corriendo
curl http://localhost:8080/actuator/health

# 2. Registro funciona
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test","apellido":"User","email":"test@test.com","contraseña":"test123","tipoUsuario":"PACIENTE"}'

# 3. Login funciona
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","contraseña":"test123"}'

# 4. Token funciona (reemplaza TOKEN)
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer TOKEN"
```

---

## 🎉 ¡Listo!

Tu sistema de autenticación JWT está **funcionando** ✅

**Próximo paso**: Protege tus controladores con `@PreAuthorize`

Ejemplo:
```java
@GetMapping("/mis-citas")
@PreAuthorize("hasRole('PACIENTE')")
public List<Cita> getMisCitas(Authentication auth) {
    String email = auth.getName();
    // ... tu lógica
}
```

---

**¿Necesitas ayuda?** Revisa los archivos de documentación 📚

# Pruebas de Autenticación - MediApp

## 🧪 Colección de Pruebas API

### Base URL
```
http://localhost:8080
```

---

## 1️⃣ REGISTRO DE USUARIOS

### Registrar Paciente
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "María",
  "apellido": "González",
  "email": "maria.gonzalez@example.com",
  "contraseña": "password123",
  "fechaNacimiento": "1995-03-20",
  "genero": "FEMENINO",
  "telefono": "3101234567",
  "direccion": "Carrera 15 #20-30",
  "ciudad": "Armenia",
  "pais": "Colombia",
  "tipoUsuario": "PACIENTE"
}
```

### Registrar Médico
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Dr. Carlos",
  "apellido": "Ramírez",
  "email": "carlos.ramirez@example.com",
  "contraseña": "password123",
  "fechaNacimiento": "1980-07-15",
  "genero": "MASCULINO",
  "telefono": "3209876543",
  "direccion": "Avenida Bolívar #45-67",
  "ciudad": "Armenia",
  "pais": "Colombia",
  "tipoUsuario": "MEDICO"
}
```

### Registrar Admin
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Admin",
  "apellido": "Sistema",
  "email": "admin@mediapp.com",
  "contraseña": "admin123",
  "fechaNacimiento": "1990-01-01",
  "genero": "MASCULINO",
  "telefono": "3001111111",
  "direccion": "Oficina Central",
  "ciudad": "Armenia",
  "pais": "Colombia",
  "tipoUsuario": "ADMIN"
}
```

---

## 2️⃣ INICIO DE SESIÓN

### Login como Paciente
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "maria.gonzalez@example.com",
  "contraseña": "password123"
}
```

### Login como Médico
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "carlos.ramirez@example.com",
  "contraseña": "password123"
}
```

### Login como Admin
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@mediapp.com",
  "contraseña": "admin123"
}
```

**Respuesta Esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpdGllcyI6WyJST0xFX1BBQ0lFTlRFIl0sInN1YiI6Im1hcmlhLmdvbnphbGV6QGV4YW1wbGUuY29tIiwiaWF0IjoxNzM0MTgyNDAwLCJleHAiOjE3MzQyNjg4MDB9.xyz",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "idUsuario": 1,
  "email": "maria.gonzalez@example.com",
  "nombre": "María",
  "apellido": "González",
  "tipoUsuario": "PACIENTE"
}
```

---

## 3️⃣ REFRESCAR TOKEN

```http
POST /api/auth/refresh
Authorization: Bearer {REFRESH_TOKEN_AQUI}
```

---

## 4️⃣ PROBAR ENDPOINTS PROTEGIDOS

### Obtener todos los usuarios (requiere JWT)
```http
GET /api/usuarios
Authorization: Bearer {TOKEN_AQUI}
```

### Crear una cita (requiere autenticación)
```http
POST /api/citas
Authorization: Bearer {TOKEN_AQUI}
Content-Type: application/json

{
  "idPaciente": 1,
  "idMedico": 2,
  "fechaHora": "2024-12-20T10:00:00",
  "motivo": "Consulta general",
  "estado": "PROGRAMADA"
}
```

---

## 5️⃣ ERRORES COMUNES

### ❌ Email ya registrado
```json
{
  "timestamp": "2024-12-14T10:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "El email ya está registrado: maria.gonzalez@example.com"
}
```

### ❌ Credenciales inválidas
```json
{
  "timestamp": "2024-12-14T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciales inválidas"
}
```

### ❌ Token expirado o inválido
```json
{
  "timestamp": "2024-12-14T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}
```

### ❌ Sin token
```json
{
  "timestamp": "2024-12-14T10:00:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied"
}
```

---

## 6️⃣ EJEMPLOS DE VALIDACIÓN

### ❌ Email inválido
```json
{
  "email": "correo-invalido",
  "contraseña": "password123"
}
```
**Respuesta:**
```json
{
  "email": "El email debe ser válido"
}
```

### ❌ Contraseña muy corta
```json
{
  "email": "test@example.com",
  "contraseña": "123"
}
```
**Respuesta:**
```json
{
  "contraseña": "La contraseña debe tener al menos 6 caracteres"
}
```

### ❌ Campos obligatorios vacíos
```json
{
  "nombre": "",
  "apellido": "",
  "email": "",
  "contraseña": ""
}
```

---

## 7️⃣ SWAGGER UI

Accede a la documentación interactiva:
```
http://localhost:8080/swagger-ui/index.html
```

### Autenticarse en Swagger:
1. Haz login y copia el **token**
2. Click en el botón **Authorize** (🔓)
3. Ingresa: `Bearer {TOKEN_AQUI}`
4. Click en **Authorize**
5. Ahora puedes probar endpoints protegidos

---

## 📝 NOTAS IMPORTANTES

1. **Guarda el token**: Cada request protegido necesita el token en el header
2. **Token expira en 24 horas**: Usa refresh token para obtener uno nuevo
3. **Refresh token expira en 7 días**: Después debes hacer login nuevamente
4. **Formato del header**: Siempre usa `Bearer {token}`, no olvides el espacio

---

## 🔍 VERIFICAR TOKEN JWT

Puedes decodificar el token en: [https://jwt.io](https://jwt.io)

**Payload esperado:**
```json
{
  "authorities": ["ROLE_PACIENTE"],
  "sub": "maria.gonzalez@example.com",
  "iat": 1734182400,
  "exp": 1734268800
}
```

- `authorities`: Roles del usuario
- `sub`: Email del usuario (subject)
- `iat`: Fecha de emisión (issued at)
- `exp`: Fecha de expiración

---

**✅ Sistema de autenticación implementado y listo para usar**

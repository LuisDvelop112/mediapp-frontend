# 🛡️ Sistema de Manejo de Excepciones - MediApp

## 📋 Descripción General

Este sistema proporciona un manejo centralizado y consistente de excepciones para toda la aplicación MediApp. Todas las excepciones se capturan automáticamente y se devuelven respuestas JSON estructuradas al cliente.

---

## 📦 Excepciones Disponibles

### 1. **ResourceNotFoundException** (404 Not Found)
Se usa cuando no se encuentra un recurso solicitado.

**Constructores:**
```java
// Mensaje personalizado
throw new ResourceNotFoundException("Usuario no encontrado");

// Con información estructurada
throw new ResourceNotFoundException("Usuario", "ID", 123);
// Genera: "Usuario no encontrado con ID: '123'"
```

**Ejemplo de uso:**
```java
Usuario usuario = usuarioRepository.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", id));
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 404,
  "error": "Recurso No Encontrado",
  "message": "Usuario no encontrado con ID: '123'",
  "path": "/api/usuarios/123"
}
```

---

### 2. **ResourceAlreadyExistsException** (409 Conflict)
Se usa cuando se intenta crear un recurso que ya existe.

**Constructores:**
```java
// Mensaje personalizado
throw new ResourceAlreadyExistsException("El email ya está registrado");

// Con información estructurada
throw new ResourceAlreadyExistsException("Usuario", "email", "juan@example.com");
// Genera: "Usuario ya existe con email: 'juan@example.com'"
```

**Ejemplo de uso:**
```java
if (usuarioRepository.existsByEmail(email)) {
    throw new ResourceAlreadyExistsException("Usuario", "email", email);
}
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 409,
  "error": "Conflicto",
  "message": "Usuario ya existe con email: 'juan@example.com'",
  "path": "/api/usuarios"
}
```

---

### 3. **InvalidDataException** (400 Bad Request)
Se usa cuando los datos proporcionados no son válidos.

**Constructores:**
```java
// Mensaje simple
throw new InvalidDataException("El nombre es obligatorio");

// Con causa
throw new InvalidDataException("Error al procesar datos", exception);
```

**Ejemplo de uso:**
```java
if (usuario.getNombre() == null || usuario.getNombre().trim().isEmpty()) {
    throw new InvalidDataException("El nombre es obligatorio");
}
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 400,
  "error": "Datos Inválidos",
  "message": "El nombre es obligatorio",
  "path": "/api/usuarios"
}
```

---

### 4. **BusinessRuleException** (400 Bad Request)
Se usa cuando se viola una regla de negocio.

**Ejemplo de uso:**
```java
if (cita.getFechaCita().isBefore(LocalDate.now())) {
    throw new BusinessRuleException("No se puede crear una cita con fecha pasada");
}

if (medicoRepository.tieneCitaEnHorario(medicoId, fecha, hora)) {
    throw new BusinessRuleException("El médico ya tiene una cita agendada en ese horario");
}
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 400,
  "error": "Error de Regla de Negocio",
  "message": "No se puede crear una cita con fecha pasada",
  "path": "/api/citas"
}
```

---

### 5. **InvalidCredentialsException** (401 Unauthorized)
Se usa cuando las credenciales de autenticación son inválidas.

**Ejemplo de uso:**
```java
Usuario usuario = usuarioRepository.findByEmail(email)
    .orElseThrow(() -> new InvalidCredentialsException());

if (!passwordEncoder.matches(password, usuario.getContraseña())) {
    throw new InvalidCredentialsException("Email o contraseña incorrectos");
}
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 401,
  "error": "Credenciales Inválidas",
  "message": "Email o contraseña incorrectos",
  "path": "/api/auth/login"
}
```

---

### 6. **UnauthorizedException** (403 Forbidden)
Se usa cuando un usuario no tiene permisos para realizar una acción.

**Ejemplo de uso:**
```java
if (!usuario.getTipoUsuario().equals(TipoUsuario.ADMIN)) {
    throw new UnauthorizedException("Solo los administradores pueden realizar esta acción");
}

if (!cita.getPaciente().getIdPaciente().equals(pacienteId)) {
    throw new UnauthorizedException("No tienes permiso para modificar esta cita");
}
```

**Respuesta JSON:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 403,
  "error": "Acceso Denegado",
  "message": "Solo los administradores pueden realizar esta acción",
  "path": "/api/usuarios/123"
}
```

---

## 🎯 Validación Automática con @Valid

Cuando usas `@Valid` en los DTOs, los errores de validación se capturan automáticamente:

**Ejemplo en DTO:**
```java
@Data
public class UsuarioCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Email(message = "El email debe ser válido")
    private String email;
}
```

**Respuesta JSON de error de validación:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 400,
  "error": "Error de Validación",
  "message": "Los datos proporcionados no son válidos",
  "path": "/api/usuarios",
  "details": [
    "nombre: El nombre es obligatorio",
    "email: El email debe ser válido"
  ]
}
```

---

## 🔧 Ejemplos de Implementación

### Ejemplo 1: Servicio de Citas
```java
@Service
public class CitaServiceImpl implements CitaService {
    
    @Override
    public Cita crearCita(Cita cita) {
        // Validar que el paciente existe
        Paciente paciente = pacienteRepository.findById(cita.getPaciente().getIdPaciente())
            .orElseThrow(() -> new ResourceNotFoundException("Paciente", "ID", cita.getPaciente().getIdPaciente()));
        
        // Validar que el médico existe
        Medico medico = medicoRepository.findById(cita.getMedico().getIdMedico())
            .orElseThrow(() -> new ResourceNotFoundException("Médico", "ID", cita.getMedico().getIdMedico()));
        
        // Validar regla de negocio: fecha no puede ser pasada
        if (cita.getFechaCita().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("No se puede crear una cita con fecha pasada");
        }
        
        // Validar que el médico no tenga otra cita en el mismo horario
        if (citaRepository.existsByMedicoAndFechaAndHora(medico, cita.getFechaCita(), cita.getHoraCita())) {
            throw new BusinessRuleException("El médico ya tiene una cita agendada en ese horario");
        }
        
        return citaRepository.save(cita);
    }
}
```

### Ejemplo 2: Servicio de Especialidades
```java
@Service
public class EspecialidadServiceImpl implements EspecialidadService {
    
    @Override
    public Especialidad crearEspecialidad(Especialidad especialidad) {
        // Validar datos
        if (especialidad.getNombreEspecialidad() == null || especialidad.getNombreEspecialidad().trim().isEmpty()) {
            throw new InvalidDataException("El nombre de la especialidad es obligatorio");
        }
        
        // Validar unicidad
        if (especialidadRepository.existsByNombreEspecialidad(especialidad.getNombreEspecialidad())) {
            throw new ResourceAlreadyExistsException("Especialidad", "nombre", especialidad.getNombreEspecialidad());
        }
        
        return especialidadRepository.save(especialidad);
    }
    
    @Override
    public Especialidad actualizarEspecialidad(Integer id, Especialidad especialidad) {
        Especialidad especialidadExistente = especialidadRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Especialidad", "ID", id));
        
        // Actualizar campos
        especialidadExistente.setNombreEspecialidad(especialidad.getNombreEspecialidad());
        especialidadExistente.setDescripcion(especialidad.getDescripcion());
        
        return especialidadRepository.save(especialidadExistente);
    }
}
```

---

## 📝 Mejores Prácticas

1. **Usa excepciones específicas:** Elige la excepción más apropiada para cada situación
2. **Mensajes claros:** Proporciona mensajes descriptivos que ayuden al frontend a mostrar errores útiles
3. **No captures excepciones personalizadas:** Deja que el `GlobalExceptionHandler` las maneje automáticamente
4. **Valida en el servicio:** Las validaciones de negocio deben estar en la capa de servicio
5. **Usa @Valid en DTOs:** Para validaciones básicas de formato y obligatoriedad

---

## 🧪 Probando las Excepciones en Postman

### Test 1: Usuario no encontrado (404)
**Request:**
```
GET http://localhost:8080/api/usuarios/999
```

**Response esperada:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 404,
  "error": "Recurso No Encontrado",
  "message": "Usuario no encontrado con ID: '999'",
  "path": "/api/usuarios/999"
}
```

### Test 2: Email duplicado (409)
**Request:**
```
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "Juan",
  "apellido": "Pérez",
  "email": "juan@example.com",  // Email que ya existe
  "contraseña": "password123",
  "tipoUsuario": "PACIENTE"
}
```

**Response esperada:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 409,
  "error": "Conflicto",
  "message": "Usuario ya existe con email: 'juan@example.com'",
  "path": "/api/usuarios"
}
```

### Test 3: Datos inválidos (400)
**Request:**
```
POST http://localhost:8080/api/usuarios
Content-Type: application/json

{
  "nombre": "",  // Vacío (inválido)
  "apellido": "Pérez",
  "email": "correo-invalido",  // Email mal formateado
  "contraseña": "123",  // Muy corta
  "tipoUsuario": "PACIENTE"
}
```

**Response esperada:**
```json
{
  "timestamp": "2025-10-14T10:30:00",
  "status": 400,
  "error": "Error de Validación",
  "message": "Los datos proporcionados no son válidos",
  "path": "/api/usuarios",
  "details": [
    "nombre: El nombre es obligatorio",
    "email: El email debe ser válido",
    "contraseña: La contraseña debe tener al menos 8 caracteres"
  ]
}
```

---

## ✅ Ventajas de Este Sistema

- ✨ **Respuestas consistentes:** Todas las excepciones devuelven el mismo formato JSON
- 🎯 **Códigos HTTP correctos:** Cada excepción mapea al código HTTP apropiado
- 🔍 **Fácil debugging:** Timestamps y paths ayudan a rastrear problemas
- 📱 **Frontend-friendly:** Formato estructurado fácil de procesar
- 🛡️ **Centralizado:** Un solo lugar para manejar todas las excepciones
- 🚀 **Escalable:** Fácil agregar nuevas excepciones personalizadas

---

¡Sistema de excepciones implementado y listo para usar! 🎉

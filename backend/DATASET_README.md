# 📊 Dataset MediApp - Sistema de Gestión de Citas Médicas

## 📋 Descripción

Dataset completo con **datos de prueba realistas** para el sistema MediApp. Cumple con el requisito de **≥5 registros por tabla**.

## 📈 Contenido del Dataset

| Tabla | Registros | Descripción |
|-------|-----------|-------------|
| **usuarios** | 15 | 5 pacientes, 5 médicos, 5 administradores |
| **especialidades** | 8 | Especialidades médicas disponibles |
| **pacientes** | 5 | Perfiles completos de pacientes |
| **medicos** | 5 | Perfiles completos de médicos |
| **calendario_disponibilidad** | 15 | Horarios de atención de médicos |
| **citas** | 10 | Citas en diferentes estados |
| **historial_medico** | 5 | Historiales de citas completadas |
| **prescripciones** | 7 | Medicamentos prescritos |
| **evaluaciones** | 6 | Calificaciones de médicos |
| **notificaciones** | 10 | Notificaciones del sistema |

**Total: 86 registros** distribuidos en 10 tablas ✅

---

## 🚀 Cómo Cargar el Dataset

### Opción 1: Usando MySQL Workbench (Recomendado)

1. Abre MySQL Workbench
2. Conecta a tu servidor MySQL local
3. Abre el archivo `dataset_mediapp.sql`
4. Click en el botón ⚡ "Execute" o presiona `Ctrl+Shift+Enter`
5. ¡Listo! Los datos se cargarán automáticamente

### Opción 2: Desde la Terminal/Consola

```bash
# Navega a la carpeta del proyecto
cd /Users/sebastianagudelo/Documents/Uniquindio/citas-backend

# Ejecuta el script SQL
mysql -u root -p mediapp_db < src/main/resources/data/dataset_mediapp.sql

# Ingresa tu contraseña cuando se solicite
```

### Opción 3: Desde la aplicación Spring Boot

El script ya está en la ubicación correcta. Spring Boot puede cargarlo automáticamente si configuras:

**Opción A - Archivo específico:**
```properties
# En application.properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:data/dataset_mediapp.sql
```

**Opción B - Nombre estándar:** 
Renombra el archivo a `data.sql` y Spring Boot lo cargará automáticamente.

---

## 👥 Usuarios de Prueba

### 🔑 Credenciales

**Contraseña para TODOS los usuarios:** `password123`

### Pacientes
| Email | Nombre | Tipo Sangre |
|-------|--------|-------------|
| maria.gonzalez@example.com | María González | O+ |
| juan.perez@example.com | Juan Pérez | A+ |
| ana.martinez@example.com | Ana Martínez | B- |
| carlos.lopez@example.com | Carlos López | AB+ |
| laura.rodriguez@example.com | Laura Rodríguez | O- |

### Médicos
| Email | Nombre | Especialidad |
|-------|--------|--------------|
| carlos.ramirez@mediapp.com | Dr. Carlos Ramírez | Medicina General |
| patricia.gomez@mediapp.com | Dra. Patricia Gómez | Cardiología |
| roberto.silva@mediapp.com | Dr. Roberto Silva | Pediatría |
| sofia.torres@mediapp.com | Dra. Sofía Torres | Dermatología |
| miguel.hernandez@mediapp.com | Dr. Miguel Hernández | Oftalmología |

### Administradores
| Email | Nombre | Rol |
|-------|--------|-----|
| admin@mediapp.com | Admin Principal | Admin Principal |
| sandra.admin@mediapp.com | Sandra Administradora | Admin |
| diego.supervisor@mediapp.com | Diego Supervisor | Admin |
| carolina.gerente@mediapp.com | Carolina Gerente | Admin |
| ricardo.coordinador@mediapp.com | Ricardo Coordinador | Admin |

---

## 🧪 Casos de Prueba Incluidos

### 1. **Citas en Diferentes Estados**
- ✅ **Completadas** (5): Con historial médico
- 📅 **Programadas** (3): Citas futuras
- ❌ **Cancelada** (1): Por el paciente
- ⚠️ **No asistió** (1): Paciente ausente

### 2. **Historiales Médicos Completos**
- Diagnósticos detallados
- Síntomas reportados
- Tratamientos recomendados
- Medicamentos prescritos
- Observaciones médicas
- Fechas de seguimiento

### 3. **Prescripciones Médicas**
- Medicamentos con dosis y frecuencia
- Instrucciones detalladas
- Diferentes tipos de tratamientos

### 4. **Evaluaciones de Médicos**
- Calificaciones de 4-5 estrellas
- Comentarios de pacientes
- Respuestas de médicos

### 5. **Notificaciones del Sistema**
- Confirmaciones de citas
- Recordatorios
- Resultados disponibles
- Mensajes del sistema

---

## 📊 Casos de Uso Cubiertos

### ✅ Autenticación
- Login con diferentes roles (PACIENTE, MEDICO, ADMIN)
- Usuarios activos, inactivos y bloqueados

### ✅ Gestión de Citas
- Agendar citas presenciales y telemedicina
- Ver disponibilidad de médicos
- Historial de citas por paciente
- Citas por médico

### ✅ Historial Médico
- Consultar historiales completos
- Ver prescripciones asociadas
- Diagnósticos y tratamientos

### ✅ Evaluaciones
- Calificar médicos después de citas
- Ver calificaciones promedio
- Respuestas de médicos

### ✅ Notificaciones
- Notificaciones leídas/no leídas
- Diferentes tipos de notificaciones
- Enlaces relacionados

---

## 🔍 Consultas Útiles

### Ver todos los usuarios por tipo
```sql
SELECT tipo_usuario, COUNT(*) as total 
FROM usuarios 
GROUP BY tipo_usuario;
```

### Citas por estado
```sql
SELECT estado, COUNT(*) as total 
FROM citas 
GROUP BY estado;
```

### Médicos mejor calificados
```sql
SELECT u.nombre, u.apellido, m.calificacion_promedio, e.nombre_especialidad
FROM medicos m
JOIN usuarios u ON m.id_usuario = u.id_usuario
JOIN especialidades e ON m.id_especialidad = e.id_especialidad
ORDER BY m.calificacion_promedio DESC;
```

### Citas completadas con historial
```sql
SELECT 
    p.id_paciente,
    CONCAT(up.nombre, ' ', up.apellido) as paciente,
    CONCAT(um.nombre, ' ', um.apellido) as medico,
    c.fecha_cita,
    h.diagnostico
FROM citas c
JOIN pacientes p ON c.id_paciente = p.id_paciente
JOIN medicos m ON c.id_medico = m.id_medico
JOIN usuarios up ON p.id_usuario = up.id_usuario
JOIN usuarios um ON m.id_usuario = um.id_usuario
LEFT JOIN historial_medico h ON c.id_cita = h.id_cita
WHERE c.estado = 'COMPLETADA';
```

---

## ⚠️ Notas Importantes

### Contraseñas
- Todas las contraseñas están encriptadas con **BCrypt**
- Hash usado: `$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG`
- Contraseña en texto plano: `password123`

### IDs Generados
- Los IDs son auto-incrementales
- Las referencias entre tablas se mantienen correctamente
- Si tu base de datos tiene datos previos, ajusta los IDs según sea necesario

### Fechas
- Las citas pasadas usan fechas de octubre 2024
- Las citas futuras usan fechas de octubre 2025
- Los timestamps usan `NOW()` para fechas actuales

---

## 🧹 Limpiar Datos

Si necesitas limpiar todos los datos:

```sql
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE notificaciones;
TRUNCATE TABLE prescripciones;
TRUNCATE TABLE historial_medico;
TRUNCATE TABLE evaluaciones;
TRUNCATE TABLE citas;
TRUNCATE TABLE calendario_disponibilidad;
TRUNCATE TABLE medicos;
TRUNCATE TABLE pacientes;
TRUNCATE TABLE especialidades;
TRUNCATE TABLE usuarios;
SET FOREIGN_KEY_CHECKS = 1;
```

---

## ✅ Verificación

Después de cargar el dataset, verifica con:

```sql
SELECT 'usuarios' as tabla, COUNT(*) as total FROM usuarios
UNION ALL SELECT 'especialidades', COUNT(*) FROM especialidades
UNION ALL SELECT 'pacientes', COUNT(*) FROM pacientes
UNION ALL SELECT 'medicos', COUNT(*) FROM medicos
UNION ALL SELECT 'calendario_disponibilidad', COUNT(*) FROM calendario_disponibilidad
UNION ALL SELECT 'citas', COUNT(*) FROM citas
UNION ALL SELECT 'historial_medico', COUNT(*) FROM historial_medico
UNION ALL SELECT 'prescripciones', COUNT(*) FROM prescripciones
UNION ALL SELECT 'evaluaciones', COUNT(*) FROM evaluaciones
UNION ALL SELECT 'notificaciones', COUNT(*) FROM notificaciones;
```

**Resultado esperado:**
```
+---------------------------+-------+
| tabla                     | total |
+---------------------------+-------+
| usuarios                  |    15 |
| especialidades            |     8 |
| pacientes                 |     5 |
| medicos                   |     5 |
| calendario_disponibilidad |    15 |
| citas                     |    10 |
| historial_medico          |     5 |
| prescripciones            |     7 |
| evaluaciones              |     6 |
| notificaciones            |    10 |
+---------------------------+-------+
```

---

## 🎉 ¡Listo!

Tu base de datos ahora tiene un dataset completo y realista para probar todas las funcionalidades del sistema MediApp.

**¿Problemas al cargar?**
- Verifica que la base de datos `mediapp_db` exista
- Asegúrate de tener permisos de escritura
- Revisa que no haya datos previos que causen conflictos

---

**Creado para:** MediApp - Sistema de Gestión de Citas Médicas  
**Fecha:** Octubre 2024  
**Versión:** 1.0

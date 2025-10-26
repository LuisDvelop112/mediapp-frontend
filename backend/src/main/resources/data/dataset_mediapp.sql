-- =====================================================
-- DATASET MEDIAPP - Sistema de Gestión de Citas Médicas
-- Contiene ≥5 registros por tabla
-- =====================================================

-- Limpiar tablas en orden inverso de dependencias
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

-- =====================================================
-- 1. USUARIOS (15 registros: 5 pacientes, 5 médicos, 5 admins)
-- =====================================================
-- Contraseña para todos: "password123" (encriptada con BCrypt)
-- Hash BCrypt: $2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG

INSERT INTO usuarios (nombre, apellido, email, contraseña, fecha_nacimiento, genero, telefono, direccion, ciudad, pais, tipo_usuario, fecha_registro, estado) VALUES
-- PACIENTES (5)
(
   'María', 
   'González', 
   'maria.gonzalez@example.com', 
   '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', 
   '1995-03-15', 
   'FEMENINO', 
   '3101234567', 
   'Carrera 15 #20-30', 
   'Armenia', 
   'Colombia', 
   'PACIENTE', 
   NOW(), 
   'ACTIVO'
),
('Juan', 'Pérez', 'juan.perez@example.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1988-07-22', 'MASCULINO', '3112345678', 'Calle 10 #15-20', 'Armenia', 'Colombia', 'PACIENTE', NOW(), 'ACTIVO'),
('Ana', 'Martínez', 'ana.martinez@example.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1992-11-08', 'FEMENINO', '3123456789', 'Avenida Bolívar #45-12', 'Pereira', 'Colombia', 'PACIENTE', NOW(), 'ACTIVO'),
('Carlos', 'López', 'carlos.lopez@example.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1985-05-30', 'MASCULINO', '3134567890', 'Calle 25 #30-15', 'Manizales', 'Colombia', 'PACIENTE', NOW(), 'ACTIVO'),
('Laura', 'Rodríguez', 'laura.rodriguez@example.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1998-01-12', 'FEMENINO', '3145678901', 'Carrera 20 #10-05', 'Armenia', 'Colombia', 'PACIENTE', NOW(), 'ACTIVO'),

-- MÉDICOS (5)
(
   'Dr. Carlos', 
   'Ramírez', 
   'carlos.ramirez@mediapp.com', 
   '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', 
   '1980-04-15', 
   'MASCULINO',
   '3156789012', 
   'Calle 14 #8-30', 
   'Armenia', 
   'Colombia', 
   'MEDICO', 
   NOW(), 
   'ACTIVO'
),
('Dra. Patricia', 'Gómez', 'patricia.gomez@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1978-09-20', 'FEMENINO', '3167890123', 'Avenida Central #12-45', 'Pereira', 'Colombia', 'MEDICO', NOW(), 'ACTIVO'),
('Dr. Roberto', 'Silva', 'roberto.silva@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1975-12-10', 'MASCULINO', '3178901234', 'Carrera 10 #20-15', 'Manizales', 'Colombia', 'MEDICO', NOW(), 'ACTIVO'),
('Dra. Sofía', 'Torres', 'sofia.torres@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1982-06-25', 'FEMENINO', '3189012345', 'Calle 18 #15-20', 'Armenia', 'Colombia', 'MEDICO', NOW(), 'ACTIVO'),
('Dr. Miguel', 'Hernández', 'miguel.hernandez@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1977-03-18', 'MASCULINO', '3190123456', 'Avenida 6 #25-10', 'Pereira', 'Colombia', 'MEDICO', NOW(), 'ACTIVO'),

-- ADMINISTRADORES (5)
(
   'Admin', 
   'Principal', 
   'admin@mediapp.com', 
   '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', 
   '1985-01-01', 
   'MASCULINO', 
   '3201234567', 
   'Oficina Central', 
   'Armenia', 
   'Colombia', 
   'ADMIN', 
   NOW(), 
   'ACTIVO'
),
('Sandra', 'Administradora', 'sandra.admin@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1990-05-15', 'FEMENINO', '3212345678', 'Oficina Central', 'Armenia', 'Colombia', 'ADMIN', NOW(), 'ACTIVO'),
('Diego', 'Supervisor', 'diego.supervisor@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1987-08-20', 'MASCULINO', '3223456789', 'Oficina Central', 'Armenia', 'Colombia', 'ADMIN', NOW(), 'ACTIVO'),
('Carolina', 'Gerente', 'carolina.gerente@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1992-11-30', 'FEMENINO', '3234567890', 'Oficina Central', 'Armenia', 'Colombia', 'ADMIN', NOW(), 'ACTIVO'),
('Ricardo', 'Coordinador', 'ricardo.coordinador@mediapp.com', '$2a$10$XPTmXgFJPKPX9sVRMx8uXOmMoJrXDNpBNg3pZ4xBRyVhgmLmqBHaG', '1989-04-10', 'MASCULINO', '3245678901', 'Oficina Central', 'Armenia', 'Colombia', 'ADMIN', NOW(), 'ACTIVO');

-- =====================================================
-- 2. ESPECIALIDADES (8 registros)
-- =====================================================
INSERT INTO especialidades (nombre_especialidad, descripcion, estado) VALUES
(
   'Medicina General', 
   'Atención médica integral para diagnóstico, tratamiento y prevención de enfermedades comunes', 
   'ACTIVA'
),
('Cardiología', 'Especialidad dedicada al diagnóstico y tratamiento de enfermedades del corazón y sistema circulatorio', 'ACTIVA'),
('Pediatría', 'Atención médica especializada para la salud de bebés, niños y adolescentes', 'ACTIVA'),
('Dermatología', 'Diagnóstico y tratamiento de enfermedades de la piel, cabello y uñas', 'ACTIVA'),
('Ginecología', 'Especialidad enfocada en la salud del sistema reproductivo femenino', 'ACTIVA'),
('Psiquiatría', 'Diagnóstico y tratamiento de trastornos mentales y emocionales', 'ACTIVA'),
('Oftalmología', 'Especialidad dedicada a la salud visual y enfermedades de los ojos', 'ACTIVA'),
('Traumatología', 'Tratamiento de lesiones y enfermedades del sistema músculo-esquelético', 'ACTIVA');

-- =====================================================
-- 3. PACIENTES (5 registros)
-- =====================================================
INSERT INTO pacientes (id_usuario, numero_identificacion, tipo_sangre, alergias, enfermedades_cronicas, medicamentos_actuales, contacto_emergencia, telefono_emergencia) VALUES
(
   1, 
   '1094123456', 
   'O+', 
   'Penicilina', 
   'Ninguna', 
   'Ninguno', 
   'Pedro González (Esposo)', 
   '3001234567'
),
(2, '1094234567', 'A+', 'Polen, Ácaros', 'Asma', 'Salbutamol (Inhalador)', 'Carmen Pérez (Madre)', '3002345678'),
(3, '1094345678', 'B-', 'Ninguna conocida', 'Hipertensión', 'Losartán 50mg', 'José Martínez (Padre)', '3003456789'),
(4, '1094456789', 'AB+', 'Ibuprofeno', 'Diabetes tipo 2', 'Metformina 850mg', 'Rosa López (Esposa)', '3004567890'),
(5, '1094567890', 'O-', 'Mariscos', 'Ninguna', 'Anticonceptivos orales', 'Martha Rodríguez (Madre)', '3005678901');

-- =====================================================
-- 4. MÉDICOS (5 registros)
-- =====================================================
INSERT INTO medicos (id_usuario, numero_licencia, id_especialidad, hospital_afiliado, experiencia_anos, resumen_bio, calificacion_promedio, estado_verificacion, fecha_verificacion) VALUES
(
   6, 
   'MED-2015-001', 
   1, 
   'Hospital San Juan de Dios', 
   8, 
   'Médico general con amplia experiencia en atención primaria y medicina preventiva', 
   4.8, 
   'VERIFICADO', 
   '2024-01-15'
),
(7, 'MED-2012-045', 2, 'Clínica La Asunción', 11, 'Cardióloga especialista en insuficiencia cardíaca y arritmias', 4.9, 'VERIFICADO', '2024-01-20'),
(8, 'MED-2008-089', 3, 'Hospital Infantil Los Ángeles', 15, 'Pediatra con subespecialidad en neonatología y cuidados intensivos pediátricos', 4.7, 'VERIFICADO', '2024-02-01'),
(9, 'MED-2016-123', 4, 'Centro Dermatológico Armenia', 7, 'Dermatóloga especializada en dermatología estética y tratamientos láser', 4.6, 'VERIFICADO', '2024-02-10'),
(10, 'MED-2010-234', 7, 'Instituto de Oftalmología', 13, 'Oftalmólogo experto en cirugía refractiva y tratamiento de cataratas', 4.8, 'VERIFICADO', '2024-02-15');

-- =====================================================
-- 5. CALENDARIO DISPONIBILIDAD (15 registros)
-- =====================================================
-- Dr. Carlos Ramírez - Medicina General
INSERT INTO calendario_disponibilidad (id_medico, dia_semana, hora_inicio, hora_fin, intervalo_cita_minutos, estado) VALUES
(
   1, 
   'LUNES', 
   '08:00:00', 
   '12:00:00', 
   30, 
   'ACTIVO'
),
(1, 'LUNES', '14:00:00', '18:00:00', 30, 'ACTIVO'),
(1, 'MIERCOLES', '08:00:00', '12:00:00', 30, 'ACTIVO'),

-- Dra. Patricia Gómez - Cardiología
(
   2, 
   'MARTES', 
   '09:00:00', 
   '13:00:00', 
   45, 
   'ACTIVO'
),
(2, 'JUEVES', '09:00:00', '13:00:00', 45, 'ACTIVO'),
(2, 'VIERNES', '14:00:00', '17:00:00', 45, 'ACTIVO'),

-- Dr. Roberto Silva - Pediatría
(
   3, 
   'LUNES', 
   '08:00:00', 
   '12:00:00', 
   20, 
   'ACTIVO'
),
(3, 'MARTES', '08:00:00', '12:00:00', 20, 'ACTIVO'),
(3, 'MIERCOLES', '14:00:00', '18:00:00', 20, 'ACTIVO'),

-- Dra. Sofía Torres - Dermatología
(4, 'MARTES', '10:00:00', '14:00:00', 30, 'ACTIVO'),
(4, 'JUEVES', '10:00:00', '14:00:00', 30, 'ACTIVO'),
(4, 'VIERNES', '08:00:00', '12:00:00', 30, 'ACTIVO'),

-- Dr. Miguel Hernández - Oftalmología
(5, 'LUNES', '14:00:00', '18:00:00', 40, 'ACTIVO'),
(5, 'MIERCOLES', '09:00:00', '13:00:00', 40, 'ACTIVO'),
(5, 'VIERNES', '14:00:00', '18:00:00', 40, 'ACTIVO');

-- =====================================================
-- 6. CITAS (10 registros)
-- =====================================================
INSERT INTO citas (id_paciente, id_medico, fecha_cita, hora_cita, tipo_cita, motivo_consulta, estado, fecha_creacion) VALUES
-- Citas completadas
(
   1, 
   1, 
   '2024-10-01', 
   '08:30:00', 
   'PRESENCIAL', 
   'Consulta de control general', 
   'COMPLETADA', 
   '2024-09-25 10:00:00'
),
(2, 2, '2024-10-03', '09:00:00', 'PRESENCIAL', 'Dolor en el pecho y palpitaciones', 'COMPLETADA', '2024-09-28 14:30:00'),
(3, 3, '2024-10-05', '08:00:00', 'PRESENCIAL', 'Control de crecimiento del bebé', 'COMPLETADA', '2024-10-01 09:00:00'),
(4, 4, '2024-10-07', '10:30:00', 'TELEMEDICINA', 'Consulta dermatológica por acné', 'COMPLETADA', '2024-10-02 16:00:00'),
(5, 5, '2024-10-08', '14:00:00', 'PRESENCIAL', 'Examen de la vista y graduación', 'COMPLETADA', '2024-10-03 11:00:00'),

-- Citas programadas (futuras)
(
   1, 
   1, 
   '2025-10-20', 
   '09:00:00', 
   'PRESENCIAL', 
   'Seguimiento de resultados de laboratorio', 
   'PROGRAMADA', 
   NOW()
),
(2, 2, '2025-10-22', '10:30:00', 'PRESENCIAL', 'Control cardiológico mensual', 'PROGRAMADA', NOW()),
(3, 3, '2025-10-25', '08:30:00', 'PRESENCIAL', 'Vacunación programada', 'PROGRAMADA', NOW()),

-- Cita cancelada
(4, 1, '2024-10-12', '15:00:00', 'PRESENCIAL', 'Chequeo médico general', 'CANCELADA', '2024-10-08 10:00:00'),

-- Paciente no asistió
(5, 4, '2024-10-10', '11:00:00', 'TELEMEDICINA', 'Consulta de dermatología', 'NO_ASISTIO', '2024-10-05 13:00:00');

-- =====================================================
-- 7. HISTORIAL MÉDICO (5 registros - solo citas completadas)
-- =====================================================
INSERT INTO historial_medico (id_cita, id_paciente, id_medico, diagnostico, sintomas_reportados, tratamiento_recomendado, medicamentos_prescritos, observaciones, fecha_proximo_seguimiento) VALUES
(
   1, 
   1, 
   1, 
   'Estado de salud general bueno. Valores de presión arterial normales.', 
   'Ningún síntoma específico reportado', 
   'Mantener estilo de vida saludable, ejercicio regular y dieta balanceada', 
   'Ninguno', 
   'Paciente en buen estado general. Recomendar control anual.', 
   '2025-10-01'
),

(2, 2, 2, 'Arritmia sinusal leve. Sin signos de enfermedad cardíaca grave.', 
   'Dolor ocasional en el pecho, palpitaciones después de esfuerzo físico', 
   'Reducir consumo de cafeína, manejo del estrés, seguimiento con electrocardiograma', 
   'Atenolol 25mg - 1 vez al día', 
   'Se solicita Holter de 24 horas para seguimiento. Evitar bebidas estimulantes.', 
   '2024-11-03'),

(3, 3, 3, 'Desarrollo psicomotor adecuado para la edad. Crecimiento dentro de percentiles normales.', 
   'Desarrollo normal según edad', 
   'Continuar con lactancia materna complementaria, introducir alimentos sólidos gradualmente', 
   'Vitamina D - 400 UI diarias', 
   'Paciente pediátrico sano. Próxima cita para vacunación.', 
   '2024-11-05'),

(4, 4, 4, 'Acné vulgar moderado. Sin signos de infección bacteriana severa.', 
   'Lesiones de acné en rostro, principalmente en zona T', 
   'Limpieza facial con productos suaves, tratamiento tópico con retinoides', 
   'Tretinoína gel 0.025% - aplicar por las noches', 
   'Evitar manipular las lesiones. Usar protector solar diariamente.', 
   '2024-12-07'),

(5, 5, 5, 'Miopía leve bilateral. Agudeza visual corregible con lentes.', 
   'Visión borrosa de lejos, especialmente al conducir', 
   'Uso de lentes correctores permanente para actividades diarias', 
   'Ninguno', 
   'Se prescribieron lentes con fórmula: OD -1.50 OS -1.25. Control en 6 meses.', 
   '2025-04-08');

-- =====================================================
-- 8. PRESCRIPCIONES (7 registros)
-- =====================================================
INSERT INTO prescripciones (id_historial, nombre_medicamento, dosis, frecuencia, duracion_dias, instrucciones, fecha_prescripcion) VALUES
-- Historial 2 (Cardiología)
(
   2, 
   'Atenolol', 
   '25mg', 
   'Una vez al día', 
   30, 
   'Tomar en ayunas por la mañana con un vaso de agua', 
   '2024-10-03'
),

-- Historial 3 (Pediatría)
(3, 'Vitamina D (Colecalciferol)', '400 UI', 'Una vez al día', 90, 'Administrar 4 gotas diarias con las comidas', '2024-10-05'),

-- Historial 4 (Dermatología)
(4, 'Tretinoína gel', '0.025%', 'Una vez al día (noche)', 60, 'Aplicar una pequeña cantidad sobre rostro limpio y seco. Usar protector solar durante el día.', '2024-10-07'),
(4, 'Protector Solar SPF 50+', 'Tópico', 'Dos veces al día', 60, 'Aplicar generosamente cada 4 horas durante exposición solar', '2024-10-07'),

-- Historial 1 (Medicina General) - Suplementos
(1, 'Multivitamínico', '1 tableta', 'Una vez al día', 30, 'Tomar con el desayuno', '2024-10-01'),

-- Historial 2 adicional
(2, 'Ácido Acetilsalicílico (Aspirina)', '100mg', 'Una vez al día', 30, 'Tomar después del almuerzo para protección cardiovascular', '2024-10-03'),

-- Historial 5 (no requiere medicamentos, pero se puede agregar gotas)
(5, 'Lágrimas Artificiales', '1-2 gotas', 'Según necesidad', 60, 'Aplicar cuando sienta resequedad ocular, especialmente después de uso prolongado de pantallas', '2024-10-08');

-- =====================================================
-- 9. EVALUACIONES (6 registros)
-- =====================================================
INSERT INTO evaluaciones (id_paciente, id_medico, calificacion, comentario, fecha_evaluacion, respuesta_medico) VALUES
(
   1, 
   1, 
   5, 
   'Excelente atención, muy profesional y dedicado. Explicó todo de manera clara.', 
   '2024-10-02 10:30:00', 
   'Muchas gracias por sus amables palabras. Es un placer atenderle.'),

(2, 2, 5, 'La Dra. Gómez es excepcional. Me sentí muy bien atendido y sus explicaciones fueron muy claras.', 
   '2024-10-04 15:00:00', 
   'Agradezco mucho su confianza. Estoy para servirle en lo que necesite.'),

(3, 3, 4, 'Muy buen pediatra, paciente y cariñoso con los niños. El único detalle fue el tiempo de espera.', 
   '2024-10-06 09:45:00', 
   'Gracias por su retroalimentación. Trabajaremos en mejorar los tiempos de espera.'),

(4, 4, 5, 'Excelente doctora, muy profesional y me dio muy buenos consejos para mi piel.', 
   '2024-10-08 12:00:00', 
   'Me alegra poder ayudarle. No dude en contactarme si tiene alguna pregunta.'),

(5, 5, 5, 'Muy satisfecha con la atención. El doctor fue muy minucioso en el examen.', 
   '2024-10-09 16:30:00', 
   'Muchas gracias. Es importante realizar exámenes completos para un diagnóstico preciso.'),

(1, 1, 4, 'Segunda visita igualmente buena. Siempre muy atento.', 
   '2024-10-11 11:00:00', 
   NULL);

-- =====================================================
-- 10. NOTIFICACIONES (10 registros)
-- =====================================================
INSERT INTO notificaciones (id_usuario, tipo_notificacion, titulo, contenido, fecha_envio, leida, enlace_relacionado) VALUES
-- Notificaciones para pacientes
(
   1, 
   'CITA_CONFIRMADA', 
   'Cita Confirmada', 
   'Su cita con Dr. Carlos Ramírez ha sido confirmada para el 20/10/2025 a las 9:00 AM', 
   NOW() - INTERVAL 2 DAY, 
   true, 
   '/citas/6'
),
(1, 'RECORDATORIO', 'Recordatorio de Cita', 'Recuerde que tiene cita mañana a las 9:00 AM con Dr. Carlos Ramírez', NOW() - INTERVAL 1 DAY, true, '/citas/6'),

(2, 'CITA_CONFIRMADA', 'Cita Confirmada', 'Su cita con Dra. Patricia Gómez ha sido confirmada para el 22/10/2025 a las 10:30 AM', NOW() - INTERVAL 3 DAY, true, '/citas/7'),
(2, 'RESULTADO', 'Resultados Disponibles', 'Los resultados de su electrocardiograma ya están disponibles', NOW() - INTERVAL 5 DAY, false, '/historial/2'),

(3, 'RECORDATORIO', 'Próxima Vacunación', 'No olvide la cita de vacunación de su bebé el 25/10/2025', NOW() - INTERVAL 1 DAY, false, '/citas/8'),

(4, 'MENSAJE', 'Recomendaciones Dermatológicas', 'Recuerde aplicar el tratamiento todas las noches y usar protector solar', NOW() - INTERVAL 4 DAY, true, '/historial/4'),

(5, 'CITA_CONFIRMADA', 'Cita Confirmada', 'Sus lentes estarán listos en 7 días hábiles', NOW() - INTERVAL 6 DAY, true, NULL),

-- Notificaciones para médicos
(
   6, 
   'MENSAJE', 
   'Nueva Evaluación', 
   'Ha recibido una nueva evaluación de 5 estrellas de un paciente', 
   NOW() - INTERVAL 1 DAY, false, 
   '/evaluaciones'
),

(7, 'CITA_CONFIRMADA', 'Nueva Cita Agendada', 'Se ha agendado una nueva cita para el 22/10/2025', NOW() - INTERVAL 2 DAY, true, '/citas/7'),

(10, 'MENSAJE', 'Actualización del Sistema', 'El sistema tendrá mantenimiento programado este fin de semana', NOW() - INTERVAL 3 DAY, false, NULL);

-- =====================================================
-- RESUMEN DEL DATASET
-- =====================================================
-- Usuarios: 15 (5 pacientes, 5 médicos, 5 admins)
-- Especialidades: 8
-- Pacientes: 5
-- Médicos: 5
-- Calendario Disponibilidad: 15
-- Citas: 10
-- Historial Médico: 5
-- Prescripciones: 7
-- Evaluaciones: 6
-- Notificaciones: 10
-- =====================================================
-- TOTAL: Todas las tablas tienen ≥5 registros
-- =====================================================

-- Verificar conteo de registros
SELECT 'usuarios' as tabla, COUNT(*) as total FROM usuarios
UNION ALL
SELECT 'especialidades', COUNT(*) FROM especialidades
UNION ALL
SELECT 'pacientes', COUNT(*) FROM pacientes
UNION ALL
SELECT 'medicos', COUNT(*) FROM medicos
UNION ALL
SELECT 'calendario_disponibilidad', COUNT(*) FROM calendario_disponibilidad
UNION ALL
SELECT 'citas', COUNT(*) FROM citas
UNION ALL
SELECT 'historial_medico', COUNT(*) FROM historial_medico
UNION ALL
SELECT 'prescripciones', COUNT(*) FROM prescripciones
UNION ALL
SELECT 'evaluaciones', COUNT(*) FROM evaluaciones
UNION ALL
SELECT 'notificaciones', COUNT(*) FROM notificaciones;

package com.mediapp.citasbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediapp.citasbackend.entities.*;
import com.mediapp.citasbackend.services.interfaces.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Tests para CitaController")
class CitaControllerTest {

        private MockMvc mockMvc;

        @Mock
        private CitaService citaService;

        private CitaController citaController;

    private ObjectMapper objectMapper;
    private Cita cita;
    private Medico medico;
    private Paciente paciente;
    private Usuario usuarioMedico;
    private Usuario usuarioPaciente;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        citaController = new CitaController(citaService);
        mockMvc = MockMvcBuilders.standaloneSetup(citaController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Configurar especialidad
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        // Configurar usuario médico
        usuarioMedico = new Usuario();
        usuarioMedico.setIdUsuario(1);
        usuarioMedico.setEmail("medico@test.com");
        usuarioMedico.setNombre("Juan");
        usuarioMedico.setApellido("Pérez");
        usuarioMedico.setTipoUsuario(Usuario.TipoUsuario.MEDICO);

        // Configurar médico
        medico = new Medico();
        medico.setIdMedico(1);
        medico.setUsuario(usuarioMedico);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);

        // Configurar usuario paciente
        usuarioPaciente = new Usuario();
        usuarioPaciente.setIdUsuario(2);
        usuarioPaciente.setEmail("paciente@test.com");
        usuarioPaciente.setNombre("María");
        usuarioPaciente.setApellido("García");
        usuarioPaciente.setTipoUsuario(Usuario.TipoUsuario.PACIENTE);

        // Configurar paciente
        paciente = new Paciente();
        paciente.setIdPaciente(1);
        paciente.setUsuario(usuarioPaciente);
        paciente.setNumeroIdentificacion("12345678");
        paciente.setTipoSangre("O+");

        // Configurar cita
        cita = new Cita();
        cita.setIdCita(1);
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setFechaCita(LocalDate.now().plusDays(2));
        cita.setHoraCita(LocalTime.of(10, 0));
        cita.setTipoCita(Cita.TipoCita.PRESENCIAL);
        cita.setMotivoConsulta("Consulta de control");
        cita.setEstado(Cita.Estado.PROGRAMADA);
    }

    @Test
    @DisplayName("POST /api/citas - Debe crear una cita exitosamente")
    void testCrearCita_Exitoso() throws Exception {
        // Arrange
        when(citaService.guardarCita(any(Cita.class))).thenReturn(cita);

        // Act & Assert
        mockMvc.perform(post("/api/citas")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().is2xxSuccessful());

        verify(citaService, times(1)).guardarCita(any(Cita.class));
    }

    @Test
    @DisplayName("POST /api/citas - Debe retornar 400 con datos inválidos")
    void testCrearCita_DatosInvalidos() throws Exception {
        // Arrange
        when(citaService.guardarCita(any(Cita.class)))
                .thenThrow(new IllegalArgumentException("Datos inválidos"));

        // Act & Assert
        mockMvc.perform(post("/api/citas")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isBadRequest());

        verify(citaService, times(1)).guardarCita(any(Cita.class));
    }

    @Test
    @DisplayName("GET /api/citas - Debe obtener todas las citas")
    void testObtenerTodasLasCitas() throws Exception {
        // Arrange
        List<Cita> citas = Arrays.asList(cita, new Cita());
        when(citaService.obtenerTodasLasCitas()).thenReturn(citas);

        // Act & Assert
        mockMvc.perform(get("/api/citas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(citaService, times(1)).obtenerTodasLasCitas();
    }

    @Test
    @DisplayName("GET /api/citas/{id} - Debe obtener una cita por ID")
    void testObtenerCitaPorId_Exitoso() throws Exception {
        // Arrange
        when(citaService.obtenerCitaPorId(anyInt())).thenReturn(Optional.of(cita));

        // Act & Assert
        mockMvc.perform(get("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(citaService, times(1)).obtenerCitaPorId(1);
    }

    @Test
    @DisplayName("GET /api/citas/{id} - Debe retornar 404 cuando no existe la cita")
    void testObtenerCitaPorId_NoExiste() throws Exception {
        // Arrange
        when(citaService.obtenerCitaPorId(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/citas/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(citaService, times(1)).obtenerCitaPorId(999);
    }

    @Test
    @DisplayName("PUT /api/citas/{id} - Debe actualizar una cita exitosamente")
    void testActualizarCita_Exitoso() throws Exception {
        // Arrange
        cita.setMotivoConsulta("Consulta actualizada");
        when(citaService.actualizarCita(anyInt(), any(Cita.class))).thenReturn(cita);

        // Act & Assert
        mockMvc.perform(put("/api/citas/1")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isOk());

        verify(citaService, times(1)).actualizarCita(anyInt(), any(Cita.class));
    }

    @Test
    @DisplayName("PUT /api/citas/{id} - Debe retornar 404 cuando no existe la cita")
    void testActualizarCita_NoExiste() throws Exception {
        // Arrange
        when(citaService.actualizarCita(anyInt(), any(Cita.class)))
                .thenThrow(new IllegalArgumentException("Cita no encontrada"));

        // Act & Assert
        mockMvc.perform(put("/api/citas/999")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cita)))
                .andExpect(status().isNotFound());

        verify(citaService, times(1)).actualizarCita(anyInt(), any(Cita.class));
    }

    @Test
    @DisplayName("DELETE /api/citas/{id} - Debe eliminar una cita exitosamente")
    void testEliminarCita_Exitoso() throws Exception {
        // Arrange
        doNothing().when(citaService).eliminarCita(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/api/citas/1")
                        
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(citaService, times(1)).eliminarCita(1);
    }

    @Test
    @DisplayName("DELETE /api/citas/{id} - Debe retornar 404 cuando no existe la cita")
    void testEliminarCita_NoExiste() throws Exception {
        // Arrange
        doThrow(new IllegalArgumentException("Cita no encontrada"))
                .when(citaService).eliminarCita(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/api/citas/999")
                        
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(citaService, times(1)).eliminarCita(999);
    }

    @Test
    @DisplayName("GET /api/citas/paciente/{idPaciente} - Debe obtener citas por paciente")
    void testObtenerCitasPorPaciente() throws Exception {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaService.obtenerCitasPorPacienteId(anyInt())).thenReturn(citas);

        // Act & Assert
        mockMvc.perform(get("/api/citas/paciente/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(citaService, times(1)).obtenerCitasPorPacienteId(1);
    }

    @Test
    @DisplayName("GET /api/citas/medico/{idMedico} - Debe obtener citas por médico")
    void testObtenerCitasPorMedico() throws Exception {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaService.obtenerCitasPorMedicoId(anyInt())).thenReturn(citas);

        // Act & Assert
        mockMvc.perform(get("/api/citas/medico/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(citaService, times(1)).obtenerCitasPorMedicoId(1);
    }

    @Test
    @DisplayName("GET /api/citas/estado/{estado} - Debe obtener citas por estado")
    void testObtenerCitasPorEstado() throws Exception {
        // Arrange
        List<Cita> citas = Arrays.asList(cita);
        when(citaService.obtenerCitasPorEstado(any(Cita.Estado.class))).thenReturn(citas);

        // Act & Assert
        mockMvc.perform(get("/api/citas/estado/PROGRAMADA")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(citaService, times(1)).obtenerCitasPorEstado(Cita.Estado.PROGRAMADA);
    }
}

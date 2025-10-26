package com.mediapp.citasbackend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mediapp.citasbackend.entities.*;
import com.mediapp.citasbackend.services.interfaces.MedicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("Tests para MedicoController")
class MedicoControllerTest {

    private MockMvc mockMvc;

    
    @Mock
    private MedicoService medicoService;

    private MedicoController medicoController;

    private ObjectMapper objectMapper;
    private Medico medico;
    private Usuario usuario;
    private Especialidad especialidad;

    @BeforeEach
    void setUp() {
    MockitoAnnotations.openMocks(this);

    medicoController = new MedicoController(medicoService);
    mockMvc = MockMvcBuilders.standaloneSetup(medicoController).build();

    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

        // Configurar especialidad
        especialidad = new Especialidad();
        especialidad.setIdEspecialidad(1);
        especialidad.setNombreEspecialidad("Cardiología");

        // Configurar usuario
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setEmail("medico@test.com");
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setTipoUsuario(Usuario.TipoUsuario.MEDICO);

        // Configurar médico
        medico = new Medico();
        medico.setIdMedico(1);
        medico.setUsuario(usuario);
        medico.setNumeroLicencia("LIC123456");
        medico.setEspecialidad(especialidad);
        medico.setHospitalAfiliado("Hospital Central");
        medico.setExperienciaAnos(10);
        medico.setCalificacionPromedio(new BigDecimal("4.5"));
        medico.setEstadoVerificacion(Medico.EstadoVerificacion.VERIFICADO);
        medico.setFechaVerificacion(LocalDate.now());
    }

    @Test
    @DisplayName("GET /api/medicos - Debe obtener todos los médicos")
    void testObtenerTodosLosMedicos() throws Exception {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico, new Medico());
        when(medicoService.obtenerTodosLosMedicos()).thenReturn(medicos);

        // Act & Assert
        mockMvc.perform(get("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Content: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isOk());

        verify(medicoService, times(1)).obtenerTodosLosMedicos();
    }

    @Test
    @DisplayName("GET /api/medicos/{id} - Debe obtener un médico por ID")
    void testObtenerMedicoPorId_Exitoso() throws Exception {
        // Arrange
        when(medicoService.obtenerMedicoPorId(anyInt())).thenReturn(Optional.of(medico));

        // Act & Assert
        mockMvc.perform(get("/api/medicos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(medicoService, times(1)).obtenerMedicoPorId(1);
    }

    @Test
    @DisplayName("GET /api/medicos/{id} - Debe retornar 404 cuando no existe el médico")
    void testObtenerMedicoPorId_NoExiste() throws Exception {
        // Arrange
        when(medicoService.obtenerMedicoPorId(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/medicos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(medicoService, times(1)).obtenerMedicoPorId(999);
    }

    @Test
    @DisplayName("POST /api/medicos - Debe crear un médico exitosamente")
    void testCrearMedico_Exitoso() throws Exception {
        // Arrange
        when(medicoService.guardarMedico(any(Medico.class))).thenReturn(medico);

        // Act & Assert
        mockMvc.perform(post("/api/medicos")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().is2xxSuccessful());

        verify(medicoService, times(1)).guardarMedico(any(Medico.class));
    }

    @Test
    @DisplayName("PUT /api/medicos/{id} - Debe actualizar un médico exitosamente")
    void testActualizarMedico_Exitoso() throws Exception {
        // Arrange
        medico.setHospitalAfiliado("Hospital Actualizado");
        when(medicoService.actualizarMedico(anyInt(), any(Medico.class))).thenReturn(medico);

        // Act & Assert
        mockMvc.perform(put("/api/medicos/1")
                        
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isOk());

        verify(medicoService, times(1)).actualizarMedico(anyInt(), any(Medico.class));
    }

    @Test
    @DisplayName("DELETE /api/medicos/{id} - Debe eliminar un médico exitosamente")
    void testEliminarMedico_Exitoso() throws Exception {
        // Arrange
        doNothing().when(medicoService).eliminarMedico(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/api/medicos/1")
                        
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(medicoService, times(1)).eliminarMedico(1);
    }

    @Test
    @DisplayName("GET /api/medicos/especialidad/{idEspecialidad} - Debe obtener médicos por especialidad")
    void testObtenerMedicosPorEspecialidad() throws Exception {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.obtenerMedicosPorEspecialidadId(anyInt())).thenReturn(medicos);

        // Act & Assert
        mockMvc.perform(get("/api/medicos/especialidad/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(medicoService, times(1)).obtenerMedicosPorEspecialidadId(1);
    }

    @Test
    @DisplayName("GET /api/medicos/verificados - Debe obtener médicos verificados")
    void testObtenerMedicosVerificados() throws Exception {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.obtenerMedicosVerificados()).thenReturn(medicos);

        // Act & Assert
        mockMvc.perform(get("/api/medicos/verificados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(medicoService, times(1)).obtenerMedicosVerificados();
    }

    @Test
    @DisplayName("GET /api/medicos/buscar - Debe buscar médicos por nombre")
    void testBuscarMedicosPorNombre() throws Exception {
        // Arrange
        List<Medico> medicos = Arrays.asList(medico);
        when(medicoService.buscarMedicosPorNombre(anyString())).thenReturn(medicos);

        // Act & Assert
        mockMvc.perform(get("/api/medicos/buscar")
                        .param("nombre", "Juan")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(medicoService, times(1)).buscarMedicosPorNombre("Juan");
    }
}

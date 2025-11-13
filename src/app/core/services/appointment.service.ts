import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin, of, throwError } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { AuthService } from './auth.service';

export interface Appointment {
    idCita?: number;
    fechaCita: string;
    horaCita: string;
    tipoCita: string;
    motivoConsulta: string;
    estado?: string;
    paciente: { idPaciente: number };
    medico: { idMedico: number };
    enlaceVideollamada?: string;
}

@Injectable({
    providedIn: 'root'
})
export class AppointmentService {

    private readonly BASE_URL = 'https://backendmedia-app-production.up.railway.app/api';

    private readonly API_URL = `${this.BASE_URL}/citas`;
    private readonly API_PACIENTES = `${this.BASE_URL}/pacientes`;
    private readonly API_MEDICOS = `${this.BASE_URL}/medicos`;

    constructor(private http: HttpClient, private authService: AuthService) { }

    // --- Nuevos métodos para resolver IDs ---
    /**
     * Obtiene el paciente (entidad) por idUsuario.
     * Endpoint esperado: GET /api/pacientes/usuario/{idUsuario}
     * Devuelve la entidad Paciente (al menos debe contener idPaciente).
     */
    getPacienteByUsuario(idUsuario: number): Observable<any> {
        return this.http.get<any>(`${this.API_PACIENTES}/usuario/${idUsuario}`);
    }

    /**
     * Obtiene el médico (entidad) por idUsuario.
     * Endpoint esperado: GET /api/medicos/usuario/{idUsuario}
     * Devuelve la entidad Medico (al menos debe contener idMedico).
     */
    getMedicoByUsuario(idUsuario: number): Observable<any> {
        return this.http.get<any>(`${this.API_MEDICOS}/usuario/${idUsuario}`);
    }

    /**
     * Crea una cita resolviendo automáticamente idMedico / idPaciente.
     *
     * - Acepta que en el payload medico/paciente venga:
     *     { idMedico } OR { idUsuario }  (igual para paciente)
     * - Si ya vienen idMedico/idPaciente los usa directamente.
     * - Si vienen idUsuario, llama a los endpoints de paciente/medico por usuario para obtener el id real.
     *
     * Ejemplo de payload esperado (parcial):
     * {
     *   fechaCita, horaCita, tipoCita, motivoConsulta,
     *   medico: { idUsuario: 9 }  // o medico: { idMedico: 3 }
     *   paciente: { idUsuario: 5 } // o paciente: { idPaciente: 7 }
     * }
     */
    createAppointmentAuto(rawCita: any): Observable<Appointment> {
        // Preparar observables para resolver medico/paciente
        const medicoObs = ((): Observable<any> => {
            if (!rawCita.medico) return of(null);
            if (rawCita.medico.idMedico != null) {
                // ya tenemos idMedico
                return of({ idMedico: Number(rawCita.medico.idMedico) });
            }
            if (rawCita.medico.idUsuario != null) {
                return this.getMedicoByUsuario(Number(rawCita.medico.idUsuario));
            }
            return of(null);
        })();

        const pacienteObs = ((): Observable<any> => {
            if (!rawCita.paciente) return of(null);
            if (rawCita.paciente.idPaciente != null) {
                // ya tenemos idPaciente
                return of({ idPaciente: Number(rawCita.paciente.idPaciente) });
            }
            if (rawCita.paciente.idUsuario != null) {
                return this.getPacienteByUsuario(Number(rawCita.paciente.idUsuario));
            }
            return of(null);
        })();

        return forkJoin([medicoObs, pacienteObs]).pipe(
            switchMap(([medicoResolved, pacienteResolved]) => {
                // Validaciones mínimas
                if (!medicoResolved || medicoResolved.idMedico == null) {
                    return throwError(() => new Error('No se pudo resolver idMedico. Asegúrate que médico existe o envía idMedico.'));
                }
                if (!pacienteResolved || pacienteResolved.idPaciente == null) {
                    return throwError(() => new Error('No se pudo resolver idPaciente. Asegúrate que paciente existe o envía idPaciente.'));
                }

                // Construir payload final con los ids resueltos
                const finalCita: Appointment = {
                    fechaCita: rawCita.fechaCita,
                    horaCita: rawCita.horaCita,
                    tipoCita: rawCita.tipoCita,
                    motivoConsulta: rawCita.motivoConsulta,
                    paciente: { idPaciente: Number(pacienteResolved.idPaciente) },
                    medico: { idMedico: Number(medicoResolved.idMedico) },
                    enlaceVideollamada: rawCita.enlaceVideollamada
                };

                console.log('📤 Enviando nueva cita al backend:', finalCita);
                return this.http.post<Appointment>(this.API_URL, finalCita);
            })
        );
    }

    // --- Métodos existentes (sin cambios) ---
    getAppointmentsByPatient(): Observable<Appointment[]> {
        const idPaciente = this.authService.getUserId();
        return this.http.get<Appointment[]>(`${this.API_URL}/paciente/${idPaciente}`);
    }

    getAppointmentsByDoctor(idMedico: number): Observable<Appointment[]> {
        return this.http.get<Appointment[]>(`${this.API_URL}/medico/${idMedico}`);
    }

    createAppointment(cita: Appointment): Observable<Appointment> {
        console.log('📤 Enviando nueva cita al backend:', cita);
        return this.http.post<Appointment>(this.API_URL, cita);
    }

    cancelAppointment(idCita: number): Observable<void> {
        return this.http.put<void>(`${this.API_URL}/${idCita}/cancelar`, {});
    }

    confirmAppointment(idCita: number): Observable<void> {
        return this.http.put<void>(`${this.API_URL}/${idCita}/confirmar`, {});
    }

    deleteAppointment(idCita: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${idCita}`);
    }

    getAllMedicos(): Observable<any[]> {
        return this.http.get<any[]>(`${this.BASE_URL}/medicos`);
    }

    getMedicosActivos(): Observable<any[]> {
        return this.http.get<any[]>(`${this.BASE_URL}/usuarios/activos/tipo/MEDICO`);
    }

    // -------------------------------------------------------------
    // 👨‍⚕️ NUEVOS MÉTODOS PARA DASHBOARD DEL DOCTOR
    // -------------------------------------------------------------

    /** Obtener citas del médico filtradas por estado */
    getCitasPorMedicoYEstado(idMedico: number, estado: string): Observable<any[]> {
        return this.http.get<any[]>(`${this.API_URL}/medico/${idMedico}/estado/${estado}`);
    }

    /** Marcar cita como completada */
    completarCita(idCita: number): Observable<any> {
        return this.http.patch(`${this.API_URL}/${idCita}/completar`, {});
    }

    /** Cancelar cita */
    cancelarCita(idCita: number): Observable<any> {
        return this.http.patch(`${this.API_URL}/${idCita}/cancelar`, {});
    }

    /** Marcar cita como no asistió */
    marcarNoAsistio(idCita: number): Observable<any> {
        return this.http.patch(`${this.API_URL}/${idCita}/no-asistio`, {});
    }

    /** Iniciar atención (marcar cita como EN_CURSO) */
    iniciarAtencion(idCita: number): Observable<any> {
        return this.http.patch(`${this.API_URL}/${idCita}/iniciar`, {});
    }


}

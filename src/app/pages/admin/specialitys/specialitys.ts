import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SpecialitysService, Especialidad } from '../../../core/services/specialitys.service';

@Component({
  selector: 'admin-specialitys',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './specialitys.html',
  styleUrls: ['./specialitys.scss']
})
export class AdminSpecialitys implements OnInit {

  specialities: Especialidad[] = [];
  loading = true;
  errorMessage: string | null = null;
  nuevaEspecialidad: Partial<Especialidad> = { nombreEspecialidad: '', descripcion: '' };

  constructor(
    private spService: SpecialitysService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;
    this.errorMessage = null;

    this.spService.getAll().subscribe({
      next: (data) => {
        console.log('📦 Especialidades recibidas:', data);
        this.specialities = data;
        this.loading = false;

        // 🔄 Forzar actualización visual después de recibir los datos
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('❌ Error cargando especialidades', err);
        this.errorMessage = 'No se pudieron cargar las especialidades.';
        this.loading = false;
      }
    });
  }

  refreshAfter(action: string) {
    console.log(`🔄 Refrescando lista tras ${action}...`);
    this.load();

    // 👀 Espera un instante y luego fuerza el cambio visual
    setTimeout(() => this.cdr.detectChanges(), 150);
  }

  create() {
    console.log('➕ Creando nueva especialidad...', this.nuevaEspecialidad);
    if (!this.nuevaEspecialidad.nombreEspecialidad?.trim()) {
      alert('Debes ingresar un nombre para la especialidad.');
      return;
    }

    const payload: Especialidad = {
      idEspecialidad: 0,
      nombreEspecialidad: this.nuevaEspecialidad.nombreEspecialidad.trim(),
      descripcion: this.nuevaEspecialidad.descripcion || '',
      estado: 'ACTIVA'
    };
    
    this.spService.create(payload).subscribe({
      next: () => {
        alert('✅ Especialidad creada correctamente.');
        this.nuevaEspecialidad = { nombreEspecialidad: '', descripcion: '' };
        this.refreshAfter('crear');
      },
      error: (err) => {
        console.error('❌ Error creando especialidad', err);
        alert('No se pudo crear la especialidad.');
      }
    });
  }

  remove(id: number) {
    if (!confirm('¿Eliminar esta especialidad?')) return;

    this.spService.delete(id).subscribe({
      next: () => {
        alert('🗑️ Especialidad eliminada.');
        this.refreshAfter('eliminar');
      },
      error: (err) => {
        console.error('❌ Error eliminando especialidad', err);
        alert('No se pudo eliminar la especialidad.');
      }
    });
  }

  toggleState(s: Especialidad) {
    const toggle = s.estado === 'ACTIVA'
      ? this.spService.deactivate(s.idEspecialidad)
      : this.spService.activate(s.idEspecialidad);

    toggle.subscribe({
      next: (res) => {
        s.estado = res.estado;
        this.refreshAfter('cambiar estado');
      },
      error: (err) => {
        console.error('❌ Error cambiando estado', err);
        alert('No se pudo actualizar el estado.');
      }
    });
  }
}

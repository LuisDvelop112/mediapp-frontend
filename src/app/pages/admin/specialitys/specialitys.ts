import { Component, OnInit } from '@angular/core';
import { SpecialitysService } from '../../../core/services/specialitys.service';
import { CommonModule } from '@angular/common';

type Especialidad = {
  idEspecialidad: number;
  nombreEspecialidad: string;
  descripcion?: string;
  estado: 'ACTIVA' | 'INACTIVA';
};

@Component({
  selector: 'admin-specialitys',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './specialitys.html',
  styleUrls: ['./specialitys.scss']
})
export class AdminSpecialitys implements OnInit {

  specialities: Especialidad[] = [];
  loading = true;

  constructor(private spService: SpecialitysService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.loading = true;

    this.spService.getAll().subscribe({
      next: (data: Especialidad[]) => {
        this.specialities = data;
        this.loading = false;
      },
      error: (err: any) => {
        console.log('Error cargando especialidades', err);
        this.loading = false;
      }
    });
  }

  // ✅ LO QUE FALTABA
  remove(id: number) {
    if (!confirm('¿Eliminar especialidad?')) return;

    this.spService.delete(id).subscribe({
      next: () => {
        this.specialities = this.specialities.filter(s => s.idEspecialidad !== id);
      },
      error: (err: any) => {
        console.error('Error eliminando especialidad', err);
      }
    });
  }

  toggleState(s: Especialidad) {
    if (s.estado === 'ACTIVA') {
      this.spService.deactivate(s.idEspecialidad).subscribe((res: Especialidad) => {
        s.estado = res.estado;
      });
    } else {
      this.spService.activate(s.idEspecialidad).subscribe((res: Especialidad) => {
        s.estado = res.estado;
      });
    }
  }
}

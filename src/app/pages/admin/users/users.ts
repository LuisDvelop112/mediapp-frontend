import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UsersService } from '../../../core/services/usuario.service';

@Component({
  selector: 'admin-users',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './users.html',
  styleUrls: ['./users.scss']
})
export class AdminUsers implements OnInit {
  users: any[] = [];
  loading = true;
  message = '';

  constructor(private usersService: UsersService, private cdr: ChangeDetectorRef) { }

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.message = '';

    this.usersService.getAll().subscribe({
      next: (data: any) => {
        console.log('✅ Pacientes recibidos:', data);
        this.users = data;
        this.loading = false;
        this.message = `✅ Se cargaron ${data.length} pacientes.`;

        // 👇 fuerza actualización de vista
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('❌ Error cargando pacientes:', err);
        this.message = '❌ Error al cargar los pacientes.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }


  remove(id: number) {
    if (!confirm('¿Eliminar este paciente? Esta acción no se puede deshacer.')) return;

    this.usersService.delete(id).subscribe({
      next: () => {
        this.users = this.users.filter(u => u.idPaciente !== id);
        this.message = '✅ Paciente eliminado correctamente.';
      },
      error: (err) => {
        console.error('❌ Error al eliminar paciente:', err);
        this.message = 'Error al eliminar el paciente.';
      }
    });
  }
}

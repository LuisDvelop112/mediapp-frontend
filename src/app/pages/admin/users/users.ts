import { Component, OnInit } from '@angular/core';
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

  constructor(private usersService: UsersService) {}

  ngOnInit() {
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.usersService.getAll().subscribe({
      next: (data: any[]) => {
        this.users = data;
        this.loading = false;
      },
      error: (err: any) => {
        console.error('Error cargando pacientes', err);
        this.loading = false;
        this.message = 'Error al cargar la lista de pacientes.';
      }
    });
  }

  remove(id: number) {
    if (!confirm('¿Eliminar este usuario?')) return;

    this.usersService.delete(id).subscribe({
      next: () => {
        this.users = this.users.filter(u => u.idPaciente !== id);
        this.message = '🗑️ Paciente eliminado correctamente.';
      },
      error: (err) => {
        console.error('Error eliminando usuario', err);
        this.message = '❌ No se pudo eliminar el paciente.';
      }
    });
  }

  openForm(paciente?: any) {
    // Aquí luego puedes abrir un modal o navegar a otra ruta de edición
    if (paciente) {
      console.log('Editar paciente:', paciente);
    } else {
      console.log('Crear nuevo paciente');
    }
  }
}

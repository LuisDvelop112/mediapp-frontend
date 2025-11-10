import { Component } from '@angular/core';
import { RouterModule, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar {

  constructor(
    private router: Router,
    public authService: AuthService
  ) {}

  logout() {
    this.authService.logout();  // limpia: token, rol, user info
    this.router.navigate(['/landing/home']);
  }
}

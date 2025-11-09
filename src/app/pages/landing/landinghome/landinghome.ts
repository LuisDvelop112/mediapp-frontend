import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Login } from '../../auth/login/login';
import { Register } from '../../auth/register/register';

@Component({
  selector: 'app-landinghome',
  standalone: true,
  imports: [CommonModule, Login, Register],
  templateUrl: './landinghome.html',
  styleUrl: './landinghome.scss'
})
export class Landinghome {
  activeForm: 'login' | 'register' = 'login';

  setActiveForm(form: 'login' | 'register') {
    this.activeForm = form;
  }
}

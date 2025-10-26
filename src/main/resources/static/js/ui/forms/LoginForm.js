/**
 * Formulario de login
 */

import { AuthService } from '../../services/AuthService.js';

export class LoginForm {

  constructor(navigation) {
    this.navigation = navigation;
    this.formId = 'formLogin';
    this.init();
  }

  init() {
    const form = document.getElementById(this.formId);
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const v = (id) => document.getElementById(id).value.trim();
      const loginResult = document.getElementById('loginResult');
      const btnLoginSubmit = document.getElementById('btnLoginSubmit');

      if (loginResult) {
        loginResult.textContent = 'Iniciando sesión…';
        loginResult.style.color = '#666';
      }
      if (btnLoginSubmit) btnLoginSubmit.disabled = true;

      try {
        await AuthService.login(v('login_dni'), v('login_email'));

        if (loginResult) {
          loginResult.textContent = '✅ Sesión iniciada';
          loginResult.style.color = 'green';
        }

        setTimeout(() => {
          this.navigation.navigateTo('form');
          form.reset();
        }, 400);
      } catch (err) {
        if (loginResult) {
          loginResult.textContent = '❌ ' + err.message;
          loginResult.style.color = 'red';
        }
      } finally {
        if (btnLoginSubmit) btnLoginSubmit.disabled = false;
      }
    });
  }
}
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

      const loginResult = document.getElementById('loginResult');
      const btnLoginSubmit = document.getElementById('btnLoginSubmit');

      // Obtener valores
      const dni = document.getElementById('login_dni').value.trim();
      const email = document.getElementById('login_email').value.trim();

      // Mostrar loading
      if (loginResult) {
        loginResult.textContent = 'Iniciando sesión…';
        loginResult.style.color = '#666';
        loginResult.classList.remove('form__message--error', 'form__message--success');
      }
      if (btnLoginSubmit) btnLoginSubmit.disabled = true;

      try {
        // Realizar login
        await AuthService.login(dni, email);

        // Mostrar éxito
        if (loginResult) {
          loginResult.textContent = '✅ Sesión iniciada correctamente';
          loginResult.style.color = 'green';
          loginResult.classList.add('form__message--success');
        }

        // Limpiar formulario
        form.reset();

        // ✨ SOLUCIÓN 1: Disparar evento para actualizar UI
        window.dispatchEvent(new Event('authChange'));

        // ✨ SOLUCIÓN 2: Ir al MAPA después del login (no al formulario)
        setTimeout(() => {
          this.navigation.navigateTo('mapa');
        }, 500);

      } catch (err) {
        // Mostrar error
        if (loginResult) {
          loginResult.textContent = '❌ ' + err.message;
          loginResult.style.color = 'red';
          loginResult.classList.add('form__message--error');
        }
      } finally {
        if (btnLoginSubmit) btnLoginSubmit.disabled = false;
      }
    });
  }
}
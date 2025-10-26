/**
 * Formulario de registro de desaparecidos
 */

import { AuthService } from '../../services/AuthService.js';
import { DesaparecidoService } from '../../services/DesaparecidoService.js';

export class DesaparecidoForm {

  constructor(navigation) {
    this.navigation = navigation;
    this.formId = 'formDesaparecido';
    this.init();
  }

  init() {
    const form = document.getElementById(this.formId);
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const resultado = document.getElementById('resultado');
      const btnSubmit = document.getElementById('btnSubmit');

      // Validar autenticación
      if (!AuthService.getCurrentUser()) {
        if (resultado) {
          resultado.textContent = '❌ Debés iniciar sesión primero.';
          resultado.style.color = 'red';
        }
        this.navigation.navigateTo('login');
        const lm = document.getElementById('loginMessage');
        if (lm) lm.textContent = 'Tu sesión no está activa. Iniciá sesión para registrar un desaparecido.';
        return;
      }

      const v = (id) => document.getElementById(id).value.trim();

      if (!form.checkValidity()) {
        if (resultado) {
          resultado.textContent = '❌ Revisá los campos en rojo.';
          resultado.style.color = 'red';
        }
        return;
      }

      const body = {
        nombre: v('nombre'),
        apellido: v('apellido'),
        edad: parseInt(v('edad'), 10),
        dni: v('dni'),
        descripcion: v('descripcion'),
        fotoUrl: v('fotoUrl') || 'https://via.placeholder.com/150',
      };

      if (resultado) {
        resultado.textContent = 'Enviando…';
        resultado.style.color = '#666';
      }
      if (btnSubmit) btnSubmit.disabled = true;

      try {
        const creado = await DesaparecidoService.crear(body);

        if (resultado) {
          resultado.textContent = `✅ Persona registrada con ID: ${creado.id}`;
          resultado.style.color = 'green';
        }

        form.reset();

        setTimeout(() => {
          this.navigation.navigateTo('list');
          window.dispatchEvent(new Event('loadList'));
        }, 800);
      } catch (err) {
        if (resultado) {
          resultado.textContent = '❌ ' + err.message;
          resultado.style.color = 'red';
        }
      } finally {
        if (btnSubmit) btnSubmit.disabled = false;
      }
    });
  }
}
/**
 * Formulario de registro de avistadores
 */

import { AuthService } from '../../services/AuthService.js';
import { AvistadorService } from '../../services/AvistadorService.js';

export class AvistadorForm {

  constructor(navigation) {
    this.navigation = navigation;
    this.formId = 'formAvistador';
    this.init();
  }

  init() {
    const form = document.getElementById(this.formId);
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const out = document.getElementById('outAvistador');
      const btn = document.getElementById('btnAvistador');
      const v = (id) => document.getElementById(id).value.trim();

      if (!form.checkValidity()) {
        if (out) {
          out.textContent = '❌ Revisá los campos.';
          out.style.color = 'red';
        }
        return;
      }

      const body = {
        dni: v('a_dni'),
        nombre: v('a_nombre'),
        apellido: v('a_apellido'),
        edad: parseInt(v('a_edad'), 10),
        direccion: v('a_direccion'),
        email: v('a_email') || null,
        telefono: v('a_telefono') || null,
      };

      if (out) {
        out.textContent = 'Enviando…';
        out.style.color = '#666';
      }
      if (btn) btn.disabled = true;

      try {
        const dto = await AvistadorService.crear(body);

        if (out) {
          out.textContent = `✅ Avistador registrado. ID: ${dto.id}`;
          out.style.color = 'green';
        }

        form.reset();

        // El backend setea cookie → traemos sesión
        await AuthService.checkAuth();

        setTimeout(() => {
          if (out) {
            out.textContent = '✅ Ya estás registrado y logueado. Ahora podés registrar desaparecidos.';
          }
          this.navigation.navigateTo('form');
        }, 600);
      } catch (err) {
        if (out) {
          out.textContent = '❌ ' + err.message;
          out.style.color = 'red';
        }
      } finally {
        if (btn) btn.disabled = false;
      }
    });
  }
}
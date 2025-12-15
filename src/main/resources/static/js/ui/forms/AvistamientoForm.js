/**
 * Formulario de avistamientos (modal)
 */

export class AvistamientoForm {

  constructor(mapManager) {
    this.mapManager = mapManager;
    this.formId = 'formAvistamiento';
    this.init();
  }

  init() {
    const form = document.getElementById(this.formId);
    if (!form) return;

    form.addEventListener('submit', async (e) => {
      e.preventDefault();

      const v = (id) => document.getElementById(id).value.trim();

      // nombre mostrado del select (para el popup inmediato)
      const selectDesaparecido = document.getElementById('av_desaparecido');
      const desaparecidoNombre = selectDesaparecido.options[selectDesaparecido.selectedIndex]?.text || '';

      const formData = {
        desaparecidoId: v('av_desaparecido'),
        descripcion: v('av_descripcion'),
        fotoUrl: v('av_foto'),
        publico: document.getElementById('av_publico').checked,
        desaparecidoNombre,
      };

      if (!formData.desaparecidoId) {
        const result = document.getElementById('avistamientoResult');
        if (result) {
          result.textContent = '❌ Debes seleccionar una persona desaparecida';
          result.style.color = 'red';
        }
        return;
      }

      await this.mapManager.submitAvistamiento(formData);
    });
  }
}
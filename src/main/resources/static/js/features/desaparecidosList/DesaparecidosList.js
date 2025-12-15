import { DesaparecidoService } from '../../services/DesaparecidoService.js';

export class DesaparecidosList {

  constructor() {
    this.estadoId = 'listaEstado';
    this.tbodyId = 'tablaDesaparecidosBody';
    this.init();
  }

  init() {
    const btnReload = document.getElementById('btnReload');
    if (btnReload) {
      btnReload.onclick = () => this.load();
    }
  }

  async load() {
    const estado = document.getElementById(this.estadoId);
    const tbody = document.getElementById(this.tbodyId);

    if (estado) {
      estado.textContent = 'Cargando…';
    }

    if (tbody) {
      tbody.innerHTML = '';
    }

    try {
      const data = await DesaparecidoService.obtenerTodos();

      if (estado) {
        estado.textContent = '';
      }

      this.renderTable(data);

      if (!data || data.length === 0) {
        if (estado) {
          estado.textContent = 'No hay registros.';
        }
      }
    } catch (err) {
      console.error('Error cargando lista:', err);
      if (estado) {
        estado.textContent = 'Error al cargar la lista.';
      }
    }
  }

  renderTable(data) {
    const tbody = document.getElementById(this.tbodyId);
    tbody.innerHTML = '';

    (data || []).forEach((d) => {
      const tr = document.createElement('tr');
      tr.className = 'table__row';
      tr.innerHTML = `
        <td class="table__cell">${d.nombre ?? ''}</td>
        <td class="table__cell">${d.apellido ?? ''}</td>
        <td class="table__cell">${d.dni ?? ''}</td>
        <td class="table__cell">${d.descripcion ?? ''}</td>
        <td class="table__cell"><img class="table__image" src="${d.foto ?? ''}" alt="foto"/></td>
        <td class="table__cell">${d.fechaFormateada ?? ''}</td>
      `;
      tbody.appendChild(tr);
    });
  }
}
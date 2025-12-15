/**
 * Helpers para renderizado de tablas
 */

/**
 * Renderiza filas en una tabla
 * @param {string} tbodyId - ID del tbody
 * @param {Array} data - Datos a renderizar
 * @param {Function} rowRenderer - Función que retorna HTML de una fila
 */
export function renderTable(tbodyId, data, rowRenderer) {
  const tbody = document.getElementById(tbodyId);
  if (!tbody) return;

  tbody.innerHTML = '';

  if (!data || data.length === 0) {
    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: #999;">No hay registros</td></tr>';
    return;
  }

  data.forEach(item => {
    const tr = document.createElement('tr');
    tr.className = 'table__row';
    tr.innerHTML = rowRenderer(item);
    tbody.appendChild(tr);
  });
}

/**
 * Renderiza una fila de desaparecido
 * @param {Object} d - Datos del desaparecido
 * @returns {string} HTML de la fila
 */
export function renderDesaparecidoRow(d) {
  return `
    <td class="table__cell">${d.nombre ?? ''}</td>
    <td class="table__cell">${d.apellido ?? ''}</td>
    <td class="table__cell">${d.dni ?? ''}</td>
    <td class="table__cell">${d.descripcion ?? ''}</td>
    <td class="table__cell">
      <img class="table__image"
           src="${d.foto ?? 'https://via.placeholder.com/150'}"
           alt="Foto de ${d.nombre ?? 'desconocido'}"
           onerror="this.src='https://via.placeholder.com/150?text=Sin+foto'">
    </td>
    <td class="table__cell">${d.fechaFormateada ?? ''}</td>
  `;
}
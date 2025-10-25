/**
 * Utilidades para generar templates HTML
 */

/**
 * Genera el contenido HTML de un popup de avistamiento
 * @param {object} avistamiento - Datos del avistamiento
 * @returns {string} HTML del popup
 */
export function createPopupContent(avistamiento) {
  const {
    desaparecidoNombre = '',
    desaparecidoApellido = '',
    desaparecidoFoto,
    fotoUrl,
    fechaFormateada = '',
    descripcion = '',
    avistadorNombre = '',
    verificado = false
  } = avistamiento;

  const foto = desaparecidoFoto ?? fotoUrl ?? '';

  const imagenHtml = foto
    ? `<img class="popup__image" src="${escapeHtml(foto)}" alt="Foto" loading="lazy">`
    : '';

  const badgeHtml = verificado
    ? '<span class="badge badge--verified">✓ Verificado</span>'
    : '<span class="badge badge--unverified">⚠ Sin verificar</span>';

  return `
    <div class="popup">
      <h3 class="popup__title">${escapeHtml(desaparecidoNombre)} ${escapeHtml(desaparecidoApellido)}</h3>
      ${imagenHtml}
      <p class="popup__text"><span class="popup__label">📅</span> ${escapeHtml(fechaFormateada)}</p>
      <p class="popup__text"><span class="popup__label">📝</span> ${escapeHtml(descripcion)}</p>
      <p class="popup__text"><span class="popup__label">👤</span> ${escapeHtml(avistadorNombre)}</p>
      ${badgeHtml}
    </div>
  `;
}

/**
 * Genera el contenido de una opción de select
 * @param {object} desaparecido - Datos del desaparecido
 * @returns {string} Texto para el option
 */
export function createDesaparecidoOption(desaparecido) {
  const { nombre, apellido, dni } = desaparecido;
  return `${nombre} ${apellido} (DNI: ${dni})`;
}

/**
 * Escapa HTML para prevenir XSS
 * @param {string} unsafe - Texto sin sanitizar
 * @returns {string} Texto sanitizado
 */
function escapeHtml(unsafe) {
  if (unsafe == null) return '';

  return String(unsafe)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#039;');
}
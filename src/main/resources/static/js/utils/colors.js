/**
 * Utilidades para manejo de colores de markers
 */

// Paleta de colores disponibles para markers
const MARKER_COLORS = [
  'red',
  'blue',
  'green',
  'violet',
  'orange',
  'yellow',
  'grey',
  'black'
];

/**
 * Genera un color consistente basado en un ID
 * @param {string} id - UUID del desaparecido
 * @returns {string} Nombre del color
 */
export function getColorForId(id) {
  if (!id) return 'gold';

  // Generar hash simple
  let hash = 0;
  const idStr = String(id);

  for (let i = 0; i < idStr.length; i++) {
    hash = idStr.charCodeAt(i) + ((hash << 5) - hash);
  }

  const index = Math.abs(hash) % MARKER_COLORS.length;
  return MARKER_COLORS[index];
}

/**
 * Obtiene la URL del icono de marker según estado
 * @param {boolean} verificado - Si el avistamiento está verificado
 * @param {string} desaparecidoId - ID del desaparecido
 * @returns {string} URL del icono
 */
export function getMarkerIconUrl(verificado, desaparecidoId) {
  const baseUrl = 'https://cdn.jsdelivr.net/gh/pointhi/leaflet-color-markers@master/img';

  if (verificado) {
    return `${baseUrl}/marker-icon-2x-green.png`;
  }

  const color = getColorForId(desaparecidoId);
  return `${baseUrl}/marker-icon-2x-${color}.png`;
}

/**
 * Configuración estándar de iconos de Leaflet
 */
export const MARKER_ICON_CONFIG = {
  shadowUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
};
/**
 * Parsea una respuesta HTTP a un objeto de error estructurado
 * @param {Response} resp - Respuesta HTTP
 * @returns {Promise<{status: number, key: string|null, detail: string}>}
 */
export async function parseProblem(resp) {
  const text = await resp.text();
  try {
    const pd = JSON.parse(text);
    const key = pd?.key ?? pd?.properties?.key ?? null;
    const detail = pd?.detail ?? text ?? `HTTP ${resp.status}`;
    return { status: resp.status, key, detail };
  } catch {
    return { status: resp.status, key: null, detail: text || `HTTP ${resp.status}` };
  }
}

/**
 * Obtiene el mensaje de error amigable para el usuario
 * @param {object} errorMap - Mapa de errores (de constants.js)
 * @param {number} status - HTTP status code
 * @param {string|null} key - Error key del backend
 * @param {string} detail - Detalle del error
 * @returns {string} Mensaje amigable
 */
export function getErrorMessage(errorMap, status, key, detail) {
  const byStatus = errorMap[status];
  if (!byStatus) return detail || `Error HTTP ${status}`;
  if ((status === 422 || status === 404) && key && byStatus[key]) return byStatus[key];
  return byStatus.default || detail || `Error HTTP ${status}`;
}
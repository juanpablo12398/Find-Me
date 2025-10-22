import { API_ENDPOINTS, ERROR_MAPS } from '../config/constants.js';
import { fetchWithAuth } from '../utils/fetch.js';
import { parseProblem, getErrorMessage } from '../utils/errors.js';

/**
 * Servicio para gestionar avistamientos
 */
export class AvistamientoService {

  /**
   * Obtiene todos los avistamientos para mostrar en el mapa
   * @returns {Promise<Array>}
   */
    static async getAvistamientosParaMapa() {
      const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/mapa`);
      if (!resp.ok) {
        const problem = await parseProblem(resp);
        const msg = getErrorMessage(
          ERROR_MAPS.AVISTAMIENTO,
          problem.status,
          problem.key,
          problem.detail
        );
        throw new Error(msg);
      }
      return await resp.json();
    }

  /**
   * Obtiene avistamientos en un área específica
   * @param {number} latMin - Latitud mínima
   * @param {number} latMax - Latitud máxima
   * @param {number} lngMin - Longitud mínima
   * @param {number} lngMax - Longitud máxima
   * @returns {Promise<Array>}
   */
    static async getAvistamientosEnArea(latMin, latMax, lngMin, lngMax) {
      const params = new URLSearchParams({ latMin, latMax, lngMin, lngMax });
      const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/mapa/area?${params}`);
      if (!resp.ok) {
        const problem = await parseProblem(resp);
        const msg = getErrorMessage(
          ERROR_MAPS.AVISTAMIENTO,
          problem.status,
          problem.key,
          problem.detail
        );
        throw new Error(msg);
      }
      return await resp.json();
    }

  /**
   * Obtiene la lista de desaparecidos (para el select del formulario)
   * @returns {Promise<Array>}
   */
    static async getDesaparecidos() {
      const resp = await fetchWithAuth(API_ENDPOINTS.DESAPARECIDOS);
      if (!resp.ok) {
        const problem = await parseProblem(resp);
        const msg = getErrorMessage(
          ERROR_MAPS.DESAPARECIDO,
          problem.status,
          problem.key,
          problem.detail
        );
        throw new Error(msg);
      }
      return await resp.json();
    }

  /**
   * Crea un nuevo avistamiento
   * @param {object} data - Datos del avistamiento
   * @returns {Promise<object>}
   */
  static async crear(data) {
    const resp = await fetchWithAuth(API_ENDPOINTS.AVISTAMIENTOS, {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data)
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(ERROR_MAPS.AVISTAMIENTO, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    return await resp.json();
  }
}
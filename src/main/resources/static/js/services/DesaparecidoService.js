import { API_ENDPOINTS, ERROR_MAPS } from '../config/constants.js';
import { fetchWithAuth } from '../utils/fetch.js';
import { parseProblem, getErrorMessage } from '../utils/errors.js';

/**
 * Servicio para gestionar desaparecidos
 */
export class DesaparecidoService {

  /**
   * Obtiene todos los desaparecidos
   * @returns {Promise<Array>}
   */
    static async obtenerTodos() {
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
   * Crea un nuevo desaparecido
   * @param {object} data - Datos del desaparecido
   * @returns {Promise<object>}
   */
  static async crear(data) {
    const resp = await fetchWithAuth(API_ENDPOINTS.DESAPARECIDOS, {
      method: "POST",
      body: JSON.stringify(data)
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(ERROR_MAPS.DESAPARECIDO, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    return await resp.json();
  }
}
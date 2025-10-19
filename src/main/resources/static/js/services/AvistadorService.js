import { API_ENDPOINTS, ERROR_MAPS } from '../config/constants.js';
import { fetchWithAuth } from '../utils/fetch.js';
import { parseProblem, getErrorMessage } from '../utils/errors.js';

/**
 * Servicio para gestionar avistadores
 */
export class AvistadorService {

  /**
   * Crea un nuevo avistador (registro)
   * @param {object} data - Datos del avistador
   * @returns {Promise<object>}
   */
  static async crear(data) {
    const resp = await fetchWithAuth(API_ENDPOINTS.AVISTADORES, {
      method: "POST",
      body: JSON.stringify(data)
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(ERROR_MAPS.AVISTADOR, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    return await resp.json();
  }
}
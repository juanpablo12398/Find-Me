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
   * Obtiene avistamientos dentro de un radio desde un punto
   * @param {number} lat - Latitud del centro
   * @param {number} lng - Longitud del centro
   * @param {number} radioKm - Radio en kilómetros
   * @returns {Promise<Array>}
   */
  static async getEnRadio(lat, lng, radioKm) {
    const params = new URLSearchParams({ lat, lng, radioKm });
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/radio?${params}`);
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
   * Obtiene avistamientos en un área rectangular
   * @param {number} latMin - Latitud mínima
   * @param {number} latMax - Latitud máxima
   * @param {number} lngMin - Longitud mínima
   * @param {number} lngMax - Longitud máxima
   * @returns {Promise<Array>}
   */
  static async getEnArea(latMin, latMax, lngMin, lngMax) {
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
   * Cuenta avistamientos en un área rectangular
   * @param {number} latMin - Latitud mínima
   * @param {number} latMax - Latitud máxima
   * @param {number} lngMin - Longitud mínima
   * @param {number} lngMax - Longitud máxima
   * @returns {Promise<number>}
   */
  static async contarEnArea(latMin, latMax, lngMin, lngMax) {
    const params = new URLSearchParams({ latMin, latMax, lngMin, lngMax });
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/area/count?${params}`);
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
   * Obtiene avistamientos dentro de un polígono (WKT)
   * @param {string} wkt - Polígono en formato WKT
   * @returns {Promise<Array>}
   */
  static async getEnPoligono(wkt) {
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/poligono?wkt=${encodeURIComponent(wkt)}`, {
      method: 'POST'
    });
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
   * Obtiene avistamientos de un desaparecido específico
   * @param {string} desaparecidoId - ID del desaparecido
   * @returns {Promise<Array>}
   */
  static async getPorDesaparecido(desaparecidoId) {
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/desaparecido/${desaparecidoId}`);
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
   * Obtiene avistamientos de un desaparecido en un radio
   * @param {string} desaparecidoId - ID del desaparecido
   * @param {number} lat - Latitud del centro
   * @param {number} lng - Longitud del centro
   * @param {number} radioKm - Radio en kilómetros
   * @returns {Promise<Array>}
   */
  static async getPorDesaparecidoEnRadio(desaparecidoId, lat, lng, radioKm) {
    const params = new URLSearchParams({ lat, lng, radioKm });
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/desaparecido/${desaparecidoId}/radio?${params}`);
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
   * Obtiene los N avistamientos más cercanos a un punto
   * @param {number} lat - Latitud del punto
   * @param {number} lng - Longitud del punto
   * @param {number} limite - Número máximo de resultados (default: 10)
   * @returns {Promise<Array>}
   */
  static async getMasCercanos(lat, lng, limite = 10) {
    const params = new URLSearchParams({ lat, lng, limite });
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AVISTAMIENTOS}/cercanos?${params}`);
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

  /**
   * Obtiene todos los avistamientos (sin datos enriquecidos)
   * @returns {Promise<Array>}
   */
  static async obtenerTodos() {
    const resp = await fetchWithAuth(API_ENDPOINTS.AVISTAMIENTOS);
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
}
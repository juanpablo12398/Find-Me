import { API_ENDPOINTS, ERROR_MAPS } from '../config/constants.js';
import { appState } from '../config/state.js';
import { fetchWithAuth } from '../utils/fetch.js';
import { parseProblem, getErrorMessage } from '../utils/errors.js';

/**
 * Servicio de autenticación
 */
export class AuthService {

  /**
   * Verifica si hay una sesión activa (cookie JWT)
   * @returns {Promise<boolean>}
   */
  static async checkAuth() {
    try {
      const resp = await fetchWithAuth(`${API_ENDPOINTS.AUTH}/me`);
      if (resp.ok) {
        const user = await resp.json();
        user.id = String(user.id);
        appState.currentUser = user;
        console.log('✅ Sesión activa encontrada:', user);
        return true;
      }
    } catch (error) {
      console.error('Error checking auth:', error);
    }
    appState.currentUser = null;
    return false;
  }

  /**
   * Inicia sesión
   * @param {string} dni - DNI del usuario
   * @param {string} email - Email del usuario
   * @returns {Promise<object>} Usuario logueado
   * @throws {Error} Si las credenciales son inválidas
   */
  static async login(dni, email) {
    const resp = await fetchWithAuth(`${API_ENDPOINTS.AUTH}/login`, {
      method: "POST",
      body: JSON.stringify({ dni, email })
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(ERROR_MAPS.AUTH, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    const user = await resp.json();
    user.id = String(user.id);
    appState.currentUser = user;
    console.log('✅ Login exitoso:', user);
    return user;
  }

  /**
   * Cierra sesión
   */
  static async logout() {
    try {
      await fetchWithAuth(`${API_ENDPOINTS.AUTH}/logout`, { method: "POST" });
      console.log('✅ Logout exitoso');
    } catch (error) {
      console.error('Error during logout:', error);
    }
    appState.currentUser = null;
  }

  /**
   * Devuelve el usuario actual
   * @returns {object|null}
   */
  static getCurrentUser() {
    return appState.currentUser;
  }

  /**
   * Verifica si el usuario está autenticado
   * @returns {boolean}
   */
  static isAuthenticated() {
    return appState.currentUser !== null;
  }
}
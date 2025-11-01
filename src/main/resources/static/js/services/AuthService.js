// AuthService.js
import { API_ENDPOINTS, ERROR_MAPS } from '../config/constants.js';
import { appState } from '../config/state.js';
import { fetchWithAuth } from '../utils/fetch.js';
import { parseProblem, getErrorMessage } from '../utils/errors.js';

export class AuthService {
  static _emitAuthChanged() {
    window.dispatchEvent(new CustomEvent('auth:changed', {
      detail: { user: appState.currentUser }
    }));
  }

  static async checkAuth() {
    try {
      const resp = await fetchWithAuth(`${API_ENDPOINTS.AUTH}/me`);
      if (resp.ok) {
        const user = await resp.json();
        user.id = String(user.id);
        appState.currentUser = user;
        console.log('✅ Sesión activa encontrada:', user);
        this._emitAuthChanged();
        return true;
      }
    } catch (error) {
      console.error('Error checking auth:', error);
    }
    appState.currentUser = null;
    this._emitAuthChanged();
    return false;
  }

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
    this._emitAuthChanged();
    return user;
  }

  static async logout() {
    try {
      await fetchWithAuth(`${API_ENDPOINTS.AUTH}/logout`, { method: "POST" });
      console.log('✅ Logout exitoso');
    } catch (error) {
      console.error('Error during logout:', error);
    }
    appState.currentUser = null;
    this._emitAuthChanged();
  }

  static getCurrentUser() { return appState.currentUser; }
  static isAuthenticated() { return appState.currentUser !== null; }
}
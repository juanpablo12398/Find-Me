import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest';
import { okJson, errorResponse } from '@test/helpers/http.js';

// ───────────────────────────────────────────────────────────────────────────────
// Mocks (tienen que ir antes del import del SUT)
// ───────────────────────────────────────────────────────────────────────────────
vi.mock('@app/config/constants.js', () => ({
  API_ENDPOINTS: { AUTH: 'http://fake.api/auth' },
  ERROR_MAPS: { AUTH: {} },
}));

let _currentUser = null;
vi.mock('@app/config/state.js', () => ({
  appState: {
    get currentUser() { return _currentUser; },
    set currentUser(v) { _currentUser = v; },
  },
}));

const fetchWithAuthMock = vi.fn();
vi.mock('@app/utils/fetch.js', () => ({
  fetchWithAuth: (...args) => fetchWithAuthMock(...args),
}));

const parseProblemMock = vi.fn();
const getErrorMessageMock = vi.fn();
vi.mock('@app/utils/errors.js', () => ({
  parseProblem: (...args) => parseProblemMock(...args),
  getErrorMessage: (...args) => getErrorMessageMock(...args),
}));

// ───────────────────────────────────────────────────────────────────────────────
// Import del SUT (después de los mocks sí o sí)
// ───────────────────────────────────────────────────────────────────────────────
import { AuthService } from '@app/services/AuthService.js';
import { API_ENDPOINTS } from '@app/config/constants.js';

// ───────────────────────────────────────────────────────────────────────────────
// Hooks
// ───────────────────────────────────────────────────────────────────────────────
beforeEach(() => {
  _currentUser = null;
  fetchWithAuthMock.mockReset();
  parseProblemMock.mockReset();
  getErrorMessageMock.mockReset();
});

afterEach(() => {
  vi.restoreAllMocks();
});

// ───────────────────────────────────────────────────────────────────────────────
// Tests
// ───────────────────────────────────────────────────────────────────────────────
describe('AuthService', () => {
  describe('checkAuth', () => {
    it('retorna true y setea currentUser cuando /me responde ok', async () => {
      const user = { id: 1, name: 'Ana' };
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user));

      const ok = await AuthService.checkAuth();

      expect(ok).toBe(true);
      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/me`);
      expect(AuthService.getCurrentUser()).toEqual(user);
    });

    it('retorna false y limpia currentUser cuando /me falla', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(errorResponse(401));
      const ok = await AuthService.checkAuth();
      expect(ok).toBe(false);
      expect(AuthService.getCurrentUser()).toBeNull();
    });

    it('retorna false si fetchWithAuth lanza', async () => {
      fetchWithAuthMock.mockRejectedValueOnce(new Error('network'));
      const ok = await AuthService.checkAuth();
      expect(ok).toBe(false);
      expect(AuthService.getCurrentUser()).toBeNull();
    });
  });

  describe('login', () => {
    it('guarda y devuelve el usuario cuando login es ok', async () => {
      const user = { id: 2, name: 'Luis' };
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user));

      const res = await AuthService.login('123', 'l@e.com');

      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/login`, {
        method: 'POST',
        body: JSON.stringify({ dni: '123', email: 'l@e.com' }),
      });
      expect(res).toEqual(user);
      expect(AuthService.getCurrentUser()).toEqual(user);
    });

    it('lanza Error con mensaje mapeado cuando login falla', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(errorResponse(401));
      parseProblemMock.mockResolvedValueOnce({ status: 401, key: 'INVALID', detail: 'Bad creds' });
      getErrorMessageMock.mockReturnValueOnce('Credenciales inválidas');

      await expect(AuthService.login('999', 'x@y.com')).rejects.toThrow('Credenciales inválidas');
      expect(parseProblemMock).toHaveBeenCalled();
      expect(getErrorMessageMock).toHaveBeenCalled();
      expect(AuthService.getCurrentUser()).toBeNull();
    });
  });

  describe('logout', () => {
    it('hace POST a /logout y limpia el usuario aunque falle', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(okJson({}));
      await AuthService.logout();
      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/logout`, { method: 'POST' });
      expect(AuthService.getCurrentUser()).toBeNull();

      fetchWithAuthMock.mockRejectedValueOnce(new Error('server down'));
      await AuthService.logout();
      expect(AuthService.getCurrentUser()).toBeNull();
    });
  });

  it('getCurrentUser retorna null si no hay sesión', () => {
    expect(AuthService.getCurrentUser()).toBeNull();
  });
});

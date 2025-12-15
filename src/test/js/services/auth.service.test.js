/* @vitest-environment jsdom */

// Polyfill defensivo (jsdom ya lo trae, pero evita sorpresas en CI):
if (typeof global.CustomEvent === 'undefined') {
  global.CustomEvent = class CustomEvent extends Event {
    constructor(name, params = {}) { super(name, params); this.detail = params.detail || null; }
  };
}

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { okJson, errorResponse } from '@test/helpers/http.js'

// ───────────────────────────────────────────────────────────────────────────────
// Mocks (deben ir antes del import del SUT)
// ───────────────────────────────────────────────────────────────────────────────
vi.mock('@app/config/constants.js', () => ({
  API_ENDPOINTS: { AUTH: 'http://fake.api/auth' },
  ERROR_MAPS: { AUTH: {} },
}))

// backing state controlado por el test (evita usar appState directo)
let _currentUser = null
vi.mock('@app/config/state.js', () => ({
  appState: {
    get currentUser () { return _currentUser },
    set currentUser (v) { _currentUser = v },
  },
}))

const fetchWithAuthMock = vi.fn()
vi.mock('@app/utils/fetch.js', () => ({
  fetchWithAuth: (...args) => fetchWithAuthMock(...args),
}))

const parseProblemMock = vi.fn()
const getErrorMessageMock = vi.fn()
vi.mock('@app/utils/errors.js', () => ({
  parseProblem: (...args) => parseProblemMock(...args),
  getErrorMessage: (...args) => getErrorMessageMock(...args),
}))

// ───────────────────────────────────────────────────────────────────────────────
// Import del SUT
// ───────────────────────────────────────────────────────────────────────────────
import { AuthService } from '@app/services/AuthService.js'
import { API_ENDPOINTS } from '@app/config/constants.js'

// ───────────────────────────────────────────────────────────────────────────────
// Spies globales de console
// ───────────────────────────────────────────────────────────────────────────────
const consoleLogSpy = vi.spyOn(console, 'log').mockImplementation(() => {})
const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})

// ───────────────────────────────────────────────────────────────────────────────
// Hooks
// ───────────────────────────────────────────────────────────────────────────────
beforeEach(() => {
  vi.clearAllMocks()
  _currentUser = null
})

afterEach(() => {
  vi.clearAllMocks()
})

// ───────────────────────────────────────────────────────────────────────────────
// Tests
// ───────────────────────────────────────────────────────────────────────────────
describe('AuthService', () => {
  describe('checkAuth', () => {
    it('retorna true y setea currentUser cuando /me responde ok', async () => {
      const user = { id: 1, name: 'Ana' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user))

      const ok = await AuthService.checkAuth()

      expect(ok).toBe(true)
      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/me`)
      expect(AuthService.getCurrentUser()).toEqual(user)
      expect(consoleLogSpy).toHaveBeenCalledWith('✅ Sesión activa encontrada:', user)
    })

    it('retorna false y limpia currentUser cuando /me falla', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(errorResponse(401))
      const ok = await AuthService.checkAuth()
      expect(ok).toBe(false)
      expect(AuthService.getCurrentUser()).toBeNull()
      expect(consoleLogSpy).not.toHaveBeenCalledWith(expect.stringMatching(/✅ Sesión activa encontrada/))
    })

    it('retorna false si fetchWithAuth lanza y loguea error', async () => {
      const error = new Error('network')
      fetchWithAuthMock.mockRejectedValueOnce(error)

      const ok = await AuthService.checkAuth()

      expect(ok).toBe(false)
      expect(AuthService.getCurrentUser()).toBeNull()
      expect(consoleErrorSpy).toHaveBeenCalledWith('Error checking auth:', error)
    })
  })

  describe('login', () => {
    it('guarda y devuelve el usuario cuando login es ok', async () => {
      const user = { id: 2, name: 'Luis', dni: '12345678', email: 'l@e.com' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user))

      const res = await AuthService.login('12345678', 'l@e.com')

      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/login`, {
        method: 'POST',
        body: JSON.stringify({ dni: '12345678', email: 'l@e.com' }),
      })
      expect(res).toEqual(user)
      expect(AuthService.getCurrentUser()).toEqual(user)
      expect(consoleLogSpy).toHaveBeenCalledWith('✅ Login exitoso:', user)
    })

    it('lanza Error con mensaje mapeado cuando login falla', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(errorResponse(401))
      parseProblemMock.mockResolvedValueOnce({ status: 401, key: 'INVALID', detail: 'Bad creds' })
      getErrorMessageMock.mockReturnValueOnce('Credenciales inválidas')

      await expect(AuthService.login('999', 'x@y.com')).rejects.toThrow('Credenciales inválidas')
      expect(parseProblemMock).toHaveBeenCalled()
      expect(getErrorMessageMock).toHaveBeenCalled()
      expect(AuthService.getCurrentUser()).toBeNull()
      expect(consoleLogSpy).not.toHaveBeenCalledWith(expect.stringMatching(/✅ Login exitoso/))
    })
  })

  describe('logout', () => {
    it('hace POST a /logout, limpia el usuario y loguea éxito', async () => {
      _currentUser = { id: 1, name: 'Test' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson({}))

      await AuthService.logout()

      expect(fetchWithAuthMock).toHaveBeenCalledWith(`${API_ENDPOINTS.AUTH}/logout`, { method: 'POST' })
      expect(AuthService.getCurrentUser()).toBeNull()
      expect(consoleLogSpy).toHaveBeenCalledWith('✅ Logout exitoso')
    })

    it('limpia el usuario aunque falle el request y loguea error', async () => {
      _currentUser = { id: 1, name: 'Test' }
      const error = new Error('server down')
      fetchWithAuthMock.mockRejectedValueOnce(error)

      await AuthService.logout()

      expect(AuthService.getCurrentUser()).toBeNull()
      expect(consoleErrorSpy).toHaveBeenCalledWith('Error during logout:', error)
      expect(consoleLogSpy).not.toHaveBeenCalledWith('✅ Logout exitoso')
    })
  })

  describe('getCurrentUser / isAuthenticated', () => {
    it('getCurrentUser retorna null si no hay sesión', () => {
      expect(AuthService.getCurrentUser()).toBeNull()
    })
    it('getCurrentUser retorna el usuario actual si hay sesión', () => {
      const user = { id: 3, name: 'Pedro' }
      _currentUser = user
      expect(AuthService.getCurrentUser()).toEqual(user)
    })
    it('isAuthenticated retorna false cuando currentUser es null', () => {
      _currentUser = null
      expect(AuthService.isAuthenticated()).toBe(false)
    })
    it('isAuthenticated retorna true cuando hay un usuario actual', () => {
      _currentUser = { id: 4, name: 'María' }
      expect(AuthService.isAuthenticated()).toBe(true)
    })
    it('isAuthenticated cambia dinámicamente', () => {
      expect(AuthService.isAuthenticated()).toBe(false)
      _currentUser = { id: 5, name: 'Juan' }
      expect(AuthService.isAuthenticated()).toBe(true)
      _currentUser = null
      expect(AuthService.isAuthenticated()).toBe(false)
    })
  })

  describe('Integración', () => {
    it('checkAuth + isAuthenticated (ok)', async () => {
      const user = { id: 6, name: 'Carlos' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user))
      expect(AuthService.isAuthenticated()).toBe(false)
      await AuthService.checkAuth()
      expect(AuthService.isAuthenticated()).toBe(true)
      expect(AuthService.getCurrentUser()).toEqual(user)
    })
    it('checkAuth + isAuthenticated (falla)', async () => {
      fetchWithAuthMock.mockResolvedValueOnce(errorResponse(401))
      expect(AuthService.isAuthenticated()).toBe(false)
      await AuthService.checkAuth()
      expect(AuthService.isAuthenticated()).toBe(false)
      expect(AuthService.getCurrentUser()).toBeNull()
    })
    it('login + isAuthenticated', async () => {
      const user = { id: 7, name: 'Laura' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson(user))
      expect(AuthService.isAuthenticated()).toBe(false)
      await AuthService.login('11111111', 'laura@test.com')
      expect(AuthService.isAuthenticated()).toBe(true)
      expect(AuthService.getCurrentUser()).toEqual(user)
    })
    it('logout + isAuthenticated', async () => {
      _currentUser = { id: 8, name: 'Roberto' }
      fetchWithAuthMock.mockResolvedValueOnce(okJson({}))
      await AuthService.logout()
      expect(AuthService.isAuthenticated()).toBe(false)
      expect(AuthService.getCurrentUser()).toBeNull()
    })
  })
})

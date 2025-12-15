/* @vitest-environment jsdom */
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { installLeafletMock } from '../helpers/leafletmock.js'

import { MapManager } from '@app/ui/MapManager.js'

// Mock de appState con “decoración” para asegurar getContainer/invalidateSize
vi.mock('@app/config/state.js', () => {
  const ensureMapShape = (m = {}) => ({
    on: vi.fn(),
    off: vi.fn(),
    removeLayer: vi.fn(),
    ...m,
    getContainer: m.getContainer ?? (() => document.createElement('div')),
    invalidateSize: m.invalidateSize ?? vi.fn(),
    doubleClickZoom: m.doubleClickZoom ?? {
      _enabled: true,
      enabled () { return this._enabled },
      enable () { this._enabled = true },
      disable () { this._enabled = false },
    },
    dragging: m.dragging ?? {
      _enabled: true,
      enable () { this._enabled = true },
      disable () { this._enabled = false },
    },
  })

  let _map = null
  return {
    appState: {
      get map () { return _map },
      set map (m) { _map = ensureMapShape(m) },
      markersLayer: { clearLayers: vi.fn(), addLayer: vi.fn() },
    }
  }
})

vi.mock('@app/ui/MapFilters.js', () => {
  const ctor = vi.fn().mockImplementation(function (mm) {
    this.mm = mm
    this.init = vi.fn()
    this.applyCurrentFilter = vi.fn().mockResolvedValue(undefined)
    this.needsMapListeners = vi.fn(() => false)
  })
  return { MapFilters: ctor }
})

vi.mock('@app/services/AuthService.js', () => ({
  AuthService: {
    getCurrentUser: vi.fn(() => ({ id: 'u1', nombre: 'Juan' })),
    logout: vi.fn(),
  }
}))

vi.mock('@app/services/DesaparecidoService.js', () => ({
  DesaparecidoService: { obtenerTodos: vi.fn().mockResolvedValue([]) }
}))

vi.mock('@app/services/AvistamientoService.js', () => ({
  AvistamientoService: {
    crear: vi.fn(),
    obtenerTodos: vi.fn().mockResolvedValue([]),
  }
}))

vi.mock('@app/utils/colors.js', () => ({
  getMarkerIconUrl: vi.fn(() => 'marker.png'),
  MARKER_ICON_CONFIG: { iconSize: [25, 41], iconAnchor: [12, 41], popupAnchor: [1, -34], shadowSize: [41, 41] },
}))

vi.mock('@app/utils/templates.js', () => ({
  createPopupContent: vi.fn(() => '<div>popup</div>')
}))

vi.mock('@app/utils/validators.js', () => ({
  showError: vi.fn(),
  showSuccess: vi.fn(),
  showLoading: vi.fn(),
}))

vi.mock('@app/utils/dom.js', () => ({
  setButtonState: vi.fn(),
  createOption: (v, t) => {
    const o = document.createElement('option'); o.value = String(v); o.textContent = t; return o
  },
}))

import { MapFilters } from '@app/ui/MapFilters.js'
import { AuthService } from '@app/services/AuthService.js'
import { AvistamientoService } from '@app/services/AvistamientoService.js'
import { showError, showSuccess } from '@app/utils/validators.js'

describe('MapManager', () => {
  beforeEach(() => {
    vi.useFakeTimers()
    installLeafletMock()
    document.body.innerHTML = `
      <div id="mapaEstado"></div>
      <button id="btnToggleReportar"></button>
      <div id="map"></div>

      <div id="modalAvistamiento" class="u-hidden"></div>
      <span id="modalLatLng"></span>

      <select id="av_desaparecido"></select>
      <div id="formSection"></div>
      <div id="loginSection"></div>

      <div id="avistamientoResult"></div>
      <button id="btnSubmitAvistamiento"></button>
    `
    vi.clearAllMocks()

    // Silenciar logs ruidosos durante tests (opcional)
    vi.spyOn(console, 'log').mockImplementation(() => {})
  })

  afterEach(() => {
    vi.runOnlyPendingTimers()
    vi.useRealTimers()
    console.log.mockRestore?.()
  })

  it('init crea mapa, inicializa filtros y carga avistamientos', async () => {
    const mm = new MapManager()
    mm.init()
    vi.runOnlyPendingTimers() // dispara invalidateSize del setTimeout interno
    expect(MapFilters).toHaveBeenCalledWith(mm)
    expect(mm.filters.init).toHaveBeenCalled()
    expect(mm.filters.applyCurrentFilter).toHaveBeenCalled()
    expect(document.getElementById('map')).toBeTruthy()
  })

  it('refresh usa filtros si están presentes y renderiza si devuelve array', async () => {
    const mm = new MapManager()
    mm.init()
    vi.runOnlyPendingTimers()

    mm._renderAvistamientos = vi.fn()
    mm.filters.applyCurrentFilter.mockResolvedValueOnce([{ id: 1 }])

    await mm.refresh()
    expect(mm._renderAvistamientos).toHaveBeenCalledWith([{ id: 1 }])
  })

  it('submitAvistamiento valida auth/coords y llama al servicio', async () => {
    const mm = new MapManager()
    mm.init()
    vi.runOnlyPendingTimers()

    mm.selectedLatLng = { lat: -34.6, lng: -58.38 }
    AuthService.getCurrentUser.mockReturnValue({ id: 'u1', nombre: 'Juan' })

    AvistamientoService.crear.mockResolvedValueOnce({
      id: 'A1', latitud: -34.6, longitud: -58.38, descripcion: 'x', fotoUrl: null
    })

    await mm.submitAvistamiento({
      desaparecidoId: 'd1',
      descripcion: 'x',
      fotoUrl: '',
      publico: true,
      desaparecidoNombre: 'Persona X',
    })

    expect(AvistamientoService.crear).toHaveBeenCalledWith(expect.objectContaining({
      avistadorId: 'u1',
      desaparecidoId: 'd1',
      latitud: -34.6,
      longitud: -58.38,
      descripcion: 'x',
      publico: true
    }))
    expect(showSuccess).toHaveBeenCalled()
  })

  it('submitAvistamiento muestra error si no hay coords o no hay usuario', async () => {
    const mm = new MapManager()
    mm.init()
    vi.runOnlyPendingTimers()

    mm.selectedLatLng = null
    await mm.submitAvistamiento({ desaparecidoId: 'd1', publico: true })
    expect(showError).toHaveBeenCalled()

    vi.clearAllMocks()
    mm.selectedLatLng = { lat: 1, lng: 2 }
    vi.spyOn(AuthService, 'getCurrentUser').mockReturnValue(null)
    await mm.submitAvistamiento({ desaparecidoId: 'd1', publico: true })
    expect(showError).toHaveBeenCalled()
  })
})

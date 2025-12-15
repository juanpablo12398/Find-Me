/* @vitest-environment jsdom */
import { describe, it, expect, vi, beforeEach } from 'vitest'
import { installLeafletMock } from '../helpers/leafletmock.js'

vi.mock('@app/config/state.js', () => {
  const map = {
    on: vi.fn(),
    off: vi.fn(),
    removeLayer: vi.fn(),
    getContainer: () => document.createElement('div'),
    doubleClickZoom: { _enabled: true, enabled() { return this._enabled }, enable() { this._enabled = true }, disable() { this._enabled = false } },
    dragging: { _enabled: true, enable() { this._enabled = true }, disable() { this._enabled = false } },
  }
  return {
    appState: {
      map,
      markersLayer: { clearLayers: vi.fn(), addLayer: vi.fn() },
    }
  }
})

vi.mock('@app/services/DesaparecidoService.js', () => ({
  DesaparecidoService: { obtenerTodos: vi.fn() }
}))
vi.mock('@app/services/AvistamientoService.js', () => ({
  AvistamientoService: {
    getAvistamientosParaMapa: vi.fn(),
    getEnRadio: vi.fn(),
    getEnArea: vi.fn(),
    getEnPoligono: vi.fn(),
    getPorDesaparecido: vi.fn(),
  }
}))

vi.mock('@app/config/filter/constants.js', () => ({
  FILTER_TYPES: { TODOS:'TODOS', RADIO:'RADIO', AREA:'AREA', POLIGONO:'POLIGONO', DESAPARECIDO:'DESAPARECIDO' },
  MAP_MESSAGES: {
    SELECT_CENTER: { text: 'seleccioná centro', color: '#333' },
    DRAW_AREA: { text: 'dibujar área', color: '#333' },
    DRAW_POLYGON: { text: 'dibujar polígono', color: '#333' },
    CENTER_SELECTED: { text: 'centro ok', color: 'green' },
    POLYGON_COMPLETE: { text: 'polígono ok', color: 'green' },
    LOADING: { text: 'Cargando…', color: '#888' },
    NO_RESULTS: { text: 'Sin resultados', color: '#666' },
  },
  MAP_COLORS: {
    POLYGON: '#09f',
    RADIO_CIRCLE: '#f00',
    AREA_RECTANGLE: '#0f0',
  }
}))

import { DesaparecidoService } from '@app/services/DesaparecidoService.js'
import { AvistamientoService } from '@app/services/AvistamientoService.js'
import { FILTER_TYPES } from '@app/config/filter/constants.js'
import { MapFilters } from '@app/ui/MapFilters.js'

describe('MapFilters', () => {
  let mapManager

  beforeEach(() => {
    installLeafletMock()
    document.body.innerHTML = `
      <div id="sidebarFiltros"></div>
      <button id="btnToggleFilters"></button>

      <button id="filtroTodos" class="filtro-item"></button>
      <button id="filtroRadio" class="filtro-item"></button>
      <button id="filtroArea" class="filtro-item"></button>
      <button id="filtroPoligono" class="filtro-item"></button>

      <div id="radioControls" class="u-hidden">
        <input id="radioSlider" type="range" value="5" />
        <span id="radioValue"></span>
        <button id="btnCambiarPuntoRadio"></button>
      </div>

      <select id="filtroDesaparecidoSelect"></select>
      <button id="btnResetFiltros" class="u-hidden"></button>

      <span id="counterNumber"></span>
      <span id="counterLabel"></span>
    `
    vi.clearAllMocks()
    mapManager = {
      _enableMapClick: vi.fn(),
      _checkIfShouldDisableListeners: vi.fn(),
      _updateEstado: vi.fn(),
      _renderAvistamientos: vi.fn(),
    }
  })

  it('_loadDesaparecidosForFilter carga opciones en el select', async () => {
    DesaparecidoService.obtenerTodos.mockResolvedValueOnce([
      { id: '1', nombre: 'Ana', apellido: 'Paz' },
      { id: '2', dni: '20123456' },
    ])
    const mf = new MapFilters(mapManager)
    await mf._loadDesaparecidosForFilter()
    const html = document.getElementById('filtroDesaparecidoSelect').innerHTML
    expect(html).toContain('Selecciona una persona')
    expect(html).toContain('Ana Paz')
    expect(html).toContain('20123456')
  })

  it('_activateFilter: RADIO activa selección y muestra controles', () => {
    const mf = new MapFilters(mapManager)
    const spyApply = vi.spyOn(mf, 'applyCurrentFilter').mockResolvedValue([])
    mf._activateFilter(FILTER_TYPES.RADIO)
    expect(mf.filterType).toBe(FILTER_TYPES.RADIO)
    expect(mf.isSelectingRadioCenter).toBe(true)
    expect(mapManager._enableMapClick).toHaveBeenCalled()
    expect(document.getElementById('radioControls').classList.contains('u-hidden')).toBe(false)
    spyApply.mockRestore()
  })

  it('applyCurrentFilter (TODOS) consulta servicio y actualiza counter/estado', async () => {
    AvistamientoService.getAvistamientosParaMapa.mockResolvedValueOnce([{ id: 1 }, { id: 2 }])
    const mf = new MapFilters(mapManager)
    mf.filterType = FILTER_TYPES.TODOS
    await mf.applyCurrentFilter()
    expect(AvistamientoService.getAvistamientosParaMapa).toHaveBeenCalled()
    expect(mapManager._renderAvistamientos).toHaveBeenCalledWith([{ id: 1 }, { id: 2 }])
    expect(document.getElementById('counterNumber').textContent).toBe('2')
    expect(document.getElementById('counterLabel').textContent).toContain('avistamientos encontrados')
  })

  it('applyCurrentFilter (RADIO) usa centro y radio', async () => {
    AvistamientoService.getEnRadio.mockResolvedValueOnce([{ id: 9 }])
    const mf = new MapFilters(mapManager)
    mf.filterType = FILTER_TYPES.RADIO
    mf.radioCenter = [-34.6, -58.38]
    mf.radioKm = 7
    await mf.applyCurrentFilter()
    expect(AvistamientoService.getEnRadio).toHaveBeenCalledWith(-34.6, -58.38, 7)
    expect(mapManager._renderAvistamientos).toHaveBeenCalledWith([{ id: 9 }])
  })

  it('applyCurrentFilter (AREA) pasa bounds convertidos', async () => {
    AvistamientoService.getEnArea.mockResolvedValueOnce([{ id: 3 }])
    const mf = new MapFilters(mapManager)
    mf.filterType = FILTER_TYPES.AREA
    mf.areaBounds = [[-35, -59], [-34, -58]]
    await mf.applyCurrentFilter()
    expect(AvistamientoService.getEnArea).toHaveBeenCalledWith(-35, -34, -59, -58)
  })

  it('applyCurrentFilter (POLIGONO) genera WKT', async () => {
    AvistamientoService.getEnPoligono.mockResolvedValueOnce([{ id: 5 }])
    const mf = new MapFilters(mapManager)
    mf.filterType = FILTER_TYPES.POLIGONO
    mf.poligonoCoords = [[-34, -58], [-34, -57.9], [-33.95, -58]]
    await mf.applyCurrentFilter()
    const wkt = AvistamientoService.getEnPoligono.mock.calls[0][0]
    expect(wkt).toMatch(/^POLYGON\(\(.+\)\)$/)
  })

  it('applyCurrentFilter (DESAPARECIDO) consulta por id', async () => {
    AvistamientoService.getPorDesaparecido.mockResolvedValueOnce([{ id: 7 }])
    const mf = new MapFilters(mapManager)
    mf.filterType = FILTER_TYPES.DESAPARECIDO
    mf.selectedDesaparecidoId = 'abc'
    await mf.applyCurrentFilter()
    expect(AvistamientoService.getPorDesaparecido).toHaveBeenCalledWith('abc')
  })

  it('resetFilters limpia estado y vuelve a TODOS', async () => {
    const mf = new MapFilters(mapManager)
    const spy = vi.spyOn(mf, 'applyCurrentFilter').mockResolvedValue([])
    mf.filterType = FILTER_TYPES.RADIO
    mf.radioCenter = [1, 2]
    mf.resetFilters()
    expect(mf.filterType).toBe(FILTER_TYPES.TODOS)
    expect(mf.radioCenter).toBeNull()
    expect(document.getElementById('filtroTodos').classList.contains('filtro-item--active')).toBe(true)
    expect(spy).toHaveBeenCalled()
    spy.mockRestore()
  })
})

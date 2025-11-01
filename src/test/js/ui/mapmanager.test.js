/// <reference types="vitest" />
/* @vitest-environment jsdom */

import { describe, it, expect, vi, beforeEach } from 'vitest'

// ❗️Estado hoisted para que lo compartan los módulos mockeados
const state = vi.hoisted(() => ({ map: null, markersLayer: null }))

vi.mock('@app/config/state.js', () => ({ appState: state }))

// ✅ Mockear también DesaparecidoService para evitar fetch real
const getDesaparecidosMock = vi.fn()
vi.mock('@app/services/DesaparecidoService.js', () => ({
  DesaparecidoService: {
    obtenerTodos: (...a) => getDesaparecidosMock(...a),
  },
}))

// ✅ Mock AvistamientoService (los métodos que usa MapManager)
const getMapaMock = vi.fn()
const crearAvistamientoMock = vi.fn()
vi.mock('@app/services/AvistamientoService.js', () => ({
  AvistamientoService: {
    getAvistamientosParaMapa: (...a) => getMapaMock(...a),
    crear: (...a) => crearAvistamientoMock(...a),
  },
}))

// ✅ Mock AuthService
const getCurrentUserMock = vi.fn()
vi.mock('@app/services/AuthService.js', () => ({
  AuthService: {
    getCurrentUser: (...a) => getCurrentUserMock(...a),
  },
}))

// ✅ Leaflet mock (incluye métodos usados por MapManager)
const fakeMap = {
  setView: vi.fn(() => fakeMap),
  on:     vi.fn(),
  off:    vi.fn(),
  removeLayer: vi.fn(),
  project: vi.fn(() => ({ x: 0, y: 0 })),
}
const fakeMarkersLayer = {
  addTo: vi.fn(() => fakeMarkersLayer),
  clearLayers: vi.fn(),
}
const makeMarker = () => ({
  bindPopup: vi.fn(function () { return this }),
  addTo:     vi.fn(function () { return this }),
  openPopup: vi.fn(function () { return this }),
  on:        vi.fn(),                 // 👈 necesario
  getLatLng: vi.fn(() => ({ lat: 0, lng: 0 })), // 👈 usado en click
})
const fakeMarker = makeMarker()

const LMock = {
  map:       vi.fn(() => fakeMap),
  tileLayer: vi.fn(() => ({ addTo: vi.fn() })),
  layerGroup: vi.fn(() => fakeMarkersLayer),
  icon: vi.fn(() => ({})),
  marker: vi.fn(() => makeMarker()),
}
globalThis.L = LMock

import { MapManager } from '@app/ui/MapManager.js'

function setupDOM () {
  document.body.innerHTML = `
    <div id="map"></div>
    <div id="mapaEstado"></div>
    <button id="btnToggleReportar"></button>
    <div id="modalAvistamiento" class="u-hidden"></div>
    <form id="formAvistamiento"></form>
    <span id="modalLatLng"></span>
    <select id="av_desaparecido"></select>
    <div id="avistamientoResult"></div>
    <button id="btnSubmitAvistamiento"></button>
  `
}

beforeEach(() => {
  setupDOM()
  state.map = null
  state.markersLayer = null

  // Reset de todos los mocks
  getMapaMock.mockReset()
  crearAvistamientoMock.mockReset()
  getDesaparecidosMock.mockReset()
  getCurrentUserMock.mockReset()

  fakeMap.setView.mockClear()
  fakeMap.on.mockClear()
  fakeMap.off.mockClear()
  fakeMap.removeLayer.mockClear()
  fakeMap.project.mockClear()

  fakeMarkersLayer.addTo.mockClear()
  fakeMarkersLayer.clearLayers.mockClear()

  LMock.map.mockClear()
  LMock.tileLayer.mockClear()
  LMock.layerGroup.mockClear()
  LMock.icon.mockClear()
  LMock.marker.mockClear()
})

describe('MapManager', () => {
  it('init: crea mapa, layer y actualiza estado', () => {
    const m = new MapManager()
    m.init()

    expect(LMock.map).toHaveBeenCalledWith('map')
    expect(state.map).toBe(fakeMap)
    expect(state.markersLayer).toBe(fakeMarkersLayer)
    expect(document.getElementById('mapaEstado').textContent).toMatch(/Mapa inicializado/i)
  })

  it('loadAvistamientos: muestra mensaje cuando lista vacía', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([])

    await m.loadAvistamientos()

    expect(fakeMarkersLayer.clearLayers).toHaveBeenCalled()
    expect(document.getElementById('mapaEstado').textContent).toMatch(/No hay avistamientos/i)
  })

  it('loadAvistamientos: agrega markers y muestra conteo', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([
      { latitud: 1, longitud: 2, verificado: true,  fechaFormateada: 'hoy',  descripcion: 'x', desaparecidoNombre: 'A', desaparecidoApellido: 'B', avistadorNombre: 'C' },
      { latitud: 3, longitud: 4, verificado: false, fechaFormateada: 'ayer', descripcion: 'y', desaparecidoNombre: 'D', desaparecidoApellido: 'E', avistadorNombre: 'F' },
    ])

    await m.loadAvistamientos()

    expect(LMock.marker).toHaveBeenCalledTimes(2)
    expect(document.getElementById('mapaEstado').textContent).toMatch(/2 avistamientos cargados/i)
  })

  it('loadAvistamientos: muestra error si el servicio falla', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockRejectedValueOnce(new Error('boom'))

    await m.loadAvistamientos()

    expect(document.getElementById('mapaEstado').textContent).toMatch(/Error: boom/i)
  })

  it('loadAvistamientos: retorna temprano si no hay map (no llama al servicio)', async () => {
    await new MapManager().loadAvistamientos()
    expect(getMapaMock).not.toHaveBeenCalled()
  })

  it('loadAvistamientos: mensaje en singular cuando hay 1 avistamiento', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([
      { latitud: 1, longitud: 2, verificado: true, fechaFormateada: 'hoy', descripcion: 'x', desaparecidoNombre: 'A', desaparecidoApellido: 'B', avistadorNombre: 'C' }
    ])

    await m.loadAvistamientos()
    expect(document.getElementById('mapaEstado').textContent).toMatch(/1 avistamiento cargado/i)
  })

  it('_updateEstado no rompe cuando mapaEstado no existe', () => {
    document.body.innerHTML = `<div id="map"></div>`
    const m = new MapManager()
    m._updateEstado('hola', 'red')
  })

  it('_updateEstado: además del texto, setea el color', () => {
    const m = new MapManager()
    m._updateEstado('estado', 'purple')
    const el = document.getElementById('mapaEstado')
    expect(el.textContent).toBe('estado')
    expect(el.style.color).toBe('purple')
  })

  it('loadAvistamientos: cuando hay foto incluye <img> en el popup', async () => {
    const bindPopupSpy = vi.fn(function () { return this })
    const markerInstance = { ...makeMarker(), bindPopup: bindPopupSpy }
    LMock.marker.mockReturnValueOnce(markerInstance)

    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([
      {
        latitud: 10, longitud: 20, verificado: true, fechaFormateada: 'hoy',
        descripcion: 'desc', desaparecidoNombre: 'N', desaparecidoApellido: 'A',
        avistadorNombre: 'V', desaparecidoFoto: 'http://img.test/foto.jpg'
      }
    ])

    await m.loadAvistamientos()

    expect(bindPopupSpy).toHaveBeenCalledTimes(1)
    const [html] = bindPopupSpy.mock.calls[0]
    expect(html).toContain('<img')
    expect(html).toContain('badge--verified')
  })

  it('closeModal: oculta modal, resetea formulario y limpia resultado', () => {
    document.body.innerHTML = `
      <div id="mapaEstado"></div>
      <div id="map"></div>
      <div id="modalAvistamiento" class="modal u-hidden"></div>
      <form id="formAvistamiento"></form>
      <div id="avistamientoResult"></div>
      <button id="btnToggleReportar"></button>
    `
    const m = new MapManager()
    m.init()

    const modal = document.getElementById('modalAvistamiento')
    const form  = document.getElementById('formAvistamiento')
    const result = document.getElementById('avistamientoResult')

    modal.classList.remove('u-hidden')
    result.textContent = 'Algún mensaje'

    const formResetSpy = vi.spyOn(form, 'reset')

    m.closeModal()

    expect(modal.classList.contains('u-hidden')).toBe(true)
    expect(formResetSpy).toHaveBeenCalled()
    expect(result.textContent).toBe('')
  })

  it('_loadDesaparecidosSelect: carga opciones en el select', async () => {
    const m = new MapManager()
    getDesaparecidosMock.mockResolvedValueOnce([
      { id: 'uuid-1', nombre: 'Juan', apellido: 'Pérez', dni: '12345678' },
      { id: 'uuid-2', nombre: 'Ana',  apellido: 'García', dni: '87654321' },
    ])

    await m._loadDesaparecidosSelect()

    const select = document.getElementById('av_desaparecido')
    expect(select.options.length).toBe(3) // 1 placeholder + 2 opciones
    expect(select.options[1].value).toBe('uuid-1')
    expect(select.options[1].textContent).toContain('Juan Pérez')
    expect(select.options[2].value).toBe('uuid-2')
  })

  it('_loadDesaparecidosSelect: muestra error si el servicio falla', async () => {
    const consoleErrorSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
    const m = new MapManager()
    getDesaparecidosMock.mockRejectedValueOnce(new Error('fail'))

    await m._loadDesaparecidosSelect()

    const select = document.getElementById('av_desaparecido')
    expect(select.options.length).toBe(1)
    expect(select.options[0].textContent).toContain('fail')
    expect(consoleErrorSpy).toHaveBeenCalled()
    consoleErrorSpy.mockRestore()
  })

  it('submitAvistamiento: sin coordenadas muestra error', async () => {
    const m = new MapManager()
    getCurrentUserMock.mockReturnValue({ id: 'uuid-user', dni: '12345678' })

    m.selectedLatLng = null

    await m.submitAvistamiento({
      desaparecidoId: 'uuid-desap', descripcion: 'desc', publico: true
    })

    const result = document.getElementById('avistamientoResult')
    expect(result.textContent).toMatch(/No hay coordenadas/i)
    expect(crearAvistamientoMock).not.toHaveBeenCalled()
  })

  it('submitAvistamiento: sin usuario muestra error', async () => {
    const m = new MapManager()
    getCurrentUserMock.mockReturnValue(null)
    m.selectedLatLng = { lat: -34.6037, lng: -58.3816 }

    await m.submitAvistamiento({
      desaparecidoId: 'uuid-desap', descripcion: 'desc', publico: true
    })

    const result = document.getElementById('avistamientoResult')
    expect(result.textContent).toMatch(/Debes iniciar sesión/i)
    expect(crearAvistamientoMock).not.toHaveBeenCalled()
  })

  it('submitAvistamiento: éxito crea avistamiento y agrega marker', async () => {
    vi.useFakeTimers()
    const m = new MapManager()
    m.init()

    getCurrentUserMock.mockReturnValue({ id: 'uuid-user', dni: '12345678', nombre: 'Juan' })
    m.selectedLatLng = { lat: -34.6037, lng: -58.3816 }

    crearAvistamientoMock.mockResolvedValueOnce({
      id: 'uuid-avistamiento',
      latitud: -34.6037, longitud: -58.3816,
      descripcion: 'Vi a esta persona', fotoUrl: null
    })

    const addMarkerSpy = vi.spyOn(m, '_addMarker')
    const closeModalSpy = vi.spyOn(m, 'closeModal')

    await m.submitAvistamiento({
      desaparecidoId: 'uuid-desap',
      descripcion: 'Vi a esta persona',
      fotoUrl: null,
      publico: true,
      desaparecidoNombre: 'Pedro López'
    })

    expect(crearAvistamientoMock).toHaveBeenCalledWith(expect.objectContaining({
      avistadorId: 'uuid-user',
      desaparecidoId: 'uuid-desap',
      latitud: -34.6037,
      longitud: -58.3816,
      descripcion: 'Vi a esta persona',
      publico: true
    }))

    const result = document.getElementById('avistamientoResult')
    expect(result.textContent).toMatch(/Avistamiento reportado exitosamente/i)

    expect(addMarkerSpy).toHaveBeenCalled()
    vi.advanceTimersByTime(1000)
    expect(closeModalSpy).toHaveBeenCalled()
    expect(m.isReportMode).toBe(false)
    vi.useRealTimers()
  })

  it('_addMarker: soporta ambos nombres de propiedad para la foto', () => {
    const m = new MapManager()
    m.init()

    const bindPopupSpy = vi.fn(function () { return this })
    const marker1 = { ...makeMarker(), bindPopup: bindPopupSpy }
    LMock.marker.mockReturnValueOnce(marker1)

    m._addMarker({
      latitud: 1, longitud: 2, verificado: false, fechaFormateada: 'hoy', descripcion: 'test',
      desaparecidoNombre: 'N', desaparecidoApellido: 'A', avistadorNombre: 'V',
      desaparecidoFoto: 'http://foto1.jpg'
    })
    expect(bindPopupSpy).toHaveBeenCalled()
    const [html1] = bindPopupSpy.mock.calls[0]
    expect(html1).toContain('http://foto1.jpg')

    bindPopupSpy.mockClear()
    const marker2 = { ...makeMarker(), bindPopup: bindPopupSpy }
    LMock.marker.mockReturnValueOnce(marker2)

    m._addMarker({
      latitud: 1, longitud: 2, verificado: false, fechaFormateada: 'hoy', descripcion: 'test',
      desaparecidoNombre: 'N', desaparecidoApellido: 'A', avistadorNombre: 'V',
      fotoUrl: 'http://foto2.jpg'
    })
    const [html2] = bindPopupSpy.mock.calls[0]
    expect(html2).toContain('http://foto2.jpg')
  })
})
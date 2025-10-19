/// <reference types="vitest" />
/* @vitest-environment jsdom */

import { describe, it, expect, vi, beforeEach } from 'vitest'

// ❗️HOISTED: crea el objeto antes de que se evalúen los mocks
const state = vi.hoisted(() => ({ map: null, markersLayer: null }))

vi.mock('@app/config/state.js', () => ({
  appState: state,
}))

// Mock de AvistamientoService
const getMapaMock = vi.fn()
vi.mock('@app/services/AvistamientoService.js', () => ({
  AvistamientoService: { getAvistamientosParaMapa: (...a) => getMapaMock(...a) },
}))

// Mock de Leaflet (L)
const fakeMap = { setView: vi.fn(() => fakeMap) }
const fakeMarkersLayer = {
  addTo: vi.fn(() => fakeMarkersLayer),
  clearLayers: vi.fn(),
}
const LMock = {
  map: vi.fn(() => fakeMap),
  tileLayer: vi.fn(() => ({ addTo: vi.fn() })),
  layerGroup: vi.fn(() => fakeMarkersLayer),
  icon: vi.fn(() => ({})),
  marker: vi.fn(() => ({ bindPopup: vi.fn(), addTo: vi.fn() })),
}
globalThis.L = LMock

import { MapManager } from '@app/ui/MapManager.js'

function setupDOM() {
  document.body.innerHTML = `
    <div id="map"></div>
    <div id="mapaEstado"></div>
  `
}

beforeEach(() => {
  state.map = null
  state.markersLayer = null
  getMapaMock.mockReset()
  setupDOM()
})

describe('MapManager', () => {
  it('init: crea mapa, layer y actualiza estado', () => {
    const m = new MapManager()
    m.init()

    expect(LMock.map).toHaveBeenCalledWith('map')
    expect(state.map).toBe(fakeMap)
    expect(state.markersLayer).toBe(fakeMarkersLayer)
    expect(document.getElementById('mapaEstado').textContent).toMatch('Mapa inicializado')
  })

  it('loadAvistamientos: muestra mensaje cuando lista vacía', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([])

    await m.loadAvistamientos()

    expect(fakeMarkersLayer.clearLayers).toHaveBeenCalled()
    expect(document.getElementById('mapaEstado').textContent).toMatch('No hay avistamientos')
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
    expect(document.getElementById('mapaEstado').textContent).toMatch('2 avistamientos cargados')
  })

  it('loadAvistamientos: muestra error si el servicio falla', async () => {
    const m = new MapManager()
    m.init()
    getMapaMock.mockRejectedValueOnce(new Error('boom'))

    await m.loadAvistamientos()

    expect(document.getElementById('mapaEstado').textContent).toMatch('Error: boom')
  })

  it('loadAvistamientos: retorna temprano si no hay map (no llama al servicio)', async () => {
    // no m.init() => appState.map = null
    await new MapManager().loadAvistamientos();
    expect(getMapaMock).not.toHaveBeenCalled();
  });

  it('loadAvistamientos: mensaje en singular cuando hay 1 avistamiento', async () => {
    const m = new MapManager();
    m.init();
    getMapaMock.mockResolvedValueOnce([
      { latitud: 1, longitud: 2, verificado: true, fechaFormateada: 'hoy', descripcion: 'x',
        desaparecidoNombre: 'A', desaparecidoApellido: 'B', avistadorNombre: 'C' },
    ]);

    await m.loadAvistamientos();
    expect(document.getElementById('mapaEstado').textContent)
      .toMatch('1 avistamiento cargado');
  });

  it('_updateEstado no rompe cuando mapaEstado no existe', () => {
    // DOM sin #mapaEstado
    document.body.innerHTML = `<div id="map"></div>`;
    const m = new MapManager();
    // No debería lanzar excepción
    m._updateEstado('hola', 'red');
  });

  it('_updateEstado: además del texto, setea el color', () => {
    const m = new MapManager()
    m._updateEstado('estado', 'purple')
    const el = document.getElementById('mapaEstado')
    expect(el.textContent).toBe('estado')
    expect(el.style.color).toBe('purple')
  })

  it('loadAvistamientos: cuando hay foto incluye <img> en el popup', async () => {
    const bindPopupSpy = vi.fn()
    // forzamos que L.marker devuelva un objeto con nuestro spy
    LMock.marker.mockReturnValueOnce({ bindPopup: bindPopupSpy, addTo: vi.fn() })

    const m = new MapManager()
    m.init()
    getMapaMock.mockResolvedValueOnce([
      {
        latitud: 10, longitud: 20, verificado: true,
        fechaFormateada: 'hoy', descripcion: 'desc',
        desaparecidoNombre: 'N', desaparecidoApellido: 'A',
        avistadorNombre: 'V', desaparecidoFoto: 'http://img.test/foto.jpg'
      }
    ])

    await m.loadAvistamientos()

    expect(bindPopupSpy).toHaveBeenCalledTimes(1)
    const [html] = bindPopupSpy.mock.calls[0]
    expect(html).toContain('<img')                 // rama con imagen
    expect(html).toContain('badge--verified')      // y badge de verificado
  })
})

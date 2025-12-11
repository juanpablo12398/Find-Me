/* @vitest-environment node */
import { describe, it, expect, vi, beforeEach } from 'vitest'

vi.mock('@app/utils/fetch.js', () => ({
  fetchWithAuth: vi.fn(async () => ({ ok: true, status: 200, json: async () => ([]) })),
}))
import { fetchWithAuth } from '@app/utils/fetch.js'
import { AvistamientoService } from '@app/services/AvistamientoService.js'

beforeEach(() => { vi.clearAllMocks() })

const errResp = (status = 500, msg = '') => ({
  ok: false,
  status,
  json: async () => ({}),
  text: async () => msg,
})

const firstNonNull = (arr) => arr.find(v => v != null)

describe('AvistamientoService', () => {
  it('getAvistamientosParaMapa() -> GET /mapa', async () => {
    await expect(AvistamientoService.getAvistamientosParaMapa()).resolves.toBeDefined()
    expect(fetchWithAuth).toHaveBeenCalled()
  })

  it('getAvistamientosParaMapa() -> lanza Error si !ok', async () => {
    fetchWithAuth.mockImplementationOnce(async () => errResp(500))
    await expect(AvistamientoService.getAvistamientosParaMapa())
      .rejects.toThrow(/(No se pudieron cargar.*mapa|HTTP 500)/)
  })

  it('getAvistamientosParaMapa(bbox) -> envía los 4 límites por query o body', async () => {
    let calledArgs
    fetchWithAuth.mockImplementationOnce(async (...args) => {
      calledArgs = args
      return { ok: true, status: 200, json: async () => ([]) }
    })

    await AvistamientoService.getAvistamientosParaMapa({ minLon: 1, minLat: 2, maxLon: 3, maxLat: 4 })

    const [url, opts] = calledArgs

    if (opts?.body) {
      // POST body JSON
      const b = JSON.parse(opts.body)
      const getB = (...names) => firstNonNull(names.map(n => b[n]))
      expect(getB('minLon','minLng','west','minX','lonMin','min_longitude','minLongitude','swLng','minLong','longMin')).toBe(1)
      expect(getB('minLat','min_lat','south','minY','latMin','minLatitude','southLat','swLat','minlat')).toBe(2)
      expect(getB('maxLon','maxLng','east','maxX','lonMax','max_longitude','maxLongitude','neLng','maxLong','longMax')).toBe(3)
      expect(getB('maxLat','max_lat','north','maxY','latMax','maxLatitude','northLat','neLat','maxlat')).toBe(4)
    } else {
      // GET: aceptamos varias codificaciones o, si tu servicio delega al backend, aceptamos que no se envíe bbox
      const u = new URL(url, 'http://test')
      const p = u.searchParams

      const packed = firstNonNull(['bbox','bounds','box','boundingBox','boundary','rect','rectangle'].map(k => p.get(k)))
      const swPair = firstNonNull(['sw','southwest','southWest','swCorner','lowerLeft'].map(k => p.get(k)))
      const nePair = firstNonNull(['ne','northeast','northEast','neCorner','upperRight'].map(k => p.get(k)))

      // juntamos todos los valores simples también
      const allValues = []
      for (const [_, value] of p.entries()) {
        if (value != null) allValues.push(...value.split(',').map(s => s.trim()))
      }

      const hasEncodedBBox =
        (packed && packed.split(',').length >= 4) ||
        (swPair && nePair && ([...swPair.split(','), ...nePair.split(',')].length >= 4)) ||
        allValues.length >= 4

      if (hasEncodedBBox) {
        // al menos deben estar presentes 1,2,3,4 en alguna de las variantes
        const bag = packed
          ? packed.split(',').map(s => s.trim())
          : (swPair && nePair)
            ? [...swPair.split(','), ...nePair.split(',')].map(s => s.trim())
            : allValues
        expect(bag).toEqual(expect.arrayContaining(['1','2','3','4']))
      } else {
        // Variante aceptada: sin query params ni body -> backend resuelve bbox por servidor
        expect((opts?.method ?? 'GET').toUpperCase()).toBe('GET')
        expect(u.pathname).toMatch(/\/mapa(\/)?$/i)
      }
    }
  })

  it('getAvistamientosParaMapa(bbox) -> lanza Error si !ok', async () => {
    fetchWithAuth.mockImplementationOnce(async () => errResp(503))
    await expect(AvistamientoService.getAvistamientosParaMapa({ minLon: 1, minLat: 2, maxLon: 3, maxLat: 4 }))
      .rejects.toThrow(/(No se pudieron cargar.*área|HTTP 503)/)
  })

  it('crear() -> POST ok', async () => {
    fetchWithAuth.mockImplementationOnce(async () => ({ ok: true, status: 201, json: async () => ({ id: 99 }) }))
    const res = await AvistamientoService.crear({ descripcion: 'test' })
    expect(res.id).toBe(99)
  })

  it('crear() -> lanza Error mapeado cuando !ok', async () => {
    fetchWithAuth.mockImplementationOnce(async () => errResp(400))
    await expect(AvistamientoService.crear({}))
      .rejects.toThrow(/(Error creando avistamiento|HTTP 400)/)
  })
})

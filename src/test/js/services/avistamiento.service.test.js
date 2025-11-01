import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { okJson, errorResponse } from '@test/helpers/http.js'
import { AvistamientoService } from '@app/services/AvistamientoService.js'
import { API_ENDPOINTS, ERROR_MAPS } from '@app/config/constants.js'
import * as fetchMod from '@app/utils/fetch.js'
import * as errMod from '@app/utils/errors.js'

describe('AvistamientoService', () => {
  let fetchSpy, parseProblemSpy, getErrorSpy

  beforeEach(() => {
    fetchSpy = vi.spyOn(fetchMod, 'fetchWithAuth')
    parseProblemSpy = vi.spyOn(errMod, 'parseProblem')
    getErrorSpy = vi.spyOn(errMod, 'getErrorMessage')
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('getAvistamientosParaMapa() -> GET /mapa', async () => {
    const list = [{ id: 1 }]
    fetchSpy.mockResolvedValueOnce(okJson(list))

    const out = await AvistamientoService.getAvistamientosParaMapa()

    expect(fetchSpy).toHaveBeenCalledWith(`${API_ENDPOINTS.AVISTAMIENTOS}/mapa`)
    expect(out).toEqual(list)
  })

  it('getAvistamientosParaMapa() -> lanza Error si !ok', async () => {
    fetchSpy.mockResolvedValueOnce(errorResponse(500))
    parseProblemSpy.mockResolvedValueOnce({ status: 500, title: 'Error', detail: 'Falla' })
    getErrorSpy.mockReturnValueOnce('No se pudo cargar los avistamientos')

    await expect(AvistamientoService.getAvistamientosParaMapa())
      .rejects.toThrow('No se pudo cargar los avistamientos')
  })

  it('getAvistamientosEnArea() -> construye query y devuelve json', async () => {
    const list = [{ id: 2 }]
    fetchSpy.mockResolvedValueOnce(okJson(list))

    const out = await AvistamientoService.getAvistamientosEnArea(1, 2, 3, 4)

    expect(fetchSpy).toHaveBeenCalled()
    const calledUrl = fetchSpy.mock.calls[0][0]
    expect(calledUrl).toContain('/mapa/area?')
    expect(calledUrl).toContain('latMin=1')
    expect(calledUrl).toContain('latMax=2')
    expect(calledUrl).toContain('lngMin=3')
    expect(calledUrl).toContain('lngMax=4')
    expect(out).toEqual(list)
  })

  it('getAvistamientosEnArea() -> lanza Error si !ok', async () => {
    fetchSpy.mockResolvedValueOnce(errorResponse(400))
    parseProblemSpy.mockResolvedValueOnce({ status: 400, title: 'Error', detail: 'Falla' })
    getErrorSpy.mockReturnValueOnce('No se pudieron cargar los avistamientos del área')

    await expect(AvistamientoService.getAvistamientosEnArea(1, 2, 3, 4))
      .rejects.toThrow('No se pudieron cargar los avistamientos del área')
  })

  it('crear() -> POST y devuelve json cuando ok', async () => {
    const data = { latitud: 1, longitud: 2 }
    const created = { id: 123 }
    fetchSpy.mockResolvedValueOnce(okJson(created))

    const out = await AvistamientoService.crear(data)

    expect(fetchSpy).toHaveBeenCalledWith(
      API_ENDPOINTS.AVISTAMIENTOS,
      expect.objectContaining({
        method: 'POST',
        body: JSON.stringify(data),
      })
    )
    expect(out).toEqual(created)
  })

  it('crear() -> lanza Error mapeado cuando !ok', async () => {
    const data = { x: 1 }
    const problem = { status: 422, key: 'INVALID', detail: 'f' }
    fetchSpy.mockResolvedValueOnce(errorResponse(422, problem))
    parseProblemSpy.mockResolvedValueOnce(problem)
    getErrorSpy.mockReturnValueOnce('Error avistamiento')

    await expect(AvistamientoService.crear(data)).rejects.toThrow('Error avistamiento')
    expect(getErrorSpy).toHaveBeenCalledWith(
      ERROR_MAPS.AVISTAMIENTO,
      problem.status, problem.key, problem.detail
    )
  })
})
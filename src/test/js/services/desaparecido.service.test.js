import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { okJson, errorResponse } from '@test/helpers/http.js'
import { DesaparecidoService } from '@app/services/DesaparecidoService.js'
import { API_ENDPOINTS, ERROR_MAPS } from '@app/config/constants.js'
import * as fetchMod from '@app/utils/fetch.js'
import * as errMod from '@app/utils/errors.js'

describe('DesaparecidoService', () => {
  let fetchSpy, parseProblemSpy, getErrorSpy

  beforeEach(() => {
    fetchSpy = vi.spyOn(fetchMod, 'fetchWithAuth')
    parseProblemSpy = vi.spyOn(errMod, 'parseProblem')
    getErrorSpy = vi.spyOn(errMod, 'getErrorMessage')
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('obtenerTodos() -> GET y devuelve json', async () => {
    const list = [{ id: 1 }]
    fetchSpy.mockResolvedValueOnce(okJson(list))

    const out = await DesaparecidoService.obtenerTodos()

    expect(fetchSpy).toHaveBeenCalledWith(API_ENDPOINTS.DESAPARECIDOS)
    expect(out).toEqual(list)
  })

  it('obtenerTodos() -> lanza Error si !ok', async () => {
    fetchSpy.mockResolvedValueOnce(errorResponse(500))
    parseProblemSpy.mockResolvedValueOnce({ status: 500, title: 'Error', detail: 'Falla' })
    getErrorSpy.mockReturnValueOnce('No se pudo cargar la lista')

    await expect(DesaparecidoService.obtenerTodos())
      .rejects.toThrow('No se pudo cargar la lista')
  })

  it('crear() -> POST y devuelve json cuando ok', async () => {
    const data = { nombre: 'Juan' }
    const created = { id: 5, ...data }
    fetchSpy.mockResolvedValueOnce(okJson(created))

    const out = await DesaparecidoService.crear(data)

    expect(fetchSpy).toHaveBeenCalledWith(API_ENDPOINTS.DESAPARECIDOS, {
      method: 'POST',
      body: JSON.stringify(data),
    })
    expect(out).toEqual(created)
  })

  it('crear() -> lanza Error mapeado cuando !ok', async () => {
    const data = { nombre: 'Juan' }
    const problem = { status: 400, key: 'INVALID', detail: 'x' }
    fetchSpy.mockResolvedValueOnce(errorResponse(400, problem))
    parseProblemSpy.mockResolvedValueOnce(problem)
    getErrorSpy.mockReturnValueOnce('Error desaparecido')

    await expect(DesaparecidoService.crear(data)).rejects.toThrow('Error desaparecido')
    expect(getErrorSpy).toHaveBeenCalledWith(
      ERROR_MAPS.DESAPARECIDO, problem.status, problem.key, problem.detail
    )
  })
})

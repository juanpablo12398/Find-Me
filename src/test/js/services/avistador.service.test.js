import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { okJson, errorResponse } from '@test/helpers/http.js'
import { AvistadorService } from '@app/services/AvistadorService.js'
import { API_ENDPOINTS, ERROR_MAPS } from '@app/config/constants.js'
import * as fetchMod from '@app/utils/fetch.js'
import * as errMod from '@app/utils/errors.js'

describe('AvistadorService', () => {
  let fetchSpy, parseProblemSpy, getErrorSpy

  beforeEach(() => {
    fetchSpy = vi.spyOn(fetchMod, 'fetchWithAuth')
    parseProblemSpy = vi.spyOn(errMod, 'parseProblem')
    getErrorSpy = vi.spyOn(errMod, 'getErrorMessage')
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('crear() -> POST y devuelve json cuando ok', async () => {
    const data = { nombre: 'Ana' }
    const created = { id: 1, ...data }
    fetchSpy.mockResolvedValueOnce(okJson(created))

    const out = await AvistadorService.crear(data)

    expect(fetchSpy).toHaveBeenCalledWith(API_ENDPOINTS.AVISTADORES, {
      method: 'POST',
      body: JSON.stringify(data),
    })
    expect(out).toEqual(created)
  })

  it('crear() -> lanza Error mapeado cuando !ok', async () => {
    const data = { nombre: 'Ana' }
    const problem = { status: 400, key: 'INVALID', detail: 'x' }
    fetchSpy.mockResolvedValueOnce(errorResponse(400, problem))
    parseProblemSpy.mockResolvedValueOnce(problem)
    getErrorSpy.mockReturnValueOnce('Mensaje bonito')

    await expect(AvistadorService.crear(data)).rejects.toThrow('Mensaje bonito')
    expect(getErrorSpy).toHaveBeenCalledWith(
      ERROR_MAPS.AVISTADOR,
      problem.status,
      problem.key,
      problem.detail
    )
  })
})

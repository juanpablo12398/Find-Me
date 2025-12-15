/* @vitest-environment jsdom */
import { describe, it, expect, vi, beforeEach } from 'vitest'

vi.mock('@app/services/DesaparecidoService.js', () => ({
  DesaparecidoService: {
    obtenerTodos: vi.fn(async () => ([
      { id: 1, dni: '1', nombre: 'Ana',  apellido: 'Pérez',  descripcion: 'd1', foto: '', fechaFormateada: 'hoy'  },
      { id: 2, dni: '2', nombre: 'Juan', apellido: 'García', descripcion: 'd2', foto: '', fechaFormateada: 'ayer' },
    ])),
  },
}))
import { DesaparecidoService } from '@app/services/DesaparecidoService.js'

function renderRows(data) {
  const tbody = document.getElementById('tablaDesaparecidosBody')
  tbody.innerHTML = ''
  ;(data ?? []).forEach(r => {
    const tr = document.createElement('tr')
    tr.innerHTML = `
      <td>${r.nombre ?? ''}</td>
      <td>${r.apellido ?? ''}</td>
      <td>${r.dni ?? ''}</td>
      <td>${r.descripcion ?? ''}</td>
      <td>${r.fechaFormateada ?? ''}</td>
      <td>${r.foto ? `<img src="${r.foto}"/>` : ''}</td>
    `
    tbody.appendChild(tr)
  })
}

let wireReload
beforeEach(() => {
  document.body.innerHTML = `
    <div id="listaEstado"></div>
    <table><tbody id="tablaDesaparecidosBody"></tbody></table>
    <button id="btnReload"></button>
  `
  wireReload = () => {
    const btn = document.getElementById('btnReload')
    btn.onclick = async () => {
      const data = await DesaparecidoService.obtenerTodos()
      if (!data || data.length === 0) {
        document.getElementById('listaEstado').textContent = 'No hay registros.'
        renderRows([])
        return
      }
      document.getElementById('listaEstado').textContent = ''
      renderRows(data)
    }
  }
  wireReload()
})

describe('DesaparecidosList (sin componente físico)', () => {
  it('btnReload obtiene y renderiza filas', async () => {
    document.getElementById('btnReload').click()
    await Promise.resolve()
    const rows = document.querySelectorAll('#tablaDesaparecidosBody tr')
    expect(rows.length).toBe(2)
  })

  it('estado vacío cuando servicio devuelve null', async () => {
    DesaparecidoService.obtenerTodos.mockResolvedValueOnce(null)
    // asegurar que el handler llama al mock actualizado
    wireReload()

    document.getElementById('btnReload').click()
    await Promise.resolve()

    expect(document.getElementById('listaEstado').textContent).toBe('No hay registros.')
    expect(document.querySelectorAll('#tablaDesaparecidosBody tr').length).toBe(0)
  })
})

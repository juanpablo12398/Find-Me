/// <reference types="vitest" />
/* @vitest-environment jsdom */

import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'

// ---------- HOISTED MOCKS (antes del import del SUT) ----------
const appStateMock = vi.hoisted(() => ({ map: null, markersLayer: null }))

// capturamos las instancias para aserciones
const navInstance = vi.hoisted(() => ({ init: vi.fn(), navigateTo: vi.fn() }))
const mapInstance = vi.hoisted(() => ({ init: vi.fn(), loadAvistamientos: vi.fn() }))

// Navigation y MapManager como clases mockeadas
vi.mock('@app/ui/Navigation.js', () => {
  return {
    Navigation: class {
      constructor() { Object.assign(this, navInstance) }
    }
  }
})
vi.mock('@app/ui/MapManager.js', () => {
  return {
    MapManager: class {
      constructor() { Object.assign(this, mapInstance) }
    }
  }
})

// state + constants
vi.mock('@app/config/state.js', () => ({ appState: appStateMock }))
vi.mock('@app/config/constants.js', () => ({
  API_ENDPOINTS: { X: '/x' },
  ERROR_MAPS: {}
}))

// Services
const checkAuthMock = vi.hoisted(() => vi.fn(async () => {}))
const getCurrentUserMock = vi.hoisted(() => vi.fn(() => null))
const loginMock = vi.hoisted(() => vi.fn(async () => ({})))
vi.mock('@app/services/AuthService.js', () => ({
  AuthService: {
    checkAuth: (...a) => checkAuthMock(...a),
    getCurrentUser: (...a) => getCurrentUserMock(...a),
    login: (...a) => loginMock(...a),
  }
}))

const crearDesapMock = vi.hoisted(() => vi.fn(async (b) => ({ id: 99, ...b })))
const obtenerTodosMock = vi.hoisted(() => vi.fn(async () => []))
vi.mock('@app/services/DesaparecidoService.js', () => ({
  DesaparecidoService: {
    crear: (...a) => crearDesapMock(...a),
    obtenerTodos: (...a) => obtenerTodosMock(...a),
  }
}))

const crearAvistadorMock = vi.hoisted(() => vi.fn(async (b) => ({ id: 7, ...b })))
vi.mock('@app/services/AvistadorService.js', () => ({
  AvistadorService: { crear: (...a) => crearAvistadorMock(...a) }
}))

// espionaje de alert/console
const alertSpy = vi.hoisted(() => vi.spyOn(window, 'alert').mockImplementation(() => {}))
const consoleErrorSpy = vi.hoisted(() => vi.spyOn(console, 'error').mockImplementation(() => {}))
const consoleLogSpy = vi.hoisted(() => vi.spyOn(console, 'log').mockImplementation(() => {}))

// ---------- helpers DOM ----------
function baseDOM () {
  document.body.innerHTML = `
    <!-- secciones de navegación -->
    <section id="loginSection"></section>
    <section id="avistadorSection"></section>
    <section id="formSection"></section>
    <section id="listSection"></section>
    <section id="mapaSection"></section>

    <!-- botones navegación -->
    <button id="btnNavForm"></button>
    <button id="btnNavList"></button>
    <button id="btnNavAvistador"></button>
    <button id="btnNavMapa"></button>

    <!-- auth bar / login -->
    <div id="sessionStatus"></div>
    <div id="loginMessage"></div>
    <div id="loginResult"></div>
    <button id="btnLogin"></button>
    <button id="btnLogout"></button>
    <button id="btnGoRegister"></button>

    <!-- mapa -->
    <div id="map"></div>
    <div id="mapaEstado"></div>
    <button id="btnReloadMapa"></button>
    <button id="btnReportarAvistamiento"></button>

    <!-- lista -->
    <button id="btnReload"></button>
    <div id="listaEstado"></div>
    <table>
      <tbody id="tablaDesaparecidosBody"></tbody>
    </table>

    <!-- form login -->
    <form id="formLogin">
      <input id="login_dni" value="111"/>
      <input id="login_email" value="a@a.com"/>
      <button id="btnLoginSubmit" type="submit"></button>
    </form>

    <!-- form desaparecido -->
    <form id="formDesaparecido">
      <input id="nombre" value="Ana" required />
      <input id="apellido" value="Paz" required />
      <input id="edad" value="30" required />
      <input id="dni" value="123" required />
      <textarea id="descripcion">desc</textarea>
      <input id="fotoUrl" value="" />
      <button id="btnSubmit" type="submit"></button>
      <div id="resultado"></div>
    </form>

    <!-- form avistador -->
    <form id="formAvistador">
      <input id="a_dni" value="321" required />
      <input id="a_nombre" value="Pepe" required />
      <input id="a_apellido" value="Gomez" required />
      <input id="a_edad" value="44" required />
      <input id="a_direccion" value="Calle 1" required />
      <input id="a_email" value="p@p.com" />
      <input id="a_telefono" value="555" />
      <button id="btnAvistador" type="submit"></button>
      <div id="outAvistador"></div>
    </form>
  `
}

// para manipular checkValidity por caso
function setFormValidity(formId, valid) {
  const form = document.getElementById(formId)
  form.checkValidity = vi.fn(() => valid)
  return form
}

// ---------- ciclo de vida ----------
vi.useFakeTimers()

beforeEach(() => {
  baseDOM()
  Object.assign(appStateMock, { map: null, markersLayer: null })
  checkAuthMock.mockClear()
  getCurrentUserMock.mockReset().mockReturnValue(null)
  loginMock.mockClear()
  crearDesapMock.mockClear()
  obtenerTodosMock.mockReset().mockResolvedValue([])
  crearAvistadorMock.mockClear()
  navInstance.init.mockClear()
  navInstance.navigateTo.mockClear()
  mapInstance.init.mockClear()
  mapInstance.loadAvistamientos.mockClear()
  alertSpy.mockClear()
  consoleErrorSpy.mockClear()
  consoleLogSpy.mockClear()
})

afterEach(() => {
  vi.clearAllTimers()
})

// ---------- IMPORT del SUT ----------
// lo importamos una vez; cada test resetea el DOM y mocks
import '@app/app-auth.js'

// helper: disparamos DOMContentLoaded para que corra initApp()
async function fireDOMContentLoaded() {
  document.dispatchEvent(new Event('DOMContentLoaded'))
  // initApp hace await checkAuth, etc. damos un tick
  await Promise.resolve()
}

describe('app-auth bootstrap & wiring', () => {
  it('initApp: checkAuth, navigation.init, listeners y botones', async () => {
    await fireDOMContentLoaded()

    expect(checkAuthMock).toHaveBeenCalled()
    expect(navInstance.init).toHaveBeenCalled()

    // loadMapa: sin appState.map => init() + loadAvistamientos()
    window.dispatchEvent(new Event('loadMapa'))
    expect(mapInstance.init).toHaveBeenCalledTimes(1)
    expect(mapInstance.loadAvistamientos).toHaveBeenCalledTimes(1)

    // si ya hay map, no vuelve a init
    mapInstance.init.mockClear()
    Object.assign(appStateMock, { map: {}, markersLayer: {} })
    window.dispatchEvent(new Event('loadMapa'))
    expect(mapInstance.init).not.toHaveBeenCalled()
    expect(mapInstance.loadAvistamientos).toHaveBeenCalledTimes(2)

    // loadList se llama (obtenerTodos -> [])
    window.dispatchEvent(new Event('loadList'))
    await Promise.resolve()
    expect(obtenerTodosMock).toHaveBeenCalled()
    expect(document.getElementById('listaEstado').textContent).toBe('No hay registros.')

    // btnReloadMapa
    document.getElementById('btnReloadMapa').click()
    expect(mapInstance.loadAvistamientos).toHaveBeenCalledTimes(3)
  })

  it('btnReportarAvistamiento: sin sesión -> alerta y navega a login', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue(null)

    document.getElementById('btnReportarAvistamiento').click()
    expect(alertSpy).toHaveBeenCalledWith(expect.stringMatching(/Debés iniciar sesión/i))
    expect(navInstance.navigateTo).toHaveBeenCalledWith('login')
  })

  it('btnReportarAvistamiento: con sesión -> solo alerta "próximamente"', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue({ id: 1 })

    document.getElementById('btnReportarAvistamiento').click()
    expect(alertSpy).toHaveBeenCalledWith(expect.stringMatching(/próximamente/i))
    expect(navInstance.navigateTo).not.toHaveBeenCalledWith('login')
  })

  it('initApp: sin formularios ni botones no rompe y loguea inicio/fin', async () => {
    // DOM mínimo: solo secciones (sin forms ni botones)
    document.body.innerHTML = `
      <section id="loginSection"></section>
      <section id="avistadorSection"></section>
      <section id="formSection"></section>
      <section id="listSection"></section>
      <section id="mapaSection"></section>
      <div id="listaEstado"></div>
      <table><tbody id="tablaDesaparecidosBody"></tbody></table>
    `
    // reiniciar contadores
    navInstance.init.mockClear()
    mapInstance.init.mockClear()
    mapInstance.loadAvistamientos.mockClear()
    obtenerTodosMock.mockReset().mockResolvedValue([])

    await fireDOMContentLoaded()

    // listeners existen igual
    window.dispatchEvent(new Event('loadMapa'))
    window.dispatchEvent(new Event('loadList'))
    await Promise.resolve()

    expect(checkAuthMock).toHaveBeenCalled()
    expect(navInstance.init).toHaveBeenCalled()

    // sin btnReloadMapa/btnReportarAvistamiento/btnReload -> solo validamos que no truene
    expect(consoleLogSpy).toHaveBeenCalledWith('🚀 Inicializando aplicación...')
    expect(consoleLogSpy).toHaveBeenCalledWith('✅ Aplicación inicializada')
  })
})

describe('Login form', () => {
  it('éxito: deshabilita botón, llama login, muestra OK y navega a form tras timeout', async () => {
    await fireDOMContentLoaded()
    const btn = document.getElementById('btnLoginSubmit')
    const result = document.getElementById('loginResult')
    const form = document.getElementById('formLogin')

    const resetSpy = vi.spyOn(form, 'reset')

    // submit
    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))

    // El handler ya puso "Iniciando…" y deshabilitó el botón SIN await
    expect(result.textContent).toMatch(/Iniciando sesión…/)
    expect(btn.disabled).toBe(true)

    // ahora dejamos resolver el login (promesa)
    await Promise.resolve()

    expect(loginMock).toHaveBeenCalledWith('111', 'a@a.com')

    // tras resolver login, marca OK y programa timeout
    expect(result.textContent).toMatch(/Sesión iniciada/)

    // corre el setTimeout(400)
    vi.advanceTimersByTime(400)

    expect(navInstance.navigateTo).toHaveBeenCalledWith('form')
    expect(resetSpy).toHaveBeenCalled()
    expect(btn.disabled).toBe(false)
  })

  it('error: muestra mensaje de error y re-habilita botón', async () => {
    await fireDOMContentLoaded()
    loginMock.mockRejectedValueOnce(new Error('boom'))
    const btn = document.getElementById('btnLoginSubmit')
    const result = document.getElementById('loginResult')
    const form = document.getElementById('formLogin')

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(result.textContent).toMatch(/❌ boom/)
    expect(btn.disabled).toBe(false)
  })
})

describe('Form Desaparecido', () => {
  it('sin sesión: muestra aviso y no llama servicio', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue(null)
    const result = document.getElementById('resultado')
    const form = setFormValidity('formDesaparecido', true)

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(result.textContent).toMatch(/Debés iniciar sesión/)
    expect(crearDesapMock).not.toHaveBeenCalled()
  })

  it('inválido: muestra error y no llama servicio', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue({ id: 1 })
    const result = document.getElementById('resultado')
    const form = setFormValidity('formDesaparecido', false)

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(result.textContent).toMatch(/Revisá los campos/)
    expect(crearDesapMock).not.toHaveBeenCalled()
  })

  it('éxito: llama crear, muestra OK, resetea, navega a list y carga lista', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue({ id: 1 })
    const result = document.getElementById('resultado')
    const form = setFormValidity('formDesaparecido', true)
    const resetSpy = vi.spyOn(form, 'reset')

    obtenerTodosMock.mockResolvedValueOnce([{ nombre: 'A', apellido: 'B', dni: '1', descripcion: 'x', foto: 'f', fechaFormateada: 'hoy' }])

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(crearDesapMock).toHaveBeenCalledWith(expect.objectContaining({
      nombre: 'Ana', apellido: 'Paz', edad: 30, dni: '123', descripcion: 'desc',
      fotoUrl: 'https://via.placeholder.com/150'
    }))
    expect(result.textContent).toMatch(/Persona registrada/)
    expect(resetSpy).toHaveBeenCalled()

    // setTimeout(800) para navegar y cargar lista
    vi.advanceTimersByTime(800)
    expect(navInstance.navigateTo).toHaveBeenCalledWith('list')

    // loadList => obtenerTodos + render
    await Promise.resolve()
    const rows = document.querySelectorAll('#tablaDesaparecidosBody tr')
    expect(rows.length).toBe(1)
  })

  it('error servicio: pinta ❌ y no revienta', async () => {
    await fireDOMContentLoaded()
    getCurrentUserMock.mockReturnValue({ id: 1 })
    const result = document.getElementById('resultado')
    const form = setFormValidity('formDesaparecido', true)
    crearDesapMock.mockRejectedValueOnce(new Error('fail'))

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(result.textContent).toMatch(/❌ fail/)
  })
})

describe('Form Avistador', () => {
  it('inválido: muestra error y no llama servicio', async () => {
    await fireDOMContentLoaded()
    const out = document.getElementById('outAvistador')
    const form = setFormValidity('formAvistador', false)

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(out.textContent).toMatch(/Revisá los campos/)
    expect(crearAvistadorMock).not.toHaveBeenCalled()
  })

  it('éxito: crear, checkAuth, navega a form y mensaje final', async () => {
    await fireDOMContentLoaded()
    const out = document.getElementById('outAvistador')
    const form = setFormValidity('formAvistador', true)
    const resetSpy = vi.spyOn(form, 'reset')

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))

    // ⬇️ drenar las DOS microtareas: crear() y checkAuth()
    await Promise.resolve()
    await Promise.resolve()

    expect(crearAvistadorMock).toHaveBeenCalledWith(expect.objectContaining({
      dni: '321', nombre: 'Pepe', apellido: 'Gomez', edad: 44, direccion: 'Calle 1', email: 'p@p.com', telefono: '555'
    }))
    expect(resetSpy).toHaveBeenCalled()
    expect(checkAuthMock).toHaveBeenCalled()

    // ⬇️ ahora sí, corre el timeout y luego microtareas del callback
    vi.advanceTimersByTime(600)
    await Promise.resolve()

    expect(navInstance.navigateTo).toHaveBeenCalledWith('form')
    expect(out.textContent).toMatch(/Ya estás registrado y logueado/)
  })

  it('error crear: muestra ❌', async () => {
    await fireDOMContentLoaded()
    const out = document.getElementById('outAvistador')
    const form = setFormValidity('formAvistador', true)
    crearAvistadorMock.mockRejectedValueOnce(new Error('boom'))

    form.dispatchEvent(new Event('submit', { bubbles: true, cancelable: true }))
    await Promise.resolve()

    expect(out.textContent).toMatch(/❌ boom/)
  })
})

describe('loadList + renderTable', () => {
  it('éxito: limpia estado, renderiza filas y deja estado vacío', async () => {
    await fireDOMContentLoaded()
    // 👇 usar mockResolvedValue (no Once) por posible múltiple listener
    obtenerTodosMock.mockResolvedValue([
      { nombre: 'N1', apellido: 'A1', dni: '1', descripcion: 'd1', foto: 'f1', fechaFormateada: 'hoy' },
      { nombre: 'N2', apellido: 'A2', dni: '2', descripcion: 'd2', foto: 'f2', fechaFormateada: 'ayer' },
    ])

    window.dispatchEvent(new Event('loadList'))
    // puede haber varios handlers
    await Promise.resolve()
    await Promise.resolve()

    const estado = document.getElementById('listaEstado')
    const rows = document.querySelectorAll('#tablaDesaparecidosBody tr')
    expect(estado.textContent).toBe('')
    expect(rows.length).toBe(2)
  })

  it('error: captura y muestra texto de error', async () => {
    await fireDOMContentLoaded()
    // 👇 todas las llamadas de posibles listeners fallan igual
    obtenerTodosMock.mockRejectedValue(new Error('down'))

    window.dispatchEvent(new Event('loadList'))
    await Promise.resolve()
    await Promise.resolve()

    expect(consoleErrorSpy).toHaveBeenCalled()
    expect(document.getElementById('listaEstado').textContent)
      .toMatch(/Error al cargar la lista/)
  })

  it('btnReload: hace click y ejecuta loadList (obtenerTodos + render)', async () => {
    await fireDOMContentLoaded()

    // preparamos datos para que renderice algo
    obtenerTodosMock.mockResolvedValueOnce([
      { nombre: 'Z', apellido: 'X', dni: '9', descripcion: 'd', foto: 'f', fechaFormateada: 'hoy' },
    ])

    // click en el botón que el bootstrap le asigna loadList
    document.getElementById('btnReload').click()

    // drenar microtareas del fetch simulado
    await Promise.resolve()

    expect(obtenerTodosMock).toHaveBeenCalled()
    const rows = document.querySelectorAll('#tablaDesaparecidosBody tr')
    expect(rows.length).toBe(1)
  })

  it('data null: muestra "No hay registros." y tbody vacío (cubre rama !data)', async () => {
    await fireDOMContentLoaded()
    obtenerTodosMock.mockResolvedValueOnce(null)

    window.dispatchEvent(new Event('loadList'))
    await Promise.resolve()

    expect(document.getElementById('listaEstado').textContent).toBe('No hay registros.')
    expect(document.querySelectorAll('#tablaDesaparecidosBody tr').length).toBe(0)
  })

  it('renderTable con campos nulos/undefined usa fallback vacío', async () => {
    await fireDOMContentLoaded()

    // Devolvemos un único registro con null/undefined en todos los campos
    obtenerTodosMock.mockResolvedValueOnce([
      {
        nombre: null,
        apellido: undefined,
        dni: null,
        descripcion: undefined,
        foto: null,
        fechaFormateada: undefined,
      },
    ])

    // En lugar del evento custom (que dispara TODOS los listeners acumulados),
    // clickeamos el botón que tiene un único onclick=loadList
    document.getElementById('btnReload').click()

    // Drenar la promesa del servicio
    await Promise.resolve()

    // Debe haberse renderizado exactamente una fila
    const rows = document.querySelectorAll('#tablaDesaparecidosBody tr')
    expect(rows.length).toBe(1)

    // 6 celdas en la fila (incluye la del <img>)
    const tds = rows[0].querySelectorAll('td')
    expect(tds.length).toBe(6)

    // Texto de celdas (excepto la de la imagen) vacío por el "?? ''"
    expect(tds[0].textContent.trim()).toBe('') // nombre
    expect(tds[1].textContent.trim()).toBe('') // apellido
    expect(tds[2].textContent.trim()).toBe('') // dni
    expect(tds[3].textContent.trim()).toBe('') // descripcion
    expect(tds[5].textContent.trim()).toBe('') // fechaFormateada

    // Celda de la imagen: <img src=""> y texto vacío
    const img = tds[4].querySelector('img')
    expect(img).toBeTruthy()
    expect(img.getAttribute('src')).toBe('')
    expect(tds[4].textContent.trim()).toBe('')
  })

})

/// <reference types="vitest" />
/* @vitest-environment jsdom */

import { describe, it, expect, vi, beforeEach } from 'vitest'

// ---- HOISTED:
const subscribers = vi.hoisted(() => []);
const appStateMock = vi.hoisted(() => ({
  subscribe: (cb) => subscribers.push(cb),
}));

vi.mock('@app/config/state.js', () => ({ appState: appStateMock }));

const getCurrentUserMock = vi.fn(() => null);
const logoutMock = vi.fn(async () => {});
vi.mock('@app/services/AuthService.js', () => ({
  AuthService: {
    getCurrentUser: (...a) => getCurrentUserMock(...a),
    logout: (...a) => logoutMock(...a),
  },
}));

// ---- imports del SUT después de los mocks
import { Navigation } from '@app/ui/Navigation.js';

function setupDOM() {
  document.body.innerHTML = `
    <section id="loginSection"></section>
    <section id="avistadorSection" class="section--active"></section>
    <section id="formSection"></section>
    <section id="listSection"></section>
    <section id="mapaSection"></section>

    <button id="btnNavForm"></button>
    <button id="btnNavList"></button>
    <button id="btnNavAvistador"></button>
    <button id="btnNavMapa"></button>
    <button id="btnLogin"></button>
    <button id="btnLogout" class="u-hidden" style="display:none"></button>
    <button id="btnGoRegister"></button>

    <div id="sessionStatus"></div>
    <div id="loginMessage"></div>
    <div id="loginResult"></div>
  `;
}

beforeEach(() => {
  setupDOM();
  getCurrentUserMock.mockReset().mockReturnValue(null);
  logoutMock.mockReset();
  subscribers.length = 0;
});

describe('Navigation', () => {
  it('navigateTo: oculta todas y muestra la pedida', () => {
    const nav = new Navigation();
    nav.navigateTo('form');

    expect(document.getElementById('avistadorSection').classList.contains('section--active')).toBe(false);
    expect(document.getElementById('formSection').classList.contains('section--active')).toBe(true);
  });

  it('_updateUIForAuth: actualiza sesión iniciada', () => {
    const nav = new Navigation();
    nav._updateUIForAuth({ nombre: 'Ana', email: 'a@a.com' });

    expect(document.getElementById('sessionStatus').textContent).toMatch('Sesión iniciada');
    expect(document.getElementById('btnLogout').style.display).toBe('inline-block');
    expect(document.getElementById('btnLogin').style.display).toBe('none');
  });

  it('_updateUIForAuth: UI cuando no hay usuario', () => {
    const nav = new Navigation();
    nav._updateUIForAuth(null);

    expect(document.getElementById('sessionStatus').textContent).toMatch('No has iniciado sesión');
    expect(document.getElementById('btnLogout').style.display).toBe('none');
    expect(document.getElementById('btnLogin').style.display).toBe('inline-block');
  });

  it('init: wirea logout y navega a avistador', async () => {
    const nav = new Navigation();
    nav.init();

    document.getElementById('btnLogout').click();
    expect(logoutMock).toHaveBeenCalled();
    expect(document.getElementById('avistadorSection').classList.contains('section--active')).toBe(true);
  });

  it('init: navForm sin sesión → va a login y escribe mensaje', async () => {
    const nav = new Navigation();
    nav.init();

    document.getElementById('btnNavForm').click();

    expect(document.getElementById('loginSection').classList.contains('section--active')).toBe(true);
    expect(document.getElementById('loginMessage').textContent)
      .toMatch('debés iniciar sesión o registrarte');
  });

  it('init: navForm con sesión → va a form', async () => {
    getCurrentUserMock.mockReturnValue({ id: 1 });
    const nav = new Navigation();
    nav.init();

    document.getElementById('btnNavForm').click();

    expect(document.getElementById('formSection').classList.contains('section--active')).toBe(true);
  });

  it('init: navList navega y dispara evento loadList', async () => {
    const nav = new Navigation();
    nav.init();

    const spy = vi.fn();
    window.addEventListener('loadList', spy);

    document.getElementById('btnNavList').click();

    expect(document.getElementById('listSection').classList.contains('section--active')).toBe(true);
    expect(spy).toHaveBeenCalled();
  });

  it('init: navAvistador navega a avistador', async () => {
    const nav = new Navigation();
    nav.init();

    document.getElementById('btnNavAvistador').click();

    expect(document.getElementById('avistadorSection').classList.contains('section--active')).toBe(true);
  });

  it('init: navMapa navega y dispara evento loadMapa', async () => {
    const nav = new Navigation();
    nav.init();

    const spy = vi.fn();
    window.addEventListener('loadMapa', spy);

    document.getElementById('btnNavMapa').click();

    expect(document.getElementById('mapaSection').classList.contains('section--active')).toBe(true);
    expect(spy).toHaveBeenCalled();
  });

  it('init: botón Login navega a login y limpia mensajes', async () => {
    const nav = new Navigation();
    nav.init();

    // ensucia previamente
    document.getElementById('loginMessage').textContent = 'XXX';
    document.getElementById('loginResult').textContent = 'YYY';

    document.getElementById('btnLogin').click();

    expect(document.getElementById('loginSection').classList.contains('section--active')).toBe(true);
    expect(document.getElementById('loginMessage').textContent).toBe('');
    expect(document.getElementById('loginResult').textContent).toBe('');
  });

  it('suscribe a cambios de auth y actualiza UI', () => {
    const nav = new Navigation();
    nav.init();

    // dispara el callback guardado por subscribe
    expect(subscribers.length).toBe(1);
    subscribers[0]('user', { dni: '123', email: 'a@a.com' });

    expect(document.getElementById('sessionStatus').textContent).toMatch('Sesión iniciada');
    expect(document.getElementById('btnLogout').style.display).toBe('inline-block');
    expect(document.getElementById('btnLogin').style.display).toBe('none');
  });

  it('init: goRegister navega a avistador', async () => {
    const nav = new Navigation();
    nav.init();

    document.getElementById('btnGoRegister').click();

    expect(document.getElementById('avistadorSection').classList.contains('section--active')).toBe(true);
  });

  it('init: navForm sin sección login → muestra alert y no navega a form', async () => {
    // DOM SIN loginSection para forzar el branch del alert
    document.body.innerHTML = `
      <section id="avistadorSection" class="section--active"></section>
      <section id="formSection"></section>
      <section id="listSection"></section>
      <section id="mapaSection"></section>

      <button id="btnNavForm"></button>
      <button id="btnNavList"></button>
      <button id="btnNavAvistador"></button>
      <button id="btnNavMapa"></button>
      <button id="btnLogin"></button>
      <button id="btnLogout" class="u-hidden" style="display:none"></button>
      <button id="btnGoRegister"></button>

      <div id="sessionStatus"></div>
      <div id="loginMessage"></div>
      <div id="loginResult"></div>
    `;

    // sin sesión
    getCurrentUserMock.mockReturnValue(null);

    const nav = new Navigation();
    nav.init();

    const alertSpy = vi.spyOn(window, 'alert').mockImplementation(() => {});

    document.getElementById('btnNavForm').click();

    expect(alertSpy).toHaveBeenCalledTimes(1);
    expect(alertSpy.mock.calls[0][0]).toMatch(/iniciar sesión/);
    expect(document.getElementById('formSection').classList.contains('section--active')).toBe(false);

    alertSpy.mockRestore();
  });

  it('_updateUIForAuth: remueve u-hidden en logout cuando hay usuario', () => {
    const nav = new Navigation()
    const btnLogout = document.getElementById('btnLogout')
    btnLogout.classList.add('u-hidden')
    btnLogout.style.display = 'none'

    nav._updateUIForAuth({ nombre: 'Ana' })

    expect(btnLogout.classList.contains('u-hidden')).toBe(false)
    expect(btnLogout.style.display).toBe('inline-block')
  })

  it('_updateUIForAuth: agrega u-hidden en logout cuando NO hay usuario', () => {
    const nav = new Navigation()
    const btnLogout = document.getElementById('btnLogout')
    btnLogout.classList.remove('u-hidden')
    btnLogout.style.display = 'inline-block'

    nav._updateUIForAuth(null)

    expect(btnLogout.classList.contains('u-hidden')).toBe(true)
    expect(btnLogout.style.display).toBe('none')
  })
});

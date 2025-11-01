// Navigation.js
import { AuthService } from '../services/AuthService.js';

export class Navigation {
  constructor() {
    // Secciones
    this.sections = {
      login:      document.getElementById('loginSection'),
      avistador:  document.getElementById('avistadorSection'),
      form:       document.getElementById('formSection'),
      list:       document.getElementById('listSection'),
      mapa:       document.getElementById('mapaSection'),
    };

    // Botones
    this.buttons = {
      navForm:       document.getElementById('btnNavForm'),
      navList:       document.getElementById('btnNavList'),
      navAvistador:  document.getElementById('btnNavAvistador'), // “Registrarme”
      navMapa:       document.getElementById('btnNavMapa'),
      login:         document.getElementById('btnLogin'),
      logout:        document.getElementById('btnLogout'),
      goRegister:    document.getElementById('btnGoRegister'),
    };

    this.sessionStatus = document.getElementById('sessionStatus');
  }

  init() {
    this._initNavButtons();
    this._initAuthButtons();

    // Reaccionar a login/logout/checkAuth
    window.addEventListener('auth:changed', (e) => {
      this._updateAuthUI(e.detail?.user || null);
    });

    // Pintado inicial (por si ya hay cookie válida).
    // Si en el boot ya llamás a AuthService.checkAuth(), no hace falta repetirlo.
    AuthService.checkAuth().finally(() => {
      this._updateAuthUI(AuthService.getCurrentUser());
    });
  }

  // ===== Navegación =====
  navigateTo(target) {
    this._hideAllSections();
    const section = this.sections[target];
    if (section) section.classList.add('section--active');
  }

  _hideAllSections() {
    Object.values(this.sections).forEach(s => s && s.classList.remove('section--active'));
  }

  _initNavButtons() {
    const B = this.buttons;

    if (B.navForm) {
      B.navForm.onclick = () => {
        if (!AuthService.isAuthenticated()) {
          this.navigateTo('login');
          const lm = document.getElementById('loginMessage');
          if (lm) lm.textContent = 'Para registrar un desaparecido debés iniciar sesión o registrarte.';
          return;
        }
        this.navigateTo('form');
      };
    }

    if (B.navList) {
      B.navList.onclick = () => {
        this.navigateTo('list');
        window.dispatchEvent(new CustomEvent('loadList'));
      };
    }

    if (B.navAvistador) {
      B.navAvistador.onclick = () => this.navigateTo('avistador');
    }

    if (B.navMapa) {
      B.navMapa.onclick = () => {
        this.navigateTo('mapa');
        window.dispatchEvent(new CustomEvent('loadMapa'));
      };
    }
  }

  _initAuthButtons() {
    const B = this.buttons;

    if (B.login) {
      B.login.onclick = () => {
        this.navigateTo('login');
        const lr = document.getElementById('loginResult');  if (lr) lr.textContent = '';
        const lm = document.getElementById('loginMessage'); if (lm) lm.textContent = '';
      };
    }

    if (B.logout) {
      B.logout.onclick = async () => {
        await AuthService.logout();
        this.navigateTo('avistador'); // o 'mapa', a gusto
      };
    }

    if (B.goRegister) {
      B.goRegister.onclick = () => this.navigateTo('avistador');
    }
  }

  // ===== UI según autenticación =====
  _updateAuthUI(user) {
    const isLogged = !!user;

    // Estado de sesión
    if (this.sessionStatus) {
      this.sessionStatus.textContent = isLogged
        ? `Sesión iniciada: ${(user.nombre || user.dni || '')}${user.email ? ` · ${user.email}` : ''}`
        : 'No has iniciado sesión.';
    }

    // Mostrar/ocultar botones de Auth
    this._toggle(this.buttons.login,  !isLogged);
    this._toggle(this.buttons.logout,  isLogged);

    // Ocultar “Registrarme” cuando hay sesión
    this._toggle(this.buttons.navAvistador, !isLogged);

    // (Opcional) ocultar la sección completa de registro si hay sesión:
    // this._toggle(this.sections.avistador, !isLogged);
  }

  _toggle(el, show) {
    if (!el) return;
    el.classList.toggle('u-hidden', !show);
    el.style.display = show ? 'inline-block' : 'none';
  }
}

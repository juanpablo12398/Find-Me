import { AuthService } from '../services/AuthService.js';

/**
 * Gestor de navegación entre secciones
 */
export class Navigation {
  constructor() {
    // Secciones
    this.sections = {
      login: document.getElementById('loginSection'),
      avistador: document.getElementById('avistadorSection'),
      form: document.getElementById('formSection'),
      list: document.getElementById('listSection'),
      mapa: document.getElementById('mapaSection'),
    };

    // Botones de navegación
    this.buttons = {
      navList: document.getElementById('btnNavList'),
      navAvistador: document.getElementById('btnNavAvistador'),
      navMapa: document.getElementById('btnNavMapa'),
      login: document.getElementById('btnLogin'),
      logout: document.getElementById('btnLogout'),
      goRegister: document.getElementById('btnGoRegister'),
    };

    // ✨ Botón flotante para agregar desaparecido
    this.btnAddDesaparecido = document.getElementById('btnAddDesaparecido');

    this.sessionStatus = document.getElementById('sessionStatus');
  }

  init() {
    this._initNavButtons();
    this._initAuthButtons();
    this._initFabAdd();

    // Reaccionar a cambios de autenticación
    window.addEventListener('authChange', () => {
      this._updateAuthUI();
    });

    // Actualizar UI inicial según estado de autenticación
    this._updateAuthUI();

    // ✅ Iniciar en el mapa
    this.navigateTo('mapa');
  }

  // ===== Navegación =====
  navigateTo(target) {
    this._hideAllSections();

    const section = this.sections[target];
    if (section) {
      section.classList.add('section--active');
    }

    // ✅ Disparar eventos según la sección (usar 'target', NO 'sectionName')
    if (target === 'mapa') {
      window.dispatchEvent(new Event('loadMapa'));
    } else if (target === 'list') {
      window.dispatchEvent(new Event('loadList'));
    }
  }

  _hideAllSections() {
    Object.values(this.sections).forEach(s => {
      if (s) s.classList.remove('section--active');
    });
  }

  _initNavButtons() {
    const B = this.buttons;

    if (B.navList) {
      B.navList.onclick = () => this.navigateTo('list');
    }

    if (B.navAvistador) {
      B.navAvistador.onclick = () => this.navigateTo('avistador');
    }

    if (B.navMapa) {
      B.navMapa.onclick = () => this.navigateTo('mapa');
    }
  }

  _initAuthButtons() {
    const B = this.buttons;

    if (B.login) {
      B.login.onclick = () => {
        this.navigateTo('login');
        const lr = document.getElementById('loginResult');
        if (lr) lr.textContent = '';
        const lm = document.getElementById('loginMessage');
        if (lm) lm.textContent = '';
      };
    }

    if (B.logout) {
      B.logout.onclick = async () => {
        await AuthService.logout();
        this._updateAuthUI();
        this.navigateTo('mapa'); // Volver al mapa después de cerrar sesión
      };
    }

    if (B.goRegister) {
      B.goRegister.onclick = () => this.navigateTo('avistador');
    }
  }

  // ✨ Botón flotante "Agregar desaparecido"
  _initFabAdd() {
    if (!this.btnAddDesaparecido) return;

    this.btnAddDesaparecido.onclick = () => {
      const user = AuthService.getCurrentUser();
      if (!user) {
        // ✅ SOLUCIÓN 3: Sin alert, ir directo al login con mensaje
        this.navigateTo('login');
        const lm = document.getElementById('loginMessage');
        if (lm) lm.textContent = 'Iniciá sesión para registrar personas desaparecidas.';
        return;
      }

      this.navigateTo('form');

      // Scroll suave hacia el formulario
      const formSection = document.getElementById('formSection');
      if (formSection) {
        formSection.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    };
  }

  // ===== UI según autenticación =====
  _updateAuthUI() {
    const user = AuthService.getCurrentUser();
    const isLogged = !!user;

    // Estado de sesión - mostrar nombre del usuario
    if (this.sessionStatus) {
      if (isLogged) {
        const nombreCompleto = [user.nombre, user.apellido].filter(Boolean).join(' ');
        const displayName = nombreCompleto || user.dni || user.email || 'Usuario';
        this.sessionStatus.textContent = `Sesión iniciada: ${displayName}`;
        this.sessionStatus.classList.remove('u-hidden');
      } else {
        this.sessionStatus.textContent = '';
        this.sessionStatus.classList.add('u-hidden');
      }
    }

    // Botones según estado de autenticación
    this._toggle(this.buttons.login, !isLogged);
    this._toggle(this.buttons.logout, isLogged);
    this._toggle(this.buttons.navAvistador, !isLogged);
  }

  _toggle(el, show) {
    if (!el) return;
    if (show) {
      el.classList.remove('u-hidden');
      el.style.display = '';
    } else {
      el.classList.add('u-hidden');
      el.style.display = 'none';
    }
  }
}
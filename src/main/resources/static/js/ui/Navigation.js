import { appState } from '../config/state.js';
import { AuthService } from '../services/AuthService.js';

/**
 * Gestor de navegación SPA
 */
export class Navigation {

  constructor() {
    this.sections = {
      login: document.getElementById("loginSection"),
      avistador: document.getElementById("avistadorSection"),
      form: document.getElementById("formSection"),
      list: document.getElementById("listSection"),
      mapa: document.getElementById("mapaSection")
    };

    this.buttons = {
      navForm: document.getElementById("btnNavForm"),
      navList: document.getElementById("btnNavList"),
      navAvistador: document.getElementById("btnNavAvistador"),
      navMapa: document.getElementById("btnNavMapa"),
      login: document.getElementById("btnLogin"),
      logout: document.getElementById("btnLogout"),
      goRegister: document.getElementById("btnGoRegister")
    };

    this.sessionStatus = document.getElementById("sessionStatus");
  }

  /**
   * Inicializa los event listeners de navegación
   */
  init() {
    this._initNavButtons();
    this._initAuthButtons();
    this._subscribeToAuth();
  }

  /**
   * Cambia a una sección específica
   * @param {string} targetSection - Nombre de la sección (login, form, list, mapa, avistador)
   */
  navigateTo(targetSection) {
    // ✨ AQUÍ VA hideAllSections() integrado
    this._hideAllSections();

    // Mostrar la sección objetivo
    const section = this.sections[targetSection];
    if (section) {
      section.classList.add('section--active');
    }
  }

  /**
   * Oculta todas las secciones (método privado)
   * @private
   */
  _hideAllSections() {
    Object.values(this.sections).forEach(section => {
      if (section) {
        section.classList.remove('section--active');
      }
    });
  }

  /**
   * Inicializa botones de navegación
   * @private
   */
  _initNavButtons() {
    if (this.buttons.navForm) {
      this.buttons.navForm.onclick = async () => {
        if (!AuthService.getCurrentUser()) {
          if (this.sections.login) {
            this.navigateTo('login');
            const lm = document.getElementById("loginMessage");
            if (lm) lm.textContent = "Para registrar un desaparecido debés iniciar sesión o registrarte.";
          } else {
            alert("Para registrar un desaparecido debés iniciar sesión.");
          }
          return;
        }
        this.navigateTo('form');
      };
    }

    if (this.buttons.navList) {
      this.buttons.navList.onclick = () => {
        this.navigateTo('list');
        // Disparar evento custom para cargar la lista
        window.dispatchEvent(new CustomEvent('loadList'));
      };
    }

    if (this.buttons.navAvistador) {
      this.buttons.navAvistador.onclick = () => {
        this.navigateTo('avistador');
      };
    }

    if (this.buttons.navMapa) {
      this.buttons.navMapa.onclick = () => {
        this.navigateTo('mapa');
        // Disparar evento custom para inicializar el mapa
        window.dispatchEvent(new CustomEvent('loadMapa'));
      };
    }
  }

  /**
   * Inicializa botones de autenticación
   * @private
   */
  _initAuthButtons() {
    if (this.buttons.login) {
      this.buttons.login.onclick = () => {
        this.navigateTo('login');
        const lr = document.getElementById("loginResult");
        if (lr) lr.textContent = "";
        const lm = document.getElementById("loginMessage");
        if (lm) lm.textContent = "";
      };
    }

    if (this.buttons.logout) {
      this.buttons.logout.onclick = async () => {
        await AuthService.logout();
        this.navigateTo('avistador');
      };
    }

    if (this.buttons.goRegister) {
      this.buttons.goRegister.onclick = () => {
        this.navigateTo('avistador');
      };
    }
  }

  /**
   * Suscribirse a cambios en autenticación
   * @private
   */
  _subscribeToAuth() {
    appState.subscribe((type, data) => {
      if (type === 'user') {
        this._updateUIForAuth(data);
      }
    });
  }

  /**
   * Actualiza la UI según el estado de autenticación
   * @private
   */
  _updateUIForAuth(user) {
    if (user) {
      const label = user.nombre || user.dni || "";
      if (this.sessionStatus) {
        this.sessionStatus.textContent = `Sesión iniciada: ${label} · ${user.email || ""}`;
      }
      if (this.buttons.logout) {
        this.buttons.logout.classList.remove('u-hidden');
        this.buttons.logout.style.display = "inline-block";
      }
      if (this.buttons.login) {
        this.buttons.login.style.display = "none";
      }
    } else {
      if (this.sessionStatus) this.sessionStatus.textContent = "No has iniciado sesión.";
      if (this.buttons.logout) {
        this.buttons.logout.classList.add('u-hidden');
        this.buttons.logout.style.display = "none";
      }
      if (this.buttons.login) {
        this.buttons.login.style.display = "inline-block";
      }
    }
  }
}
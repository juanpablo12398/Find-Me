import { appState } from './config/state.js';
import { AuthService } from './services/AuthService.js';
import { Navigation } from './ui/Navigation.js';
import { MapManager } from './ui/MapManager.js';
import { LoginForm } from './ui/forms/LoginForm.js';
import { DesaparecidoService } from './services/DesaparecidoService.js';
import { AvistadorService } from './services/AvistadorService.js';

let navigation;
let mapManager;

window.addEventListener('DOMContentLoaded', initApp);

async function initApp() {
  console.log('🚀 Inicializando aplicación...');

  // 1) Autenticación primero
  await AuthService.checkAuth();

  // 2) Crear navegación después del DOM
  navigation = new Navigation();
  navigation.init();

  // 3) Cargar mapa solo cuando el usuario entra a esa sección
  window.addEventListener('loadMapa', () => {
    if (!mapManager) {
      console.log('📍 Creando MapManager por primera vez...');
      mapManager = new MapManager();
      mapManager.init();
    } else {
      console.log('📍 Recargando avistamientos...');
      mapManager.loadAvistamientos();
    }

    // 🔧 Recalcula tamaño después de pintar
    setTimeout(() => {
      if (appState.map) appState.map.invalidateSize();
    }, 150);
  });

  // También si cambia el tamaño de la ventana
  window.addEventListener('resize', () => {
    if (appState.map) appState.map.invalidateSize();
  });

  // 4) Cargar lista al entrar a lista
  window.addEventListener('loadList', loadList);

  // 5) Formularios
  new LoginForm(navigation);
  initDesaparecidosForm();
  initAvistadoresForm();
  initAvistamientoForm();

  // 6) Botones auxiliares
  const btnReloadMapa = document.getElementById('btnReloadMapa');
  if (btnReloadMapa) {
    btnReloadMapa.addEventListener('click', async () => {
      if (!mapManager) {
        console.warn('⚠️ MapManager no inicializado');
        return;
      }
      console.log('🔄 Actualizando mapa...');
      await mapManager.refresh();
      setTimeout(() => {
        if (appState.map) appState.map.invalidateSize();
      }, 50);
    });
  }

  const btnCloseModal = document.getElementById('btnCloseModal');
  if (btnCloseModal) {
    btnCloseModal.onclick = () => mapManager && mapManager.closeModal();
  }

  const modal = document.getElementById('modalAvistamiento');
  if (modal) {
    modal.onclick = (e) => {
      if (e.target === modal) mapManager && mapManager.closeModal();
    };
  }

  const btnReload = document.getElementById('btnReload');
  if (btnReload) btnReload.onclick = loadList;

  console.log('✅ Aplicación inicializada');
}

// ============================================
// === FORMULARIO DE DESAPARECIDOS ===
// ============================================
function initDesaparecidosForm() {
  const form = document.getElementById('formDesaparecido');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const resultado = document.getElementById('resultado');
    const btnSubmit = document.getElementById('btnSubmit');

    // Verificar autenticación
    if (!AuthService.getCurrentUser()) {
      if (resultado) {
        resultado.textContent = '❌ Debés iniciar sesión primero.';
        resultado.style.color = 'red';
      }
      navigation.navigateTo('login');
      const lm = document.getElementById('loginMessage');
      if (lm) lm.textContent = 'Tu sesión no está activa. Iniciá sesión para registrar un desaparecido.';
      return;
    }

    const v = (id) => document.getElementById(id).value.trim();

    if (!form.checkValidity()) {
      if (resultado) {
        resultado.textContent = '❌ Revisá los campos en rojo.';
        resultado.style.color = 'red';
      }
      return;
    }

    const body = {
      nombre: v('nombre'),
      apellido: v('apellido'),
      edad: parseInt(v('edad'), 10),
      dni: v('dni'),
      descripcion: v('descripcion'),
      fotoUrl: v('fotoUrl') || 'https://via.placeholder.com/150',
    };

    if (resultado) {
      resultado.textContent = 'Enviando…';
      resultado.style.color = '#666';
    }
    if (btnSubmit) btnSubmit.disabled = true;

    try {
      const creado = await DesaparecidoService.crear(body);

      if (resultado) {
        resultado.textContent = `✅ Persona registrada con ID: ${creado.id}`;
        resultado.style.color = 'green';
      }

      form.reset();

      setTimeout(() => {
        navigation.navigateTo('list');
      }, 800);
    } catch (err) {
      if (resultado) {
        resultado.textContent = '❌ ' + err.message;
        resultado.style.color = 'red';
      }
    } finally {
      if (btnSubmit) btnSubmit.disabled = false;
    }
  });
}

// ============================================
// === FORMULARIO DE AVISTADORES ===
// ============================================
function initAvistadoresForm() {
  const form = document.getElementById('formAvistador');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const out = document.getElementById('outAvistador');
    const btn = document.getElementById('btnAvistador');
    const v = (id) => document.getElementById(id).value.trim();

    if (!form.checkValidity()) {
      if (out) {
        out.textContent = '❌ Revisá los campos.';
        out.style.color = 'red';
      }
      return;
    }

    const body = {
      dni: v('a_dni'),
      nombre: v('a_nombre'),
      apellido: v('a_apellido'),
      edad: parseInt(v('a_edad'), 10),
      direccion: v('a_direccion'),
      email: v('a_email') || null,
      telefono: v('a_telefono') || null,
    };

    if (out) {
      out.textContent = 'Enviando…';
      out.style.color = '#666';
    }
    if (btn) btn.disabled = true;

    try {
      const dto = await AvistadorService.crear(body);

      if (out) {
        out.textContent = `✅ Avistador registrado. ID: ${dto.id}`;
        out.style.color = 'green';
      }

      form.reset();

      // El backend setea cookie → traemos sesión
      await AuthService.checkAuth();

      setTimeout(() => {
        if (out) {
          out.textContent = '✅ Ya estás registrado y logueado. Ahora podés registrar desaparecidos.';
        }
        navigation.navigateTo('form');
      }, 600);
    } catch (err) {
      if (out) {
        out.textContent = '❌ ' + err.message;
        out.style.color = 'red';
      }
    } finally {
      if (btn) btn.disabled = false;
    }
  });
}

// ============================================
// === FORMULARIO DE AVISTAMIENTO (MODAL) ===
// ============================================
function initAvistamientoForm() {
  const form = document.getElementById('formAvistamiento');
  if (!form) return;

  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const v = (id) => document.getElementById(id).value.trim();

    // nombre mostrado del select (para el popup inmediato)
    const selectDesaparecido = document.getElementById('av_desaparecido');
    const desaparecidoNombre = selectDesaparecido.options[selectDesaparecido.selectedIndex]?.text || '';

    const formData = {
      desaparecidoId: v('av_desaparecido'),
      descripcion: v('av_descripcion'),
      fotoUrl: v('av_foto'),
      publico: document.getElementById('av_publico').checked,
      desaparecidoNombre,
    };

    if (!formData.desaparecidoId) {
      const result = document.getElementById('avistamientoResult');
      if (result) {
        result.textContent = '❌ Debes seleccionar una persona desaparecida';
        result.style.color = 'red';
      }
      return;
    }

    await mapManager.submitAvistamiento(formData);
  });
}

// ============================================
// === CARGAR LISTA DE DESAPARECIDOS ===
// ============================================
async function loadList() {
  const estado = document.getElementById('listaEstado');
  const tbody = document.getElementById('tablaDesaparecidosBody');

  if (estado) {
    estado.textContent = 'Cargando…';
  }

  if (tbody) {
    tbody.innerHTML = '';
  }

  try {
    const data = await DesaparecidoService.obtenerTodos();

    if (estado) {
      estado.textContent = '';
    }

    renderTable(data);

    if (!data || data.length === 0) {
      if (estado) {
        estado.textContent = 'No hay registros.';
      }
    }
  } catch (err) {
    console.error('Error cargando lista:', err);
    if (estado) {
      estado.textContent = 'Error al cargar la lista.';
    }
  }
}

// ============================================
// === RENDERIZAR TABLA ===
// ============================================
function renderTable(data) {
  const tbody = document.getElementById('tablaDesaparecidosBody');
  tbody.innerHTML = '';

  (data || []).forEach((d) => {
    const tr = document.createElement('tr');
    tr.className = 'table__row';
    tr.innerHTML = `
      <td class="table__cell">${d.nombre ?? ''}</td>
      <td class="table__cell">${d.apellido ?? ''}</td>
      <td class="table__cell">${d.dni ?? ''}</td>
      <td class="table__cell">${d.descripcion ?? ''}</td>
      <td class="table__cell"><img class="table__image" src="${d.foto ?? ''}" alt="foto"/></td>
      <td class="table__cell">${d.fechaFormateada ?? ''}</td>
    `;
    tbody.appendChild(tr);
  });
}

import { API_ENDPOINTS, ERROR_MAPS } from './config/constants.js';
import { appState } from './config/state.js';
import { AuthService } from './services/AuthService.js';
import { DesaparecidoService } from './services/DesaparecidoService.js';
import { AvistadorService } from './services/AvistadorService.js';
import { Navigation } from './ui/Navigation.js';
import { MapManager } from './ui/MapManager.js';

// === Instancias globales ===
const navigation = new Navigation();
const mapManager = new MapManager();

/**
 * Inicializa la aplicación
 */
async function initApp() {
  console.log('🚀 Inicializando aplicación...');

  // 1. Verificar autenticación
  await AuthService.checkAuth();

  // 2. Inicializar navegación
  navigation.init();

  // 3. Inicializar formularios
  initLoginForm();
  initDesaparecidosForm();
  initAvistadoresForm();

  // 4. Event listeners custom
  window.addEventListener('loadMapa', () => {
    if (!appState.map) {
      mapManager.init();
    }
    mapManager.loadAvistamientos();
  });

  window.addEventListener('loadList', () => {
    loadList();
  });

  // 5. Botones del mapa
  const btnReloadMapa = document.getElementById("btnReloadMapa");
  if (btnReloadMapa) {
    btnReloadMapa.onclick = () => mapManager.loadAvistamientos();
  }

  const btnReportarAvistamiento = document.getElementById("btnReportarAvistamiento");
  if (btnReportarAvistamiento) {
    btnReportarAvistamiento.onclick = () => {
      if (!AuthService.getCurrentUser()) {
        alert("Debés iniciar sesión para reportar un avistamiento.");
        navigation.navigateTo('login');
        return;
      }
      alert("Funcionalidad próximamente...");
    };
  }

  // 6. Botón recargar lista
  const btnReload = document.getElementById("btnReload");
  if (btnReload) {
    btnReload.onclick = loadList;
  }

  console.log('✅ Aplicación inicializada');
}

// ============================================
// === FORMULARIO DE LOGIN ===
// ============================================
function initLoginForm() {
  const loginForm = document.getElementById("formLogin");
  if (!loginForm) return;

  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const v = id => document.getElementById(id).value.trim();
    const loginResult = document.getElementById("loginResult");
    const btnLoginSubmit = document.getElementById("btnLoginSubmit");

    if (loginResult) {
      loginResult.textContent = "Iniciando sesión…";
      loginResult.style.color = "#666";
    }
    if (btnLoginSubmit) btnLoginSubmit.disabled = true;

    try {
      await AuthService.login(v("login_dni"), v("login_email"));

      if (loginResult) {
        loginResult.textContent = "✅ Sesión iniciada";
        loginResult.style.color = "green";
      }

      setTimeout(() => {
        navigation.navigateTo('form');
        loginForm.reset();
      }, 400);

    } catch (err) {
      if (loginResult) {
        loginResult.textContent = "❌ " + err.message;
        loginResult.style.color = "red";
      }
    } finally {
      if (btnLoginSubmit) btnLoginSubmit.disabled = false;
    }
  });
}

// ============================================
// === FORMULARIO DE DESAPARECIDOS ===
// ============================================
function initDesaparecidosForm() {
  const form = document.getElementById("formDesaparecido");
  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const resultado = document.getElementById("resultado");
    const btnSubmit = document.getElementById("btnSubmit");

    if (!AuthService.getCurrentUser()) {
      if (resultado) {
        resultado.textContent = "❌ Debés iniciar sesión primero.";
        resultado.style.color = "red";
      }
      return;
    }

    const v = id => document.getElementById(id).value.trim();

    if (!form.checkValidity()) {
      if (resultado) {
        resultado.textContent = "❌ Revisá los campos en rojo.";
        resultado.style.color = "red";
      }
      return;
    }

    const body = {
      nombre: v("nombre"),
      apellido: v("apellido"),
      edad: parseInt(v("edad"), 10),
      dni: v("dni"),
      descripcion: v("descripcion"),
      fotoUrl: v("fotoUrl") || "https://via.placeholder.com/150"
    };

    if (resultado) {
      resultado.textContent = "Enviando…";
      resultado.style.color = "#666";
    }
    if (btnSubmit) btnSubmit.disabled = true;

    try {
      const creado = await DesaparecidoService.crear(body);

      if (resultado) {
        resultado.textContent = `✅ Persona registrada con ID: ${creado.id}`;
        resultado.style.color = "green";
      }

      form.reset();

      // Cambiar a la vista de lista
      setTimeout(() => {
        navigation.navigateTo('list');
        loadList();
      }, 800);

    } catch (err) {
      if (resultado) {
        resultado.textContent = "❌ " + err.message;
        resultado.style.color = "red";
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
  const form = document.getElementById("formAvistador");
  if (!form) return;

  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const out = document.getElementById("outAvistador");
    const btn = document.getElementById("btnAvistador");
    const v = id => document.getElementById(id).value.trim();

    if (!form.checkValidity()) {
      if (out) {
        out.textContent = "❌ Revisá los campos.";
        out.style.color = "red";
      }
      return;
    }

    const body = {
      dni: v("a_dni"),
      nombre: v("a_nombre"),
      apellido: v("a_apellido"),
      edad: parseInt(v("a_edad"), 10),
      direccion: v("a_direccion"),
      email: v("a_email") || null,
      telefono: v("a_telefono") || null
    };

    if (out) {
      out.textContent = "Enviando…";
      out.style.color = "#666";
    }
    if (btn) btn.disabled = true;

    try {
      const dto = await AvistadorService.crear(body);

      if (out) {
        out.textContent = `✅ Avistador registrado. ID: ${dto.id}`;
        out.style.color = "green";
      }

      form.reset();

      // El backend setea cookie, traemos sesión
      await AuthService.checkAuth();

      setTimeout(() => {
        if (out) {
          out.textContent = "✅ Ya estás registrado y logueado. Ahora podés registrar desaparecidos.";
        }
        navigation.navigateTo('form');
      }, 600);

    } catch (err) {
      if (out) {
        out.textContent = "❌ " + err.message;
        out.style.color = "red";
      }
    } finally {
      if (btn) btn.disabled = false;
    }
  });
}

// ============================================
// === CARGAR LISTA DE DESAPARECIDOS ===
// ============================================
async function loadList() {
  const estado = document.getElementById("listaEstado");
  const tbody = document.getElementById("tablaDesaparecidosBody");

  if (estado) {
    estado.textContent = "Cargando…";
  }

  if (tbody) {
    tbody.innerHTML = "";
  }

  try {
    const data = await DesaparecidoService.obtenerTodos();

    if (estado) {
      estado.textContent = "";
    }

    renderTable(data);

    if (!data || data.length === 0) {
      if (estado) {
        estado.textContent = "No hay registros.";
      }
    }
  } catch (err) {
    console.error('Error cargando lista:', err);
    if (estado) {
      estado.textContent = "Error al cargar la lista.";
    }
  }
}

// ============================================
// === RENDERIZAR TABLA ===
// ============================================
function renderTable(data) {
  const tbody = document.getElementById("tablaDesaparecidosBody");
  tbody.innerHTML = "";

  (data || []).forEach(d => {
    const tr = document.createElement("tr");
    tr.className = "table__row";
    tr.innerHTML = `
      <td class="table__cell">${d.nombre ?? ""}</td>
      <td class="table__cell">${d.apellido ?? ""}</td>
      <td class="table__cell">${d.dni ?? ""}</td>
      <td class="table__cell">${d.descripcion ?? ""}</td>
      <td class="table__cell"><img class="table__image" src="${d.foto ?? ""}" alt="foto"/></td>
      <td class="table__cell">${d.fechaFormateada ?? ""}</td>
    `;
    tbody.appendChild(tr);
  });
}

// ============================================
// === BOOTSTRAP ===
// ============================================
document.addEventListener("DOMContentLoaded", initApp);
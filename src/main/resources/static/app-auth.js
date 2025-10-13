// === Endpoints ===
const baseUrl = "/api/desaparecidos";
const avistadoresUrl = "/api/avistadores";
const authUrl = "/api/auth";

// === Estado ===
let currentUser = null;

// === Wrapper: fetch con cookies siempre incluidas ===
function fetchWithAuth(url, options = {}) {
  const isFormData = options.body instanceof FormData;
  const baseHeaders = isFormData ? {} : { "Content-Type": "application/json" };

  return fetch(url, {
    credentials: "include",
    ...options,
    headers: {
      ...baseHeaders,
      ...(options.headers || {}),
    },
  });
}

// === Mapeo de errores ===
const AVISTADOR_ERRORS = {
  400: { default: "Datos inválidos (revisá el formulario)." },
  404: { default: "No existe en padrón (RENAPER)." },
  409: { default: "DNI ya registrado." },
  422: {
    "avistador.renaper.nomatch": "Los datos no coinciden con el padrón.",
    "avistador.edad.menor": "Debés ser mayor de edad para registrarte.",
    default: "Datos inválidos."
  }
};

const DESAPARECIDO_ERRORS = {
  401: { default: "Debés iniciar sesión para registrar un desaparecido." },
  409: { default: "Ya existe un desaparecido con ese DNI." },
  422: {
    "desaparecido.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
    default: "Los datos enviados no son válidos."
  }
};

const AUTH_ERRORS = {
  404: {
    "auth.renaper.notfound": "No existe en padrón (RENAPER).",
    "auth.user.notfound": "Usuario no registrado. Primero debés registrarte.",
    default: "Usuario no encontrado."
  },
  422: {
    "auth.email.mismatch": "El email no coincide con el registrado.",
    default: "Datos inválidos."
  },
  401: { default: "Credenciales inválidas." }
};

// === Utils ===
async function parseProblem(resp) {
  const text = await resp.text();
  try {
    const pd = JSON.parse(text);
    const key = pd?.key ?? pd?.properties?.key ?? null;
    const detail = pd?.detail ?? text ?? `HTTP ${resp.status}`;
    return { status: resp.status, key, detail };
  } catch {
    return { status: resp.status, key: null, detail: text || `HTTP ${resp.status}` };
  }
}

function getErrorMessage(errorMap, status, key, detail) {
  const byStatus = errorMap[status];
  if (!byStatus) return detail || `Error HTTP ${status}`;
  if ((status === 422 || status === 404) && key && byStatus[key]) return byStatus[key];
  return byStatus.default || detail || `Error HTTP ${status}`;
}

// === DOM refs ===
const formSection = document.getElementById("formSection");
const listSection = document.getElementById("listSection");
const avistadorSection = document.getElementById("avistadorSection");
const loginSection = document.getElementById("loginSection"); // puede no existir si no la agregaste
const resultado = document.getElementById("resultado");
const btnSubmit = document.getElementById("btnSubmit");

const btnNavForm = document.getElementById("btnNavForm");
const btnNavList = document.getElementById("btnNavList");
const btnNavAvistador = document.getElementById("btnNavAvistador");

const btnLoginOpen = document.getElementById("btnLogin");
const btnLogout = document.getElementById("btnLogout");
const sessionStatus = document.getElementById("sessionStatus");

// === Auth ===
async function checkAuth() {
  try {
    const resp = await fetchWithAuth(`${authUrl}/me`);
    if (resp.ok) {
      currentUser = await resp.json();
      updateUIForAuth();
      return true;
    }
  } catch {}
  currentUser = null;
  updateUIForAuth();
  return false;
}

function updateUIForAuth() {
  if (currentUser) {
    if (sessionStatus) {
      const label = currentUser.nombre || currentUser.dni || "";
      sessionStatus.textContent = `Sesión iniciada: ${label} · ${currentUser.email || ""}`;
    }
    if (btnLogout) btnLogout.style.display = "inline-block";
    if (btnLoginOpen) btnLoginOpen.style.display = "none";
  } else {
    if (sessionStatus) sessionStatus.textContent = "No has iniciado sesión.";
    if (btnLogout) btnLogout.style.display = "none";
    if (btnLoginOpen) btnLoginOpen.style.display = "inline-block";
  }
}

async function logout() {
  try {
    await fetchWithAuth(`${authUrl}/logout`, { method: "POST" });
  } catch {}
  currentUser = null;
  updateUIForAuth();

  if (loginSection) loginSection.classList.remove("active");
  avistadorSection.classList.add("active");
  formSection.classList.remove("active");
  listSection.classList.remove("active");
}

// === Navegación SPA ===
if (btnNavForm) {
  btnNavForm.onclick = async () => {
    if (!currentUser) {
      if (loginSection) {
        loginSection.classList.add("active");
        formSection.classList.remove("active");
        listSection.classList.remove("active");
        avistadorSection.classList.remove("active");
        const lm = document.getElementById("loginMessage");
        if (lm) lm.textContent = "Para registrar un desaparecido debés iniciar sesión o registrarte.";
      } else {
        alert("Para registrar un desaparecido debés iniciar sesión.");
      }
      return;
    }
    formSection.classList.add("active");
    listSection.classList.remove("active");
    avistadorSection.classList.remove("active");
    if (loginSection) loginSection.classList.remove("active");
  };
}

if (btnNavList) {
  btnNavList.onclick = () => {
    listSection.classList.add("active");
    formSection.classList.remove("active");
    avistadorSection.classList.remove("active");
    if (loginSection) loginSection.classList.remove("active");
    loadList();
  };
}

if (btnNavAvistador) {
  btnNavAvistador.onclick = () => {
    avistadorSection.classList.add("active");
    formSection.classList.remove("active");
    listSection.classList.remove("active");
    if (loginSection) loginSection.classList.remove("active");
  };
}

// Abrir sección de login (si existe)
if (btnLoginOpen) {
  btnLoginOpen.addEventListener("click", () => {
    if (!loginSection) return;
    loginSection.classList.add("active");
    formSection.classList.remove("active");
    listSection.classList.remove("active");
    avistadorSection.classList.remove("active");
    const lr = document.getElementById("loginResult");
    if (lr) lr.textContent = "";
    const lm = document.getElementById("loginMessage");
    if (lm) lm.textContent = "";
  });
}

const btnGoRegister = document.getElementById("btnGoRegister");
if (btnGoRegister) {
  btnGoRegister.addEventListener("click", () => {
    avistadorSection.classList.add("active");
    if (loginSection) loginSection.classList.remove("active");
    formSection.classList.remove("active");
    listSection.classList.remove("active");
  });
}

if (btnLogout) btnLogout.onclick = logout;

// === Login form (opcional) ===
const loginForm = document.getElementById("formLogin");
if (loginForm) {
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const v = id => document.getElementById(id).value.trim();
    const loginResult = document.getElementById("loginResult");
    const btnLoginSubmit = document.getElementById("btnLoginSubmit");

    const body = { dni: v("login_dni"), email: v("login_email") };

    if (loginResult) {
      loginResult.textContent = "Iniciando sesión…";
      loginResult.style.color = "#666";
    }
    if (btnLoginSubmit) btnLoginSubmit.disabled = true;

    try {
      const resp = await fetchWithAuth(`${authUrl}/login`, {
        method: "POST",
        body: JSON.stringify(body)
      });

      if (!resp.ok) {
        const problem = await parseProblem(resp);
        const msg = getErrorMessage(AUTH_ERRORS, problem.status, problem.key, problem.detail);
        throw new Error(msg);
      }

      currentUser = await resp.json();
      updateUIForAuth();
      if (loginResult) {
        loginResult.textContent = "✅ Sesión iniciada";
        loginResult.style.color = "green";
      }

      setTimeout(() => {
        formSection.classList.add("active");
        if (loginSection) {
          loginSection.classList.remove("active");
          loginForm.reset();
        }
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

// === Desaparecidos: POST ===
document.getElementById("formDesaparecido").addEventListener("submit", async (e) => {
  e.preventDefault();

  if (!currentUser) {
    resultado.textContent = "❌ Debés iniciar sesión primero.";
    resultado.style.color = "red";
    return;
  }

  const v = id => document.getElementById(id).value.trim();
  const form = e.currentTarget;

  if (!form.checkValidity()) {
    resultado.textContent = "❌ Revisá los campos en rojo.";
    resultado.style.color = "red";
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

  resultado.textContent = "Enviando…";
  resultado.style.color = "#666";
  btnSubmit.disabled = true;

  try {
    const resp = await fetchWithAuth(baseUrl, {
      method: "POST",
      body: JSON.stringify(body)
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(DESAPARECIDO_ERRORS, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    const creado = await resp.json();
    resultado.textContent = `✅ Persona registrada con ID: ${creado.id}`;
    resultado.style.color = "green";
    form.reset();

    listSection.classList.add("active");
    formSection.classList.remove("active");
    await loadList();

  } catch (err) {
    resultado.textContent = "❌ " + err.message;
    resultado.style.color = "red";
  } finally {
    btnSubmit.disabled = false;
  }
});

// === Desaparecidos: GET lista ===
document.getElementById("btnReload").onclick = loadList;

async function loadList() {
  const estado = document.getElementById("listaEstado");
  const tbody = document.getElementById("tablaDesaparecidosBody");
  estado.textContent = "Cargando…";
  tbody.innerHTML = "";

  try {
    const resp = await fetchWithAuth(baseUrl);
    if (!resp.ok) throw new Error("No se pudo cargar la lista");

    const data = await resp.json();
    estado.textContent = "";
    renderTable(data);

    if (!data || data.length === 0) {
      estado.textContent = "No hay registros.";
    }
  } catch {
    estado.textContent = "Error al cargar la lista.";
  }
}

function renderTable(data) {
  const tbody = document.getElementById("tablaDesaparecidosBody");
  tbody.innerHTML = "";

  (data || []).forEach(d => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${d.nombre ?? ""}</td>
      <td>${d.apellido ?? ""}</td>
      <td>${d.dni ?? ""}</td>
      <td>${d.descripcion ?? ""}</td>
      <td><img src="${d.foto ?? ""}" alt="foto"/></td>
      <td>${d.fechaFormateada ?? ""}</td>
    `;
    tbody.appendChild(tr);
  });
}

// === Avistadores: POST (registro + sesión) ===
document.getElementById("formAvistador").addEventListener("submit", async (e) => {
  e.preventDefault();
  const v = id => document.getElementById(id).value.trim();
  const out = document.getElementById("outAvistador");
  const btn = document.getElementById("btnAvistador");
  const form = e.currentTarget;

  if (!form.checkValidity()) {
    out.textContent = "❌ Revisá los campos.";
    out.style.color = "red";
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

  out.textContent = "Enviando…";
  out.style.color = "#666";
  btn.disabled = true;

  try {
    const resp = await fetchWithAuth(avistadoresUrl, {
      method: "POST",
      body: JSON.stringify(body)
    });

    if (!resp.ok) {
      const problem = await parseProblem(resp);
      const msg = getErrorMessage(AVISTADOR_ERRORS, problem.status, problem.key, problem.detail);
      throw new Error(msg);
    }

    const dto = await resp.json();
    out.textContent = `✅ Avistador registrado. ID: ${dto.id}`;
    out.style.color = "green";
    form.reset();

    // backend setea cookie → traemos sesión
    await checkAuth();

    setTimeout(() => {
      out.textContent = "✅ Ya estás registrado y logueado. Ahora podés registrar desaparecidos.";
      formSection.classList.add("active");
      avistadorSection.classList.remove("active");
      if (loginSection) loginSection.classList.remove("active");
    }, 600);

  } catch (err) {
    out.textContent = "❌ " + err.message;
    out.style.color = "red";
  } finally {
    btn.disabled = false;
  }
});

// === Boot ===
document.addEventListener("DOMContentLoaded", checkAuth);

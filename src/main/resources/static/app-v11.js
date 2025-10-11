// === Config ===
const baseUrl = "/api/desaparecidos";
const avistadoresUrl = "/api/avistadores";

// === Errores conocidos ===
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
  409: { default: "Ya existe un desaparecido con ese DNI." },
  422: {
    "desaparecido.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
    default: "Los datos enviados no son válidos."
  }
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

  if (status === 422) {
    if (key && byStatus[key]) return byStatus[key];
    // 422: si no hay key específica, usamos default y si no, detail
    return byStatus.default || detail || `Error HTTP ${status}`;
  }

  // 400/404/409: preferir SIEMPRE el default del mapa (mensaje amigable)
  return byStatus.default || detail || `Error HTTP ${status}`;
}

// === SPA NAV ===
const formSection = document.getElementById("formSection");
const listSection = document.getElementById("listSection");
const avistadorSection = document.getElementById("avistadorSection");
const resultado = document.getElementById("resultado");
const btnSubmit = document.getElementById("btnSubmit");

document.getElementById("btnNavForm").onclick = () => {
  formSection.classList.add("active");
  listSection.classList.remove("active");
  avistadorSection.classList.remove("active");
};

document.getElementById("btnNavList").onclick = () => {
  listSection.classList.add("active");
  formSection.classList.remove("active");
  avistadorSection.classList.remove("active");
  loadList();
};

document.getElementById("btnNavAvistador").onclick = () => {
  avistadorSection.classList.add("active");
  formSection.classList.remove("active");
  listSection.classList.remove("active");
};

// === Desaparecidos: POST ===
document.getElementById("formDesaparecido").addEventListener("submit", async (e) => {
  e.preventDefault();
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
    const resp = await fetch(baseUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
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
    const resp = await fetch(baseUrl);
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

// === Avistadores: POST ===
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
    const resp = await fetch(avistadoresUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
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

  } catch (err) {
    out.textContent = "❌ " + err.message;
    out.style.color = "red";
  } finally {
    btn.disabled = false;
  }
});

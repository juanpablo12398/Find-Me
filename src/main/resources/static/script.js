const baseUrl = "/api/desaparecidos";

// Elementos
const formSection = document.getElementById("formSection");
const listSection = document.getElementById("listSection");
const resultado = document.getElementById("resultado");
const btnSubmit = document.getElementById("btnSubmit");

// Navegación SPA
document.getElementById("btnNavForm").onclick = () => {
  formSection.classList.add("active");
  listSection.classList.remove("active");
};

document.getElementById("btnNavList").onclick = () => {
  listSection.classList.add("active");
  formSection.classList.remove("active");
  loadList();
};

// Envío del formulario (POST)
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
      const errorText = await resp.text();
      throw new Error(errorText || `HTTP ${resp.status}`);
    }
    const creado = await resp.json();
    resultado.textContent = `✅ Persona registrada con ID: ${creado.id}`;
    resultado.style.color = "green";
    form.reset();

    // Ir a la lista y refrescar
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

// Cargar lista (GET)
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
  } catch (err) {
    console.error(err);
    estado.textContent = "Error al cargar la lista.";
  }
}

// Renderizar tabla (usa FrontDTO: foto y fechaFormateada)
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

// ---- Avistadores ----
const avistadoresUrl = "/api/avistadores";
const avistadorSection = document.getElementById("avistadorSection");
const btnNavAvistador = document.getElementById("btnNavAvistador");

// Navegación a sección Avistador
document.getElementById("btnNavAvistador").onclick = () => {
  avistadorSection.classList.add("active");
  formSection.classList.remove("active");
  listSection.classList.remove("active");
};

// Submit Avistador
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
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(body)
    });

    const text = await resp.text();
    if (!resp.ok) {
      // Mensajes claros según tu handler del back
      if (resp.status === 404) throw new Error("No existe en padrón (RENAPER).");
      if (resp.status === 409) throw new Error("DNI ya registrado.");
      if (resp.status === 422) throw new Error("Los datos no coinciden con el padrón.");
      throw new Error(text || `HTTP ${resp.status}`);
    }

    const dto = JSON.parse(text);
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


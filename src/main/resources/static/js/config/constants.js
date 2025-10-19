// === Endpoints de la API ===
export const API_ENDPOINTS = {
  DESAPARECIDOS: "/api/desaparecidos",
  AVISTADORES: "/api/avistadores",
  AUTH: "/api/auth",
  AVISTAMIENTOS: "/api/avistamientos"
};

// === Mapeo de errores ===
export const ERROR_MAPS = {
  AVISTADOR: {
    400: { default: "Datos inválidos (revisá el formulario)." },
    404: { default: "No existe en padrón (RENAPER)." },
    409: { default: "DNI ya registrado." },
    422: {
      "avistador.renaper.nomatch": "Los datos no coinciden con el padrón.",
      "avistador.edad.menor": "Debés ser mayor de edad para registrarte.",
      default: "Datos inválidos."
    }
  },

  DESAPARECIDO: {
    401: { default: "Debés iniciar sesión para registrar un desaparecido." },
    409: { default: "Ya existe un desaparecido con ese DNI." },
    422: {
      "desaparecido.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
      default: "Los datos enviados no son válidos."
    }
  },

  AUTH: {
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
  },

  AVISTAMIENTO: {
    404: {
      "avistamiento.avistador.notfound": "El avistador no existe.",
      "avistamiento.desaparecido.notfound": "El desaparecido no existe.",
      default: "Recurso no encontrado."
    },
    422: {
      "avistamiento.coords.invalidas": "Las coordenadas son inválidas.",
      "avistamiento.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
      default: "Datos inválidos."
    },
    401: { default: "Debés iniciar sesión para reportar un avistamiento." }
  }
};
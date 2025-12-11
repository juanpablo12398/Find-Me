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
    404: {
      "desaparecido.renaper.notfound": "No se encontró el DNI en el padrón de RENAPER.",  // ✅ NUEVO
      default: "Recurso no encontrado."
    },
    422: {
      "desaparecido.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
      default: "Los datos enviados no son válidos."
    }
  },

  AUTH: {
    404: {
      "auth.login.renaper.notfound": "No existe en padrón (RENAPER).",
      "auth.login.user.notfound": "Usuario no registrado. Primero debés registrarte.",
      default: "Usuario no encontrado.",
    },
    422: {
      "auth.login.email.mismatch": "El email no coincide con el registrado.",
      default: "Datos inválidos.",
    },
    401: {
      "auth.token.notfound": "No encontramos tu sesión. Iniciá sesión de nuevo.",
      "auth.token.expired": "Tu sesión expiró. Volvé a iniciar sesión.",
      "auth.token.invalid": "Token inválido. Probá iniciar sesión otra vez.",
      "auth.token.malformed": "Token mal formado.",
      "auth.token.data.missing": "Faltan datos en el token.",
      default: "Credenciales inválidas.",
    },
  },

  AVISTAMIENTO: {
    404: {
      "avistamiento.avistador.notfound": "El avistador no existe.",
      "avistamiento.desaparecido.notfound": "El desaparecido no existe.",
      default: "Recurso no encontrado."
    },
    422: {
      "avistamiento.coords.invalidas": "Las coordenadas son inválidas.",
      "avistamiento.coords.lat.required": "La latitud es obligatoria.",
      "avistamiento.coords.lat.range": "La latitud está fuera de rango.",
      "avistamiento.coords.lat.naninf": "La latitud no es un número válido.",
      "avistamiento.coords.lng.required": "La longitud es obligatoria.",
      "avistamiento.coords.lng.range": "La longitud está fuera de rango.",
      "avistamiento.coords.lng.naninf": "La longitud no es un número válido.",
      "avistamiento.descripcion.corta": "La descripción debe tener al menos 20 caracteres.",
      default: "Datos inválidos."
    },
    401: { default: "Debés iniciar sesión para reportar un avistamiento." }
  }
};
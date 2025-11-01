export const FILTER_TYPES = {
  TODOS: 'todos',
  RADIO: 'radio',
  AREA: 'area',
  POLIGONO: 'poligono',
  DESAPARECIDO: 'desaparecido'
};

/**
 * Configuración de cada filtro
 */
export const FILTER_CONFIG = {
  [FILTER_TYPES.TODOS]: {
    icon: '🗺️',
    title: 'Todos los avistamientos',
    description: 'Ver todos en el mapa',
    requiresInteraction: false
  },
  [FILTER_TYPES.RADIO]: {
    icon: '📍',
    title: 'Buscar por radio',
    description: 'Desde un punto específico',
    requiresInteraction: true,
    hasControls: true
  },
  [FILTER_TYPES.AREA]: {
    icon: '▢',
    title: 'Buscar por área',
    description: 'Dibuja un rectángulo',
    requiresInteraction: true
  },
  [FILTER_TYPES.POLIGONO]: {
    icon: '⬡',
    title: 'Buscar por polígono',
    description: 'Área personalizada',
    requiresInteraction: true
  },
  [FILTER_TYPES.DESAPARECIDO]: {
    icon: '👤',
    title: 'Por persona',
    description: 'Filtrar por desaparecido',
    requiresInteraction: false,
    hasSelect: true
  }
};

/**
 * Mensajes del mapa
 */
export const MAP_MESSAGES = {
  INITIALIZED: { text: "✅ Mapa inicializado", color: "green" },
  LOADING: { text: "⏳ Cargando avistamientos...", color: "#666" },
  NO_RESULTS: { text: "⚠️ No hay avistamientos en esta área", color: "#999" },
  SELECT_CENTER: { text: "🎯 Haz clic en el mapa para seleccionar centro", color: "#0066cc" },
  CENTER_SELECTED: { text: "✅ Centro seleccionado. Ajusta el radio con el slider", color: "green" },
  DRAW_AREA: { text: "📐 Arrastra en el mapa para dibujar un área rectangular", color: "#0066cc" },
  DRAW_POLYGON: { text: "🔷 Haz clic en el mapa para dibujar un polígono (doble clic para terminar)", color: "#0066cc" },
  POLYGON_COMPLETE: { text: "✅ Polígono completado", color: "green" },
  REPORT_MODE: { text: "🎯 Click en el mapa para marcar ubicación", color: "#0066cc" },
  ADDED: { text: "✅ Avistamiento agregado al mapa", color: "green" }
};

/**
 * Colores de visualización
 */
export const MAP_COLORS = {
  RADIO_CIRCLE: '#4CAF50',
  AREA_RECTANGLE: '#4CAF50',
  POLYGON: '#9c27b0'
};

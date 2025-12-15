import { appState } from '../config/state.js';
import { AuthService } from '../services/AuthService.js';
import { DesaparecidoService } from '../services/DesaparecidoService.js';
import { AvistamientoService } from '../services/AvistamientoService.js';
import { getMarkerIconUrl, MARKER_ICON_CONFIG } from '../utils/colors.js';
import { createPopupContent, createDesaparecidoOption } from '../utils/templates.js';
import { showError, showSuccess, showLoading } from '../utils/validators.js';
import { setButtonState, createOption } from '../utils/dom.js';
import { MapFilters } from './MapFilters.js';

/**
 * Gestor del mapa Leaflet
 * Responsabilidades: inicialización del mapa, modo reportar, agregar markers
 */
export class MapManager {

  constructor() {
    this.mapaEstado = document.getElementById("mapaEstado");
    this.isReportMode = false;
    this.tempMarker = null;
    this.selectedLatLng = null;

    // Delegamos los filtros a MapFilters
    this.filters = null;

    // Control de listeners
    this.listenersActive = false;

    this._onMapClick = this._handleMapClick.bind(this);
    this._onMapDblClick = this._handleMapDblClick.bind(this);

    // ⚠️ Bloqueo de menú y derivación a filtros (p/terminar polígono)
    this._onContextMenu = (ev) => {
      ev.preventDefault();
      if (this.filters) this.filters.handleContextMenu(ev);
    };

    this._preloadMarkerIcons();
  }

  /**
   * Precarga las imágenes de los íconos
   */
  _preloadMarkerIcons() {
    const colors = ['red', 'blue', 'green', 'violet', 'orange', 'yellow', 'grey', 'black', 'gold'];
    const baseUrl = 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img';

    colors.forEach(color => {
      const img = new Image();
      img.src = `${baseUrl}/marker-icon-2x-${color}.png`;
    });

    const shadow = new Image();
    shadow.src = 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png';

    console.log('🎨 Precargando íconos de markers...');
  }

  /**
   * Inicializa el mapa y sus componentes
   */
  init() {
    const map = L.map('map').setView([-34.6037, -58.3816], 12);

    setTimeout(() => {
      if (appState.map) appState.map.invalidateSize();
    }, 150);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(map);

    appState.map = map;
    appState.markersLayer = L.layerGroup().addTo(map);

    // 🔒 Bloquear click derecho SIEMPRE sobre el contenedor del mapa
    appState.map.getContainer().addEventListener('contextmenu', this._onContextMenu, { passive: false });

    this._updateEstado("✅ Mapa inicializado", "green");

    // Configurar modos
    this._setupReportMode();

    // Inicializar sistema de filtros
    this.filters = new MapFilters(this);
    this.filters.init();

    this.loadAvistamientos();
  }

  /**
   * Configura el modo reportar avistamiento
   */
  _setupReportMode() {
    const btnToggle = document.getElementById("btnToggleReportar");
    const mapElement = document.getElementById("map");

    if (!btnToggle) return;

    btnToggle.onclick = () => {
      if (!AuthService.getCurrentUser()) {
        alert("Debés iniciar sesión para reportar un avistamiento.");
        return;
      }

      this.isReportMode = !this.isReportMode;

      if (this.isReportMode) {
        btnToggle.textContent = "❌ Cancelar Reporte";
        btnToggle.classList.remove('button--primary');
        btnToggle.classList.add('button--danger');
        mapElement.classList.add('map--reporting');
        this._updateEstado("🎯 Click en el mapa para marcar ubicación", "#0066cc");
        this._enableMapClick();
      } else {
        btnToggle.textContent = "📍 Activar Modo Reportar";
        btnToggle.classList.remove('button--danger');
        btnToggle.classList.add('button--primary');
        mapElement.classList.remove('map--reporting');
        this._updateEstado("", "");
        this._checkIfShouldDisableListeners();
        this._removeTempMarker();
      }
    };
  }

  /**
   * Habilita los listeners de clic en el mapa
   * SOLO si no están ya activos
   */
  _enableMapClick() {
    if (this.listenersActive) return;
    if (appState.map) {
      appState.map.on('click', this._onMapClick);
      appState.map.on('dblclick', this._onMapDblClick);
      this.listenersActive = true;
    }
  }

  /**
   * Deshabilita los listeners de clic en el mapa
   * SOLO si no hay ningún modo activo
   */
  _disableMapClick() {
    if (!this.listenersActive) return;
    if (appState.map) {
      appState.map.off('click', this._onMapClick);
      appState.map.off('dblclick', this._onMapDblClick);
      this.listenersActive = false;
    }
  }

  /**
   * Verifica si debe desactivar los listeners
   * Solo los desactiva si NO hay ningún modo interactivo activo
   */
  _checkIfShouldDisableListeners() {
    const filtersNeedListeners = this.filters && this.filters.needsMapListeners();
    const reportModeActive = this.isReportMode;

    if (!filtersNeedListeners && !reportModeActive) {
      this._disableMapClick();
    }
  }

  /**
   * Maneja el clic en el mapa
   */
  _handleMapClick(e) {
    const { lat, lng } = e.latlng;

    // Primero, dar prioridad a los filtros
    if (this.filters) {
      const handledByFilter = this.filters.handleMapClick(lat, lng);
      if (handledByFilter) return;
    }

    // Luego, modo reportar avistamiento
    if (this.isReportMode) {
      this.selectedLatLng = { lat, lng };
      this._removeTempMarker();

      const tempIcon = L.icon({
        iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
        ...MARKER_ICON_CONFIG
      });

      this.tempMarker = L.marker([lat, lng], { icon: tempIcon })
        .addTo(appState.map)
        .bindPopup("📍 Ubicación del avistamiento")
        .openPopup();

      this._openModal(lat, lng);
    }
  }

  /**
   * Maneja el doble clic en el mapa (evita zoom por seguridad)
   */
  _handleMapDblClick(e) {
    if (e?.originalEvent) e.originalEvent.preventDefault();
    if (this.filters) this.filters.handleMapDblClick();
  }

  /**
   * Remueve el marker temporal
   */
  _removeTempMarker() {
    if (this.tempMarker && appState.map) {
      appState.map.removeLayer(this.tempMarker);
      this.tempMarker = null;
    }
  }

  /**
   * Abre el modal de reportar avistamiento
   */
  _openModal(lat, lng) {
    const modal = document.getElementById("modalAvistamiento");
    const coordsSpan = document.getElementById("modalLatLng");

    if (coordsSpan) {
      coordsSpan.textContent = `${lat.toFixed(6)}, ${lng.toFixed(6)}`;
    }

    if (modal) {
      modal.classList.remove('u-hidden');
    }

    this._loadDesaparecidosSelect();
  }

  /**
   * Cierra el modal de reportar
   */
  closeModal() {
    const modal = document.getElementById("modalAvistamiento");
    if (modal) {
      modal.classList.add('u-hidden');
    }
    this._removeTempMarker();

    const form = document.getElementById("formAvistamiento");
    if (form) form.reset();

    const result = document.getElementById("avistamientoResult");
    if (result) result.textContent = "";
  }

  /**
   * Carga desaparecidos en el select del modal
   */
  async _loadDesaparecidosSelect() {
    const select = document.getElementById("av_desaparecido");
    if (!select) return;

    try {
      const desaparecidos = await DesaparecidoService.obtenerTodos();
      select.innerHTML = '<option value="">-- Seleccionar --</option>';

      desaparecidos.forEach(d => {
        const option = createOption(d.id, createDesaparecidoOption(d));
        select.appendChild(option);
      });

    } catch (err) {
      console.error('Error cargando desaparecidos:', err);
      select.innerHTML = `<option value="">${err.message || 'Error al cargar'}</option>`;
    }
  }

  /**
   * Envía el formulario de avistamiento
   */
  async submitAvistamiento(formData) {
    const result = document.getElementById("avistamientoResult");

    if (!this.selectedLatLng) {
      showError(result, "No hay coordenadas seleccionadas");
      return;
    }

    const user = AuthService.getCurrentUser();
    if (!user) {
      showError(result, "Debes iniciar sesión");
      return;
    }

    showLoading(result, "Enviando...");
    setButtonState("btnSubmitAvistamiento", true);

    try {
      const body = {
        avistadorId: String(user.id),
        desaparecidoId: String(formData.desaparecidoId),
        latitud: this.selectedLatLng.lat,
        longitud: this.selectedLatLng.lng,
        descripcion: formData.descripcion,
        fotoUrl: formData.fotoUrl || null,
        publico: formData.publico
      };

      const created = await AvistamientoService.crear(body);

      showSuccess(result, "Avistamiento reportado exitosamente");

      // Agregar marker al mapa
      this._addMarker({
        id: created.id,
        desaparecidoId: formData.desaparecidoId,
        latitud: created.latitud,
        longitud: created.longitud,
        fechaFormateada: "Recién reportado",
        descripcion: created.descripcion,
        fotoUrl: created.fotoUrl,
        verificado: false,
        desaparecidoNombre: formData.desaparecidoNombre || "",
        desaparecidoApellido: "",
        avistadorNombre: user.nombre || user.dni
      });

      setTimeout(() => {
        this.closeModal();
        this._deactivateReportMode();
        this._updateEstado("✅ Avistamiento agregado al mapa", "green");
      }, 1000);

    } catch (err) {
      showError(result, err.message);
    } finally {
      setButtonState("btnSubmitAvistamiento", false);
    }
  }

  /**
   * Desactiva el modo reportar
   */
  _deactivateReportMode() {
    this.isReportMode = false;

    const btnToggle = document.getElementById("btnToggleReportar");
    if (btnToggle) {
      btnToggle.textContent = "📍 Activar Modo Reportar";
      btnToggle.classList.remove('button--danger');
      btnToggle.classList.add('button--primary');
    }

    const mapElement = document.getElementById("map");
    if (mapElement) {
      mapElement.classList.remove('map--reporting');
    }

    this._checkIfShouldDisableListeners();
  }

  /**
   * Carga los avistamientos iniciales
   */
  async loadAvistamientos() {
    if (this.filters) {
      this.filters.applyCurrentFilter();
    }
  }

  /**
   * Renderiza avistamientos en el mapa
   */
  _renderAvistamientos(avistamientos) {
    appState.markersLayer.clearLayers();
    if (avistamientos.length === 0) return;
    avistamientos.forEach(a => this._addMarker(a));
  }

  /**
   * Agrega un marker al mapa
   */
  _addMarker(avistamiento) {
    const iconUrl = getMarkerIconUrl(avistamiento.verificado, avistamiento.desaparecidoId);

    const customIcon = L.icon({
      iconUrl,
      ...MARKER_ICON_CONFIG
    });

    const marker = L.marker([avistamiento.latitud, avistamiento.longitud], { icon: customIcon });
    const popupContent = createPopupContent(avistamiento);

    marker.bindPopup(popupContent, {
      maxWidth: 300,
      minWidth: 250,
      autoPan: false,
      keepInView: false
    });

    marker.on('click', () => {
      const px = appState.map.project(marker.getLatLng());
      px.y -= 160;
      const targetLatLng = appState.map.unproject(px);

      appState.map.setView(targetLatLng, appState.map.getZoom(), { animate: true });
      marker.openPopup();
    });

    marker.addTo(appState.markersLayer);
  }

  /**
   * Actualiza el mapa recargando los avistamientos
   */
  async refresh() {
    try {
      this._updateEstado('Actualizando…', '#666');

      // Si hay sistema de filtros, pedile que vuelva a aplicar/traer datos
      if (this.filters && typeof this.filters.applyCurrentFilter === 'function') {
        // Si tu MapFilters no devuelve el array, igual servirá si internamente hace render;
        // si SÍ devuelve, lo renderizamos acá también por si acaso.
        const avs = await this.filters.applyCurrentFilter({ forceReload: true });
        if (Array.isArray(avs)) this._renderAvistamientos(avs);
      } else {
        // Fallback sin filtros: traé todo/publicos y renderizá
        const avs = (await AvistamientoService.obtenerPublicos?.())
                 ?? (await AvistamientoService.obtenerTodos?.())
                 ?? [];
        this._renderAvistamientos(avs);
      }

      this._updateEstado('✅ Mapa actualizado', 'green');
    } catch (err) {
      console.error('Error refrescando mapa:', err);
      this._updateEstado('❌ Error al actualizar', 'red');
    }
  }

  /**
   * Actualiza el mensaje de estado del mapa
   */
  _updateEstado(mensaje, color) {
    if (this.mapaEstado) {
      this.mapaEstado.textContent = mensaje;
      this.mapaEstado.style.color = color;
    }
  }
}

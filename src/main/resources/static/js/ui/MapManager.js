import { appState } from '../config/state.js';
import { AuthService } from '../services/AuthService.js';
import { DesaparecidoService } from '../services/DesaparecidoService.js';
import { AvistamientoService } from '../services/AvistamientoService.js';
import { getMarkerIconUrl, MARKER_ICON_CONFIG } from '../utils/colors.js';
import { createPopupContent, createDesaparecidoOption } from '../utils/templates.js';
import { isValidSelection, showError, showSuccess, showLoading } from '../utils/validators.js';
import { getValue, setButtonState, createOption } from '../utils/dom.js';

/**
 * Gestor del mapa Leaflet
 */
export class MapManager {

  constructor() {
    this.mapaEstado = document.getElementById("mapaEstado");
    this.isReportMode = false;
    this.tempMarker = null;
    this.selectedLatLng = null;
    this._onMapClick = this._handleMapClick.bind(this);
        this._preloadMarkerIcons();
  }

  /**
   * Precarga las imágenes de los íconos para evitar problemas de carga
   * @private
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

  init() {
    const map = L.map('map').setView([-34.6037, -58.3816], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(map);

    appState.map = map;
    appState.markersLayer = L.layerGroup().addTo(map);

    this._updateEstado("✅ Mapa inicializado", "green");
    this._setupReportMode();
  }

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
        this._disableMapClick();
        this._removeTempMarker();
      }
    };
  }

  _enableMapClick() {
    if (appState.map) appState.map.on('click', this._onMapClick);
  }

  _disableMapClick() {
    if (appState.map) appState.map.off('click', this._onMapClick);
  }

  _handleMapClick(e) {
    if (!this.isReportMode) return;

    const { lat, lng } = e.latlng;
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

  _removeTempMarker() {
    if (this.tempMarker && appState.map) {
      appState.map.removeLayer(this.tempMarker);
      this.tempMarker = null;
    }
  }

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

  async submitAvistamiento(formData) {
    const result = document.getElementById("avistamientoResult");
    const btn = document.getElementById("btnSubmitAvistamiento");

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

        this._disableMapClick();
        this._updateEstado("✅ Avistamiento agregado al mapa", "green");
      }, 1000);

    } catch (err) {
      showError(result, err.message);
    } finally {
      setButtonState("btnSubmitAvistamiento", false);
    }
  }

  async loadAvistamientos() {
    if (!appState.map) return;

    this._updateEstado("Cargando avistamientos...", "#666");

    try {
      const avistamientos = await AvistamientoService.getAvistamientosParaMapa();
      appState.markersLayer.clearLayers();

      if (avistamientos.length === 0) {
        this._updateEstado("⚠️ No hay avistamientos registrados todavía", "#999");
        return;
      }

      avistamientos.forEach(a => this._addMarker(a));

      this._updateEstado(
        `✅ ${avistamientos.length} avistamiento${avistamientos.length !== 1 ? 's' : ''} cargado${avistamientos.length !== 1 ? 's' : ''}`,
        "green"
      );

    } catch (err) {
      this._updateEstado(`❌ Error: ${err.message}`, "red");
    }
  }

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

      appState.map.setView(targetLatLng, appState.map.getZoom(), {
        animate: true
      });

      marker.openPopup();
    });

    marker.addTo(appState.markersLayer);
  }

  _updateEstado(mensaje, color) {
    if (this.mapaEstado) {
      this.mapaEstado.textContent = mensaje;
      this.mapaEstado.style.color = color;
    }
  }
}
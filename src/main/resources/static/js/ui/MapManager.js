import { appState } from '../config/state.js';
import { AuthService } from '../services/AuthService.js';
import { DesaparecidoService } from '../services/DesaparecidoService.js';
import { AvistamientoService } from '../services/AvistamientoService.js';

/**
 * Gestor del mapa Leaflet
 */
export class MapManager {

  constructor() {
    this.mapaEstado = document.getElementById("mapaEstado");
    this.isReportMode = false;
    this.tempMarker = null;
    this.selectedLatLng = null;

    // 🛠️ Fix #1: conservar referencia del handler ligado
    this._onMapClick = this._handleMapClick.bind(this);
  }

  /**
   * Inicializa el mapa de Leaflet
   */
  init() {
    const map = L.map('map').setView([-34.6037, -58.3816], 12);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 19
    }).addTo(map);

    const markersLayer = L.layerGroup().addTo(map);

    // Guardar en estado global
    appState.map = map;
    appState.markersLayer = markersLayer;

    this._updateEstado("✅ Mapa inicializado", "green");
    this._setupReportMode();
  }

  /**
   * Configura el modo de reportar avistamientos
   * @private
   */
  _setupReportMode() {
    const btnToggle = document.getElementById("btnToggleReportar");
    const mapElement = document.getElementById("map");

    if (!btnToggle) return;

    btnToggle.onclick = () => {
      // Verificar autenticación
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
        this._updateEstado("🎯 Click en el mapa para marcar ubicación del avistamiento", "#0066cc");
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

  /**
   * Habilita el click en el mapa para reportar
   * @private
   */
  _enableMapClick() {
    if (!appState.map) return;
    appState.map.on('click', this._onMapClick); // usa la referencia guardada
  }

  /**
   * Deshabilita el click en el mapa
   * @private
   */
  _disableMapClick() {
    if (!appState.map) return;
    appState.map.off('click', this._onMapClick); // misma referencia -> se desuscribe bien
  }

  /**
   * Maneja el click en el mapa
   * @private
   */
  _handleMapClick(e) {
    if (!this.isReportMode) return;

    const { lat, lng } = e.latlng;
    this.selectedLatLng = { lat, lng };

    // Remover marker temporal anterior
    this._removeTempMarker();

    // Crear marker temporal
    const tempIcon = L.icon({
      iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });

    this.tempMarker = L.marker([lat, lng], { icon: tempIcon })
      .addTo(appState.map)
      .bindPopup("📍 Ubicación del avistamiento")
      .openPopup();

    // Abrir modal
    this._openModal(lat, lng);
  }

  /**
   * Remueve el marker temporal
   * @private
   */
  _removeTempMarker() {
    if (this.tempMarker && appState.map) {
      appState.map.removeLayer(this.tempMarker);
      this.tempMarker = null;
    }
  }

  /**
   * Abre el modal de reportar avistamiento
   * @private
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

    // Cargar lista de desaparecidos
    this._loadDesaparecidosSelect();
  }

  /**
   * Cierra el modal
   */
  closeModal() {
    const modal = document.getElementById("modalAvistamiento");
    if (modal) {
      modal.classList.add('u-hidden');
    }
    this._removeTempMarker();

    // Resetear formulario
    const form = document.getElementById("formAvistamiento");
    if (form) form.reset();

    const result = document.getElementById("avistamientoResult");
    if (result) result.textContent = "";
  }

  /**
   * Carga la lista de desaparecidos en el select
   * @private
   */
  async _loadDesaparecidosSelect() {
    const select = document.getElementById("av_desaparecido");
    if (!select) return;

    try {
      const desaparecidos = await DesaparecidoService.obtenerTodos();

      select.innerHTML = '<option value="">-- Seleccionar --</option>';

      desaparecidos.forEach(d => {
        const option = document.createElement('option');
        option.value = d.id;
        option.textContent = `${d.nombre} ${d.apellido} (DNI: ${d.dni})`;
        select.appendChild(option);
      });

    } catch (err) {
      console.error('Error cargando desaparecidos:', err);
      select.innerHTML = `<option value="">${err.message || 'Error al cargar'}</option>`;
    }
  }

  /**
   * Envía el avistamiento
   */
  async submitAvistamiento(formData) {
    const result = document.getElementById("avistamientoResult");
    const btn = document.getElementById("btnSubmitAvistamiento");

    if (!this.selectedLatLng) {
      if (result) {
        result.textContent = "❌ Error: No hay coordenadas seleccionadas";
        result.style.color = "red";
      }
      return;
    }

    const user = AuthService.getCurrentUser();
    if (!user) {
      if (result) {
        result.textContent = "❌ Debes iniciar sesión";
        result.style.color = "red";
      }
      return;
    }

    if (result) {
      result.textContent = "Enviando...";
      result.style.color = "#666";
    }
    if (btn) btn.disabled = true;

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

      console.log('📤 Body a enviar:', JSON.stringify(body, null, 2));
          console.log('📤 Tipos:', {
              avistadorId: typeof body.avistadorId,
              desaparecidoId: typeof body.desaparecidoId,
              latitud: typeof body.latitud,
              longitud: typeof body.longitud,
              descripcion: typeof body.descripcion,
              publico: typeof body.publico
          });

      const created = await AvistamientoService.crear(body);

      if (result) {
        result.textContent = "✅ Avistamiento reportado exitosamente";
        result.style.color = "green";
      }

      // Agregar marker al mapa inmediatamente
      this._addMarker({
        id: created.id,
        latitud: created.latitud,
        longitud: created.longitud,
        fechaFormateada: "Recién reportado",
        descripcion: created.descripcion,
        fotoUrl: created.fotoUrl, // puede venir como fotoUrl
        verificado: false,
        desaparecidoNombre: formData.desaparecidoNombre || "",
        desaparecidoApellido: "",
        avistadorNombre: user.nombre || user.dni
      });

      // Cerrar modal después de 1 segundo
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

        // Por las dudas, también deshabilitamos el listener
        this._disableMapClick();

        this._updateEstado("✅ Avistamiento agregado al mapa", "green");
      }, 1000);

    } catch (err) {
      if (result) {
        result.textContent = "❌ " + err.message;
        result.style.color = "red";
      }
    } finally {
      if (btn) btn.disabled = false;
    }
  }

  /**
   * Carga avistamientos en el mapa
   */
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

  /**
   * Agrega un marker al mapa
   * @private
   */
  _addMarker(avistamiento) {
    const iconUrl = avistamiento.verificado
      ? 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png'
      : 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-gold.png';

    const customIcon = L.icon({
      iconUrl: iconUrl,
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.9.4/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });

    const marker = L.marker([avistamiento.latitud, avistamiento.longitud], { icon: customIcon });

    // 🛠️ Fix #2: soportar ambos nombres de propiedad para la foto
    const foto = avistamiento.desaparecidoFoto ?? avistamiento.fotoUrl ?? '';

    const popupContent = `
      <div class="popup">
        <h3 class="popup__title">${avistamiento.desaparecidoNombre} ${avistamiento.desaparecidoApellido}</h3>
        ${foto ? `<img class="popup__image" src="${foto}" alt="Foto">` : ''}
        <p class="popup__text"><span class="popup__label">📅 Fecha:</span> ${avistamiento.fechaFormateada}</p>
        <p class="popup__text"><span class="popup__label">📝 Descripción:</span> ${avistamiento.descripcion}</p>
        <p class="popup__text"><span class="popup__label">👤 Reportado por:</span> ${avistamiento.avistadorNombre}</p>
        ${avistamiento.verificado
          ? '<span class="badge badge--verified">✓ Verificado</span>'
          : '<span class="badge badge--unverified">⚠ Sin verificar</span>'}
      </div>
    `;

    marker.bindPopup(popupContent);
    marker.addTo(appState.markersLayer);
  }

  /**
   * Actualiza el mensaje de estado del mapa
   * @private
   */
  _updateEstado(mensaje, color) {
    if (this.mapaEstado) {
      this.mapaEstado.textContent = mensaje;
      this.mapaEstado.style.color = color;
    }
  }
}

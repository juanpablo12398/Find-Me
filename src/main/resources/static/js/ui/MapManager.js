import { appState } from '../config/state.js';
import { AvistamientoService } from '../services/AvistamientoService.js';

/**
 * Gestor del mapa Leaflet
 */
export class MapManager {

  constructor() {
    this.mapaEstado = document.getElementById("mapaEstado");
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

    const popupContent = `
      <div class="popup">
        <h3 class="popup__title">${avistamiento.desaparecidoNombre} ${avistamiento.desaparecidoApellido}</h3>
        ${avistamiento.desaparecidoFoto ? `<img class="popup__image" src="${avistamiento.desaparecidoFoto}" alt="Foto">` : ''}
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
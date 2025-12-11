import { appState } from '../config/state.js';
import { AvistamientoService } from '../services/AvistamientoService.js';
import { DesaparecidoService } from '../services/DesaparecidoService.js';
import { createOption } from '../utils/dom.js';
import { FILTER_TYPES, MAP_MESSAGES, MAP_COLORS } from '../config/filter/constants.js';

export class MapFilters {

  constructor(mapManager) {
    this.mapManager = mapManager;

    // Estado del filtro activo
    this.filterType = FILTER_TYPES.TODOS;

    // Filtro por radio
    this.radioCenter = null;
    this.radioKm = 5;
    this.radioCircle = null;
    this.isSelectingRadioCenter = false;

    // Filtro por área
    this.areaBounds = null;
    this.areaRectangle = null;
    this.isDrawingArea = false;
    this.areaDrawHandlers = null;

    // Filtro por polígono
    this.poligonoCoords = null;
    this.poligonoLayer = null;
    this.isDrawingPolygon = false;
    this.drawingPolygonPoints = [];
    this.tempPolygonMarkers = [];

    // Filtro por desaparecido
    this.selectedDesaparecidoId = null;
  }

  async _loadDesaparecidosForFilter() {
    const select = document.getElementById('filtroDesaparecidoSelect');
    if (!select) return;

    select.innerHTML = '<option value="">Cargando personas…</option>';

    try {
      const lista = await DesaparecidoService.obtenerTodos();

      const options = ['<option value="">Selecciona una persona</option>'];
      for (const d of lista) {
        const label =
          [d.nombre, d.apellido].filter(Boolean).join(' ') ||
          d.dni ||
          d.id;
        options.push(`<option value="${String(d.id)}">${label}</option>`);
      }
      select.innerHTML = options.join('');
    } catch (e) {
      console.error('Error cargando desaparecidos para filtro:', e);
      select.innerHTML = '<option value="">No se pudo cargar</option>';
    }
  }

  init() {
    try { this._loadDesaparecidosForFilter(); } catch (e) { console.error(e); }
    try { this._setupEventListeners(); } catch (e) { console.error(e); }
    try { this._setupSidebarToggle(); } catch (e) { console.error(e); }

    window.addEventListener('keydown', (e) => {
      if (e.key === 'Escape') this.resetFilters();
    });

    this._activateFilter(FILTER_TYPES.TODOS);
  }

  _setupEventListeners() {
    const btnTodos = document.getElementById('filtroTodos');
    if (btnTodos) btnTodos.addEventListener('click', () => this._activateFilter(FILTER_TYPES.TODOS));

    const btnRadio = document.getElementById('filtroRadio');
    if (btnRadio) btnRadio.addEventListener('click', () => this._activateFilter(FILTER_TYPES.RADIO));

    const sliderRadio = document.getElementById('radioSlider');
    if (sliderRadio) {
      sliderRadio.addEventListener('input', (e) => {
        this.radioKm = parseInt(e.target.value);
        const out = document.getElementById('radioValue');
        if (out) out.textContent = `${this.radioKm} km`;
        if (this.radioCenter) {
          this._updateRadioCircle();
          this.applyCurrentFilter();
        }
      });
    }

    const btnCambiarPunto = document.getElementById('btnCambiarPuntoRadio');
    if (btnCambiarPunto) {
      btnCambiarPunto.addEventListener('click', () => {
        this.isSelectingRadioCenter = true;
        this.mapManager._updateEstado(MAP_MESSAGES.SELECT_CENTER.text, MAP_MESSAGES.SELECT_CENTER.color);
        this.mapManager._enableMapClick();
      });
    }

    const btnArea = document.getElementById('filtroArea');
    if (btnArea) btnArea.addEventListener('click', () => this._activateFilter(FILTER_TYPES.AREA));

    const btnPoligono = document.getElementById('filtroPoligono');
    if (btnPoligono) btnPoligono.addEventListener('click', () => this._activateFilter(FILTER_TYPES.POLIGONO));

    const selectDesaparecido = document.getElementById('filtroDesaparecidoSelect');
    if (selectDesaparecido) {
      selectDesaparecido.addEventListener('change', (e) => {
        if (e.target.value) {
          this.selectedDesaparecidoId = e.target.value;
          this._activateFilter(FILTER_TYPES.DESAPARECIDO);
        }
      });
    }

    const btnReset = document.getElementById('btnResetFiltros');
    if (btnReset) btnReset.addEventListener('click', () => this.resetFilters());
  }

  _setupSidebarToggle() {
    const sidebar = document.getElementById('sidebarFiltros');
    const btn     = document.getElementById('btnToggleFilters');
    if (!sidebar || !btn) return;

    const applyState = (open) => {
      sidebar.classList.toggle('sidebar--closed', !open);
      btn.setAttribute('aria-expanded', String(open));
      // Texto del botón (podés dejar solo “Filtros” si preferís)
      btn.innerHTML = open ? '🧰 Ocultar filtros' : '🧰 Filtros';

      // Recalcular Leaflet después de la transición
      setTimeout(() => { if (appState.map) appState.map.invalidateSize(); }, 220);
    };

    // Estado inicial (si está cerrado por CSS, respétalo)
    const startsOpen = !sidebar.classList.contains('sidebar--closed');
    applyState(startsOpen);

    // Toggle al click
    btn.addEventListener('click', () => {
      const willOpen = sidebar.classList.contains('sidebar--closed');
      applyState(willOpen);
    });
  }

  _activateFilter(type) {
    this._clearFilterVisuals();

    this.isSelectingRadioCenter = false;

    if (this.isDrawingArea) {
      this.isDrawingArea = false;
      this._disableAreaDrawing();
    }

    if (this.isDrawingPolygon) {
      this.isDrawingPolygon = false;
      this.drawingPolygonPoints = [];
      if (appState.map.doubleClickZoom && !appState.map.doubleClickZoom.enabled()) {
        appState.map.doubleClickZoom.enable();
      }
    }

    this._deactivateAllFilterButtons();
    this.filterType = type;

    const activeBtn = document.getElementById(`filtro${String(type).charAt(0).toUpperCase()}${String(type).slice(1).toLowerCase()}`);
    if (activeBtn) activeBtn.classList.add('filtro-item--active');

    const needsListeners = [FILTER_TYPES.RADIO, FILTER_TYPES.AREA, FILTER_TYPES.POLIGONO].includes(type);
    if (needsListeners) this.mapManager._enableMapClick();
    else this.mapManager._checkIfShouldDisableListeners();

    switch (type) {
      case FILTER_TYPES.TODOS:
        this._hideRadioControls();
        this.applyCurrentFilter();
        break;

      case FILTER_TYPES.RADIO:
        this.isSelectingRadioCenter = true;
        this.mapManager._updateEstado(MAP_MESSAGES.SELECT_CENTER.text + " del radio", MAP_MESSAGES.SELECT_CENTER.color);
        this._showRadioControls();
        break;

      case FILTER_TYPES.AREA:
        this._hideRadioControls();
        this.isDrawingArea = true;
        this.mapManager._updateEstado("🔲 Click IZQUIERDO para dibujar. Click DERECHO para terminar.", MAP_MESSAGES.DRAW_AREA.color);
        this._enableAreaDrawing();
        break;

      case FILTER_TYPES.POLIGONO:
        this._hideRadioControls();
        this.isDrawingPolygon = true;
        this.drawingPolygonPoints = [];
        if (appState.map.doubleClickZoom.enabled()) appState.map.doubleClickZoom.disable();
        this.mapManager._updateEstado("🔷 Click IZQUIERDO para agregar puntos. Click DERECHO para terminar.", MAP_MESSAGES.DRAW_POLYGON.color);
        break;

      case FILTER_TYPES.DESAPARECIDO:
        this._hideRadioControls();
        this.applyCurrentFilter();
        break;
    }

    this._updateResetButton();
  }

  handleMapClick(lat, lng) {
    if (this.isSelectingRadioCenter) {
      this.radioCenter = [lat, lng];
      this.isSelectingRadioCenter = false;
      this._updateRadioCircle();
      this.mapManager._updateEstado(MAP_MESSAGES.CENTER_SELECTED.text, MAP_MESSAGES.CENTER_SELECTED.color);
      this.applyCurrentFilter();
      return true;
    }

    if (this.isDrawingPolygon) {
      const pt = [lat, lng];

      this.drawingPolygonPoints.push(pt);

      const m = L.circleMarker(pt, {
        radius: 5, color: MAP_COLORS.POLYGON, fillColor: MAP_COLORS.POLYGON, fillOpacity: 0.8
      }).addTo(appState.map);
      this.tempPolygonMarkers.push(m);

      if (this.poligonoLayer) appState.map.removeLayer(this.poligonoLayer);
      if (this.drawingPolygonPoints.length > 1) {
        this.poligonoLayer = L.polygon(this.drawingPolygonPoints, {
          color: MAP_COLORS.POLYGON, fillColor: MAP_COLORS.POLYGON, fillOpacity: 0.1, weight: 2
        }).addTo(appState.map);
      }

      this.mapManager._updateEstado(`🔷 Punto ${this.drawingPolygonPoints.length} agregado. Click derecho para terminar`, MAP_MESSAGES.DRAW_POLYGON.color);
      return true;
    }

    return false;
  }

  handleMapDblClick() {
    if (this.isDrawingPolygon && this.drawingPolygonPoints.length >= 3) {
      this._finishPolygon();
      return true;
    }
    return false;
  }

  handleContextMenu(e) {
    // FIX: PREVENIR MENÚ CONTEXTUAL cuando se está dibujando
    if (this.isDrawingPolygon || this.isDrawingArea) {
      if (e && e.originalEvent) {
        e.originalEvent.preventDefault();
        e.originalEvent.stopPropagation();
      }
      L.DomEvent.preventDefault(e);
      L.DomEvent.stopPropagation(e);
    }

    if (this.isDrawingPolygon && this.drawingPolygonPoints.length >= 3) {
      this._finishPolygon();
      return;
    }

    if (this.isDrawingArea) {
      this.isDrawingArea = false;
      this._disableAreaDrawing();
      this._clearFilterVisuals();
      this.mapManager._updateEstado('⛔ Selección de área cancelada', '#666');
      this._updateResetButton();
      return;
    }

    if (this.isSelectingRadioCenter) {
      this.isSelectingRadioCenter = false;
      this._updateRadioCircle();
      this.mapManager._updateEstado('⛔ Selección de centro cancelada', '#666');
    }
  }

  _finishPolygon() {
    this.poligonoCoords = [...this.drawingPolygonPoints];
    this.isDrawingPolygon = false;
    if (appState.map.doubleClickZoom && !appState.map.doubleClickZoom.enabled()) {
      appState.map.doubleClickZoom.enable();
    }
    this.mapManager._updateEstado(MAP_MESSAGES.POLYGON_COMPLETE.text, MAP_MESSAGES.POLYGON_COMPLETE.color);
    this.applyCurrentFilter();
  }

  async applyCurrentFilter() {
    if (!appState.map) return;

    this.mapManager._updateEstado(MAP_MESSAGES.LOADING.text, MAP_MESSAGES.LOADING.color);

    try {
      let avistamientos = [];

      switch (this.filterType) {
        case FILTER_TYPES.TODOS:
          avistamientos = await AvistamientoService.getAvistamientosParaMapa();
          break;

        case FILTER_TYPES.RADIO:
          if (this.radioCenter) {
            avistamientos = await AvistamientoService.getEnRadio(
              this.radioCenter[0], this.radioCenter[1], this.radioKm
            );
          }
          break;

        case FILTER_TYPES.AREA:
          if (this.areaBounds) {
            const [[latMin, lngMin], [latMax, lngMax]] = this.areaBounds;
            avistamientos = await AvistamientoService.getEnArea(latMin, latMax, lngMin, lngMax);
          }
          break;

        case FILTER_TYPES.POLIGONO:
          if (this.poligonoCoords) {
            const wkt = this._coordsToWKT(this.poligonoCoords);
            avistamientos = await AvistamientoService.getEnPoligono(wkt);
          }
          break;

        case FILTER_TYPES.DESAPARECIDO:
          if (this.selectedDesaparecidoId) {
            avistamientos = await AvistamientoService.getPorDesaparecido(this.selectedDesaparecidoId);
          }
          break;
      }

      this._updateCounter(avistamientos.length);
      this.mapManager._renderAvistamientos(avistamientos);

      if (avistamientos.length === 0) {
        this.mapManager._updateEstado(MAP_MESSAGES.NO_RESULTS.text, MAP_MESSAGES.NO_RESULTS.color);
      } else {
        this.mapManager._updateEstado(
          `✅ ${avistamientos.length} avistamiento${avistamientos.length !== 1 ? 's' : ''} encontrado${avistamientos.length !== 1 ? 's' : ''}`,
          "green"
        );
      }

    } catch (err) {
      this.mapManager._updateEstado(`❌ Error: ${err.message}`, "red");
      console.error('Error aplicando filtro:', err);
    }
  }

  resetFilters() {
    this._clearFilterVisuals();
    this._deactivateAllFilterButtons();
    this._hideRadioControls();
    this._disableAreaDrawing();

    this.filterType = FILTER_TYPES.TODOS;
    this.radioCenter = null;
    this.areaBounds = null;
    this.poligonoCoords = null;
    this.selectedDesaparecidoId = null;
    this.isSelectingRadioCenter = false;
    this.isDrawingArea = false;
    this.isDrawingPolygon = false;
    this.drawingPolygonPoints = [];

    const select = document.getElementById('filtroDesaparecidoSelect');
    if (select) select.value = '';

    const btnTodos = document.getElementById('filtroTodos');
    if (btnTodos) btnTodos.classList.add('filtro-item--active');

    if (appState.map.doubleClickZoom && !appState.map.doubleClickZoom.enabled()) {
      appState.map.doubleClickZoom.enable();
    }

    this._updateResetButton();
    this.applyCurrentFilter();
  }

  _clearFilterVisuals() {
    if (this.radioCircle && appState.map) {
      appState.map.removeLayer(this.radioCircle);
      this.radioCircle = null;
    }

    if (this.areaRectangle && appState.map) {
      appState.map.removeLayer(this.areaRectangle);
      this.areaRectangle = null;
    }

    if (this.poligonoLayer && appState.map) {
      appState.map.removeLayer(this.poligonoLayer);
      this.poligonoLayer = null;
    }

    this.tempPolygonMarkers.forEach(marker => {
      if (appState.map) appState.map.removeLayer(marker);
    });
    this.tempPolygonMarkers = [];
  }

  _updateRadioCircle() {
    if (this.radioCircle && appState.map) {
      appState.map.removeLayer(this.radioCircle);
    }

    if (this.radioCenter) {
      this.radioCircle = L.circle(this.radioCenter, {
        radius: this.radioKm * 1000,
        color: MAP_COLORS.RADIO_CIRCLE,
        fillColor: MAP_COLORS.RADIO_CIRCLE,
        fillOpacity: 0.1,
        weight: 2
      }).addTo(appState.map);
    }
  }

  /**
   * Habilita el dibujo de área rectangular
   * FIX: Click izquierdo para dibujar, derecho para terminar
   * FIX: Desactiva dragging del mapa mientras se dibuja
   */
  _enableAreaDrawing() {
    if (!appState.map) return;

    const mapEl = appState.map.getContainer();
    L.DomUtil.disableTextSelection(mapEl);

    if (appState.map.dragging) {
      appState.map.dragging.disable();
    }

    let startLatLng = null;
    let isDrawing = false;

    const onMouseDown = (e) => {
      if (!this.isDrawingArea) return;

      // Solo botón IZQUIERDO (botón 0)
      if (e.originalEvent && e.originalEvent.button !== 0) return;

      startLatLng = e.latlng;
      isDrawing = true;
    };

    const onMouseMove = (e) => {
      if (!isDrawing || !startLatLng) return;

      if (this.areaRectangle) appState.map.removeLayer(this.areaRectangle);

      const bounds = L.latLngBounds(startLatLng, e.latlng);
      this.areaRectangle = L.rectangle(bounds, {
        color: MAP_COLORS.AREA_RECTANGLE,
        fillColor: MAP_COLORS.AREA_RECTANGLE,
        fillOpacity: 0.1,
        weight: 2,
        dashArray: '5, 5'
      }).addTo(appState.map);
    };

    const onMouseUp = (e) => {
      if (!isDrawing || !startLatLng) return;

      const bounds = L.latLngBounds(startLatLng, e.latlng);
      this.areaBounds = [
        [bounds.getSouth(), bounds.getWest()],
        [bounds.getNorth(), bounds.getEast()]
      ];

      if (this.areaRectangle) appState.map.removeLayer(this.areaRectangle);
      this.areaRectangle = L.rectangle(bounds, {
        color: MAP_COLORS.AREA_RECTANGLE,
        fillColor: MAP_COLORS.AREA_RECTANGLE,
        fillOpacity: 0.2,
        weight: 2
      }).addTo(appState.map);

      isDrawing = false;
      this.isDrawingArea = false;

      this._disableAreaDrawing();
      this.applyCurrentFilter();
    };

    const onContextMenu = (e) => {
      if (!this.isDrawingArea) return;

      if (e.originalEvent) {
        e.originalEvent.preventDefault();
        e.originalEvent.stopPropagation();
      }
      L.DomEvent.preventDefault(e);
      L.DomEvent.stopPropagation(e);

      if (isDrawing && startLatLng) {
        onMouseUp(e);
      } else {
        this.isDrawingArea = false;
        this._disableAreaDrawing();
        this._clearFilterVisuals();
        this.mapManager._updateEstado('⛔ Selección de área cancelada', '#666');
      }
    };

    this.areaDrawHandlers = { onMouseDown, onMouseMove, onMouseUp, onContextMenu };

    appState.map.on('mousedown', onMouseDown);
    appState.map.on('mousemove', onMouseMove);
    appState.map.on('mouseup', onMouseUp);
    appState.map.on('contextmenu', onContextMenu);

  }

  /**
   * Deshabilita el dibujo de área
   * FIX: Reactivar dragging del mapa
   */
  _disableAreaDrawing() {
    if (!appState.map || !this.areaDrawHandlers) return;

    const { onMouseDown, onMouseMove, onMouseUp, onContextMenu } = this.areaDrawHandlers;
    appState.map.off('mousedown', onMouseDown);
    appState.map.off('mousemove', onMouseMove);
    appState.map.off('mouseup', onMouseUp);
    appState.map.off('contextmenu', onContextMenu);

    this.areaDrawHandlers = null;

    if (appState.map.dragging) appState.map.dragging.enable();
  }

  _coordsToWKT(coords) {
    const wktCoords = coords.map(([lat, lng]) => `${lng} ${lat}`).join(', ');
    const firstCoord = `${coords[0][1]} ${coords[0][0]}`;
    return `POLYGON((${wktCoords}, ${firstCoord}))`;
  }

  _updateCounter(count) {
    const counterNumber = document.getElementById('counterNumber');
    const counterLabel = document.getElementById('counterLabel');

    if (counterNumber) counterNumber.textContent = count;
    if (counterLabel)  counterLabel.textContent = `avistamiento${count !== 1 ? 's' : ''} encontrado${count !== 1 ? 's' : ''}`;
  }

  _deactivateAllFilterButtons() {
    const filterButtons = document.querySelectorAll('.filtro-item');
    filterButtons.forEach(btn => btn.classList.remove('filtro-item--active'));
  }

  _showRadioControls() {
    const controls = document.getElementById('radioControls');
    if (controls) controls.classList.remove('u-hidden');
  }

  _hideRadioControls() {
    const controls = document.getElementById('radioControls');
    if (controls) controls.classList.add('u-hidden');
  }

  _updateResetButton() {
    const btnReset = document.getElementById('btnResetFiltros');
    if (!btnReset) return;
    if (this.filterType === FILTER_TYPES.TODOS) btnReset.classList.add('u-hidden');
    else btnReset.classList.remove('u-hidden');
  }

  needsMapListeners() {
    const interactiveFilters = [FILTER_TYPES.RADIO, FILTER_TYPES.AREA, FILTER_TYPES.POLIGONO];
    return interactiveFilters.includes(this.filterType) ||
           this.isSelectingRadioCenter ||
           this.isDrawingArea ||
           this.isDrawingPolygon;
  }
}
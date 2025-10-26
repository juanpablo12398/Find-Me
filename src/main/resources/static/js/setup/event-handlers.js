/**
 * Configuración de event handlers globales
 */

import { appState } from '../config/state.js';

/**
 * Configura eventos custom de la aplicación
 */
export function setupCustomEvents(mapManager, desaparecidosList) {
  window.addEventListener('loadMapa', () => {
    if (!appState.map) mapManager.init();
    mapManager.loadAvistamientos();
  });

  window.addEventListener('loadList', () => {
    desaparecidosList.load();
  });
}

/**
 * Configura controles del mapa
 */
export function setupMapControls(mapManager) {
  const btnReloadMapa = document.getElementById('btnReloadMapa');
  if (btnReloadMapa) {
    btnReloadMapa.onclick = () => mapManager.loadAvistamientos();
  }
}

/**
 * Configura el modal de avistamientos
 */
export function setupModal(mapManager) {
  const btnCloseModal = document.getElementById('btnCloseModal');
  if (btnCloseModal) {
    btnCloseModal.onclick = () => mapManager.closeModal();
  }

  const modal = document.getElementById('modalAvistamiento');
  if (modal) {
    modal.onclick = (e) => {
      if (e.target === modal) mapManager.closeModal();
    };
  }
}
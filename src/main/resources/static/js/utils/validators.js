/**
 * Valida que un campo no esté vacío (feedback inmediato)
 */
export function isNotEmpty(value) {
  return value != null && String(value).trim() !== '';
}

/**
 * Valida que un select tenga algo seleccionado (feedback inmediato)
 */
export function isValidSelection(value) {
  return isNotEmpty(value) && value !== 'undefined';
}

/**
 * Helpers de mensajes para el usuario
 */
export function showError(element, message) {
  if (!element) return;
  element.textContent = `❌ ${message}`;
  element.style.color = 'red';
}

export function showSuccess(element, message) {
  if (!element) return;
  element.textContent = `✅ ${message}`;
  element.style.color = 'green';
}

export function showLoading(element, message = 'Cargando...') {
  if (!element) return;
  element.textContent = message;
  element.style.color = '#666';
}
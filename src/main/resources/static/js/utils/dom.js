/**
 * Utilidades para manipulación del DOM
 */

/**
 * Obtiene el valor de un input por ID
 * @param {string} id - ID del elemento
 * @returns {string} Valor del input (trimmed)
 */
export function getValue(id) {
  const element = document.getElementById(id);
  return element ? element.value.trim() : '';
}

/**
 * Deshabilita un botón
 * @param {string} id - ID del botón
 * @param {boolean} disabled - Estado de deshabilitado
 */
export function setButtonState(id, disabled) {
  const button = document.getElementById(id);
  if (button) {
    button.disabled = disabled;
  }
}

/**
 * Limpia el contenido de un elemento
 * @param {string} id - ID del elemento
 */
export function clearElement(id) {
  const element = document.getElementById(id);
  if (element) {
    element.textContent = '';
  }
}

/**
 * Crea una opción de select
 * @param {string} value - Valor de la opción
 * @param {string} text - Texto visible
 * @returns {HTMLOptionElement}
 */
export function createOption(value, text) {
  const option = document.createElement('option');
  option.value = value;
  option.textContent = text;
  return option;
}
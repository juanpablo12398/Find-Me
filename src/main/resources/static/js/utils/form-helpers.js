/**
 * Helpers reutilizables para formularios
 */

import { showError, showSuccess, showLoading } from './validators.js';
import { getValue, setButtonState } from './dom.js';

/**
 * Obtiene los valores de múltiples campos
 * @param {string[]} ids - IDs de los campos
 * @returns {Object} Objeto con los valores
 */
export function getFormValues(ids) {
  const values = {};
  ids.forEach(id => {
    values[id] = getValue(id);
  });
  return values;
}

/**
 * Valida que un formulario sea válido
 * @param {HTMLFormElement} form - Formulario a validar
 * @param {string} resultId - ID del elemento de resultado
 * @returns {boolean} Si es válido
 */
export function validateForm(form, resultId) {
  if (!form.checkValidity()) {
    showError(resultId, 'Revisá los campos en rojo');
    return false;
  }
  return true;
}

/**
 * Maneja el flujo completo de un submit de formulario
 * @param {Object} config - Configuración del submit
 * @param {HTMLFormElement} config.form - Formulario
 * @param {string} config.resultId - ID del elemento de resultado
 * @param {string} config.buttonId - ID del botón
 * @param {Function} config.submitFn - Función async que ejecuta el submit
 * @param {Function} [config.onSuccess] - Callback de éxito
 * @param {string} [config.loadingMessage] - Mensaje durante carga
 */
export async function handleFormSubmit(config) {
  const {
    form,
    resultId,
    buttonId,
    submitFn,
    onSuccess,
    loadingMessage = 'Enviando...'
  } = config;

  if (!validateForm(form, resultId)) {
    return;
  }

  showLoading(resultId, loadingMessage);
  setButtonState(buttonId, true);

  try {
    const result = await submitFn();

    if (onSuccess) {
      await onSuccess(result);
    }

    form.reset();
  } catch (err) {
    showError(resultId, err.message);
  } finally {
    setButtonState(buttonId, false);
  }
}

/**
 * Resetea un formulario y su mensaje de resultado
 * @param {string} formId - ID del formulario
 * @param {string} resultId - ID del elemento de resultado
 */
export function resetFormAndResult(formId, resultId) {
  const form = document.getElementById(formId);
  const result = document.getElementById(resultId);

  if (form) form.reset();
  if (result) result.textContent = '';
}
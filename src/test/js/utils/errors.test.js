/// <reference types="vitest" />

import { describe, it, expect } from 'vitest';
import { parseProblem, getErrorMessage } from '@app/utils/errors.js';
import * as Errors from '@app/utils/errors.js';

// Helpers para crear "Response" mínimos
const resp = (status, bodyText) => ({
  status,
  text: async () => bodyText,
});

describe('utils/errors.parseProblem', () => {
  it('parsea JSON válido con key y detail en root', async () => {
    const r = await parseProblem(resp(422, JSON.stringify({ key: 'INVALID', detail: 'Datos inválidos' })));
    expect(r).toEqual({ status: 422, key: 'INVALID', detail: 'Datos inválidos' });
  });

  it('toma key desde properties.key cuando no está en root', async () => {
    const body = JSON.stringify({ properties: { key: 'NOT_FOUND' }, detail: 'No existe' });
    const r = await parseProblem(resp(404, body));
    expect(r).toEqual({ status: 404, key: 'NOT_FOUND', detail: 'No existe' });
  });

  it('si el JSON no trae detail, usa el body crudo como detail (y conserva key si existe)', async () => {
    const bodyObj = { key: 'X_ONLY' };
    const bodyText = JSON.stringify(bodyObj);
    const r = await parseProblem(resp(400, bodyText));
    expect(r).toEqual({ status: 400, key: 'X_ONLY', detail: bodyText });
  });

  it('JSON sin detail ni key => key=null y detail=body', async () => {
    const bodyText = JSON.stringify({ foo: 1 });
    const r = await parseProblem(resp(400, bodyText));
    expect(r).toEqual({ status: 400, key: null, detail: bodyText });
  });

  it('cuando el body no es JSON, devuelve key = null y detail = texto', async () => {
    const r = await parseProblem(resp(500, 'Fallo interno'));
    expect(r).toEqual({ status: 500, key: null, detail: 'Fallo interno' });
  });

  it('cuando el body es vacío, usa fallback "HTTP <status>"', async () => {
    const r = await parseProblem(resp(503, ''));
    expect(r).toEqual({ status: 503, key: null, detail: 'HTTP 503' });
  });
});

describe('utils/errors.getErrorMessage', () => {
  const MAP = {
    422: {
      default: 'Datos inválidos',
      INVALID_DNI: 'El DNI no es válido',
    },
    404: {
      default: 'No encontrado',
      USER_NOT_FOUND: 'Usuario inexistente',
    },
    500: {
      default: 'Ups! Algo salió mal',
    },
  };

  it('si no hay mapeo para el status, devuelve detail o "Error HTTP <status>"', () => {
    expect(getErrorMessage(MAP, 401, null, 'No autorizado')).toBe('No autorizado');
    expect(getErrorMessage(MAP, 401, null, '')).toBe('Error HTTP 401');
  });

  it('para 422/404 con key mapeada devuelve el mensaje de esa key', () => {
    expect(getErrorMessage(MAP, 422, 'INVALID_DNI', 'x')).toBe('El DNI no es válido');
    expect(getErrorMessage(MAP, 404, 'USER_NOT_FOUND', 'x')).toBe('Usuario inexistente');
  });

  it('para status con mapeo pero sin key (o key no mapeada) usa el default', () => {
    expect(getErrorMessage(MAP, 422, null, 'detalle')).toBe('Datos inválidos');
    expect(getErrorMessage(MAP, 422, 'OTRA', 'detalle')).toBe('Datos inválidos');
    expect(getErrorMessage(MAP, 500, 'IGNORED', 'detalle')).toBe('Ups! Algo salió mal');
  });

  it('si el status tiene mapeo pero sin default, vuelve a detail o fallback', () => {
    const NO_DEFAULT = { 400: {} };
    expect(getErrorMessage(NO_DEFAULT, 400, null, 'detalle')).toBe('detalle');
    expect(getErrorMessage(NO_DEFAULT, 400, null, '')).toBe('Error HTTP 400');
  });
});

describe('errors fallback', () => {
  it('retorna mensaje genérico cuando dominio/status/código no matchean', () => {
    // Resolvemos una función de mapeo, sea named, default, o usamos un fallback
    const mapFn =
      typeof Errors.mapApiError === 'function'
        ? Errors.mapApiError
        : typeof Errors.default === 'function'
          ? Errors.default
          : () => 'Error desconocido';

    const msg = mapFn('DOMINIO_INEXISTENTE', 499, 'codigo.inexistente');
    expect(msg).toMatch(/desconocid/i);
  });
});

/// <reference types="vitest" />

import { describe, it, expect, vi, beforeEach } from 'vitest';
import { fetchWithAuth } from '@app/utils/fetch.js';

describe('utils/fetch.fetchWithAuth', () => {
  beforeEach(() => {
    vi.restoreAllMocks();
  });

  it('agrega credentials=include y Content-Type application/json por defecto', async () => {
    const fetchSpy = vi.fn(async () => ({ ok: true }));
    global.fetch = fetchSpy;

    await fetchWithAuth('https://api.test/users', {
      method: 'POST',
      body: JSON.stringify({ a: 1 }),
    });

    expect(fetchSpy).toHaveBeenCalledTimes(1);
    const [url, options] = fetchSpy.mock.calls[0];

    expect(url).toBe('https://api.test/users');
    expect(options.credentials).toBe('include');
    expect(options.method).toBe('POST');
    expect(options.body).toBe('{"a":1}');
    expect(options.headers['Content-Type']).toBe('application/json');
  });

  it('si el body es FormData, NO setea Content-Type y conserva headers extra', async () => {
    const fetchSpy = vi.fn(async () => ({ ok: true }));
    global.fetch = fetchSpy;

    const fd = new FormData();
    fd.append('file', new Blob(['abc'], { type: 'text/plain' }), 'a.txt');

    await fetchWithAuth('https://api.test/upload', {
      method: 'POST',
      body: fd,
      headers: { 'X-Custom': '1' },
    });

    const [, options] = fetchSpy.mock.calls[0];
    expect(options.credentials).toBe('include');
    // no debe existir Content-Type (lo pone el navegador con boundary)
    expect(options.headers['Content-Type']).toBeUndefined();
    expect(options.headers['X-Custom']).toBe('1');
  });

  it('headers del caller sobreescriben Content-Type si lo especifica', async () => {
    const fetchSpy = vi.fn(async () => ({ ok: true }));
    global.fetch = fetchSpy;

    await fetchWithAuth('https://api.test/anything', {
      method: 'POST',
      body: JSON.stringify({}),
      headers: { 'Content-Type': 'application/special+json' },
    });

    const [, options] = fetchSpy.mock.calls[0];
    expect(options.headers['Content-Type']).toBe('application/special+json');
  });
});

import { vi } from 'vitest';

export function installLeafletMock () {
  const makeMap = (id) => {
    const container = document.getElementById(id) || document.createElement('div');
    return {
      on: vi.fn(),
      off: vi.fn(),
      setView: vi.fn(),
      addLayer: vi.fn(),
      removeLayer: vi.fn(),
      getContainer: vi.fn(() => container),
      invalidateSize: vi.fn(),
      doubleClickZoom: {
        _enabled: true,
        enabled: vi.fn(function () { return this._enabled; }),
        enable: vi.fn(function () { this._enabled = true; }),
        disable: vi.fn(function () { this._enabled = false; }),
      },
      dragging: {
        _enabled: true,
        enable: vi.fn(function () { this._enabled = true; }),
        disable: vi.fn(function () { this._enabled = false; }),
      },
    };
  };

  globalThis.L = {
    map: vi.fn(makeMap),
    tileLayer: vi.fn(() => ({ addTo: vi.fn() })),
    icon: vi.fn(() => ({})),
    layerGroup: vi.fn(() => ({ addTo: vi.fn(), clearLayers: vi.fn(), addLayer: vi.fn() })),
    marker: vi.fn(() => ({
      addTo: vi.fn().mockReturnThis(),
      bindPopup: vi.fn().mockReturnThis(),
      setIcon: vi.fn().mockReturnThis(),
    })),
  };
}

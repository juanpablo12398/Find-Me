/// <reference types="vitest" />
/* @vitest-environment jsdom */

import { describe, it, expect, beforeEach, vi } from 'vitest'

// Importamos el singleton real
import { appState } from '@app/config/state.js'

describe('AppState (singleton)', () => {
  beforeEach(() => {
    // Resetear estado interno entre tests
    appState._currentUser = null
    appState._map = null
    appState._markersLayer = null
    appState._listeners = []
  })

  it('estado inicial: currentUser/map/markersLayer null y sin listeners', () => {
    expect(appState.currentUser).toBeNull()
    expect(appState.map).toBeNull()
    expect(appState.markersLayer).toBeNull()
    expect(appState._listeners.length).toBe(0)
  })

  it('subscribe: agrega listeners y los guarda en orden', () => {
    const l1 = vi.fn()
    const l2 = vi.fn()

    appState.subscribe(l1)
    appState.subscribe(l2)

    expect(appState._listeners.length).toBe(2)
    expect(appState._listeners[0]).toBe(l1)
    expect(appState._listeners[1]).toBe(l2)
  })

  it('set currentUser: actualiza getter y notifica a TODOS los listeners con (type="user", data)', () => {
    const l1 = vi.fn()
    const l2 = vi.fn()
    appState.subscribe(l1)
    appState.subscribe(l2)

    const user = { dni: '123', email: 'a@a.com' }
    appState.currentUser = user

    expect(appState.currentUser).toBe(user)
    expect(l1).toHaveBeenCalledTimes(1)
    expect(l2).toHaveBeenCalledTimes(1)
    expect(l1).toHaveBeenCalledWith('user', user)
    expect(l2).toHaveBeenCalledWith('user', user)
  })

  it('set currentUser a null: también notifica con (type="user", null)', () => {
    const listener = vi.fn()
    appState.subscribe(listener)

    appState.currentUser = null

    expect(appState.currentUser).toBeNull()
    expect(listener).toHaveBeenCalledTimes(1)
    expect(listener).toHaveBeenCalledWith('user', null)
  })

  it('map: setter/getter funcionan y NO disparan notificación', () => {
    const listener = vi.fn()
    appState.subscribe(listener)

    const fakeMap = { id: 'map1' }
    appState.map = fakeMap

    expect(appState.map).toBe(fakeMap)
    expect(listener).not.toHaveBeenCalled()
  })

  it('markersLayer: setter/getter funcionan y NO disparan notificación', () => {
    const listener = vi.fn()
    appState.subscribe(listener)

    const layer = { id: 'layer1' }
    appState.markersLayer = layer

    expect(appState.markersLayer).toBe(layer)
    expect(listener).not.toHaveBeenCalled()
  })

  it('listeners posteriores reciben SOLO cambios posteriores (no se re-ejecutan retrospectivamente)', () => {
    const l1 = vi.fn()
    const l2 = vi.fn()

    // suscribimos primero l1
    appState.subscribe(l1)

    // cambio 1
    const user1 = { dni: '111' }
    appState.currentUser = user1

    // ahora suscribimos l2 (no debe recibir user1)
    appState.subscribe(l2)

    // cambio 2
    const user2 = { dni: '222' }
    appState.currentUser = user2

    expect(l1).toHaveBeenCalledTimes(2)
    expect(l1).toHaveBeenNthCalledWith(1, 'user', user1)
    expect(l1).toHaveBeenNthCalledWith(2, 'user', user2)

    expect(l2).toHaveBeenCalledTimes(1)
    expect(l2).toHaveBeenCalledWith('user', user2)
  })

  it('_notifyListeners: itera todos los listeners registrados (smoke test indirecto)', () => {
    const calls = []
    const l1 = vi.fn((type, data) => calls.push(['l1', type, data]))
    const l2 = vi.fn((type, data) => calls.push(['l2', type, data]))
    appState.subscribe(l1)
    appState.subscribe(l2)

    const user = { dni: '999' }
    appState.currentUser = user

    expect(calls).toEqual([
      ['l1', 'user', user],
      ['l2', 'user', user],
    ])
  })
})

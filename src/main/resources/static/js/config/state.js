// === Estado global de la aplicación ===
class AppState {
  constructor() {
    this._currentUser = null;
    this._map = null;
    this._markersLayer = null;
    this._listeners = [];
  }

  // Current User
  get currentUser() {
    return this._currentUser;
  }

  set currentUser(user) {
    this._currentUser = user;
    this._notifyListeners('user', user);
  }

  // Map
  get map() {
    return this._map;
  }

  set map(mapInstance) {
    this._map = mapInstance;
  }

  // Markers Layer
  get markersLayer() {
    return this._markersLayer;
  }

  set markersLayer(layer) {
    this._markersLayer = layer;
  }

  // Observer pattern para reaccionar a cambios
  subscribe(listener) {
    this._listeners.push(listener);
  }

  _notifyListeners(type, data) {
    this._listeners.forEach(listener => listener(type, data));
  }
}

// Singleton (instancia única)
export const appState = new AppState();
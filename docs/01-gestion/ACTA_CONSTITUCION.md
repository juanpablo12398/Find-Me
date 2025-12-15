# ACTA DE CONSTITUCIÓN DEL PROYECTO
## FIND ME - Sistema de Avistamiento de Personas Desaparecidas

---

## 1. INFORMACIÓN GENERAL DEL PROYECTO

**Nombre del Proyecto:** Find Me  
**Descripción breve:** Plataforma web para registro y visualización de avistamientos geolocalizados de personas desaparecidas, orientada a reducir los tiempos de respuesta en las primeras 48 horas críticas.

**Fecha de Inicio:** 01/01/2025  
**Fecha de Finalización Estimada:** 20/02/2026

**Estructura de Entregas Académicas:**
- **Entrega 1 (MVP Técnico):** 20/12/2025
- **Entrega 2 (Sistema Completo):** 20/02/2026

**Versión del Documento:** 3.1  
**Fecha de Elaboración:** 21/11/2024  
**Fecha de Última Actualización:** 08/12/2025

**Director del Proyecto:** Juan Pablo Verdondoni  
**Sponsor/Patrocinador:** Juan Pablo Verdondoni  
**Organización:** Proyecto personal – Portfolio profesional

> **Nota:** El detalle ampliado se presenta en el **Anexo**.

---

## 2. JUSTIFICACIÓN Y PROPÓSITO DEL PROYECTO

### 2.1 Contexto

En Argentina, la ausencia de una herramienta ciudadana centralizada para reportar y rastrear avistamientos de personas desaparecidas con geolocalización en tiempo real genera demoras críticas en las primeras horas de búsqueda, período reconocido internacionalmente como fundamental para localizar a la persona con vida.

Actualmente existen dispositivos institucionales (SIFEBU, línea 142, Missing Children, Alerta Sofía), pero están orientados principalmente a la denuncia formal, la coordinación interinstitucional y la difusión masiva, no a la captura estructurada de avistamientos ciudadanos georreferenciados.

### 2.2 Problemática

La problemática central que aborda el proyecto se vincula con:

- **Falta de visibilidad inmediata** de casos de desaparición en un único punto de acceso ciudadano.
- **Dispersión de la información** entre múltiples canales (redes sociales, medios, denuncias policiales, ONG, llamadas telefónicas).
- **Ausencia de herramientas de geolocalización accesibles** para centralizar avistamientos en tiempo real.
- **Dificultad para coordinar búsquedas** en contextos de catástrofes masivas o eventos críticos (ej.: Cromañón, Once, accidentes, incendios).
- **Pérdida de información temporal crítica** durante las primeras 24–48 horas posteriores a la desaparición.

### 2.3 Propósito

El propósito del proyecto es **desarrollar una plataforma web** que permita:

- Visibilizar desapariciones de personas en distintas situaciones (catástrofes, desapariciones voluntarias, posibles secuestros, personas con Alzheimer u otras condiciones, etc.).
- Centralizar avistamientos georreferenciados en tiempo real mediante un mapa interactivo.
- Facilitar la colaboración ciudadana en la búsqueda de personas desaparecidas.
- Proveer información inmediata y estructurada a familiares, ONGs y autoridades competentes.

De este modo, la plataforma busca **reducir la pérdida de información crítica en las primeras 48 horas** posteriores a una desaparición, integrando participación ciudadana y datos geolocalizados en un único sistema.

### 2.4 Beneficios Esperados

- **Reducción del tiempo de búsqueda**, al registrar reportes de avistamientos de forma inmediata y georreferenciada.
- **Mayor participación ciudadana** en casos de desaparición, con un canal claro y sencillo para reportar.
- **Centralización de información dispersa**, actualmente distribuida entre redes, llamadas, ONG y denuncias.
- **Visibilidad continua de casos sin resolver**, mediante un fichero y un mapa interactivo.
- **Herramienta gratuita y accesible** desde dispositivos móviles y desktop, orientada a impacto social.

---

## 3. OBJETIVOS DEL PROYECTO

### 3.1 Objetivo General

**Desarrollar una aplicación web funcional que permita a ciudadanos y autoridades reportar, visualizar y buscar avistamientos geolocalizados de personas desaparecidas en Argentina, reduciendo el tiempo de respuesta en las primeras 48 horas críticas mediante la centralización de información dispersa en un mapa interactivo y un sistema de fichero estructurado.**

### 3.2 Resumen de Objetivos Específicos

El proyecto se estructura en objetivos específicos agrupados en seis dimensiones:

1. **Objetivos Técnicos (Infraestructura y Arquitectura)**
    - Implementar un backend RESTful en Java Spring Boot con PostgreSQL + PostGIS.
    - Desarrollar un frontend responsivo con mapas interactivos (Leaflet).
    - Diseñar un modelo de datos con soporte para consultas espaciales.

2. **Objetivos Funcionales (Features Core)**
    - Implementar CRUD completo de avistadores, personas desaparecidas y avistamientos.
    - Visualizar avistamientos geolocalizados en un mapa interactivo.
    - Desarrollar un sistema de búsqueda (fichero) por DNI, nombre y apellido.
    - Incorporar perfiles individuales con timeline de avistamientos y diferenciación visual de casos recientes.

3. **Objetivos de Integración y Datos**
    - Preparar endpoints y estructuras para futura integración con SIFEBU u otros organismos.
    - Explorar validación de DNI mediante base municipal piloto (Vicente López).

4. **Objetivos de Calidad y Experiencia de Usuario**
    - Asegurar usabilidad multidispositivo (mobile, tablet, desktop).
    - Optimizar performance para visualizar grandes volúmenes de marcadores.
    - Mantener una cobertura de tests progresiva (≥70% en E1, ≥80% en E2).

5. **Objetivos de Validación con Usuarios**
    - Realizar pruebas de aceptación con usuarios reales (familiares, voluntarios, ciudadanos) en Entrega 2.

6. **Objetivos de Gestión del Proyecto**
    - Completar Entrega 1 y Entrega 2 en las fechas previstas.
    - Gestionar el proyecto dentro de un máximo de 150 horas/hombre.
    - Mantener presupuesto en $0 ARS utilizando herramientas gratuitas y open source.

> **Detalle completo de objetivos (OE-T*, OE-F*, OE-I*, OE-Q*, OE-V*, OE-G*) en Anexo 1, sección 2.**

---

## 4. ALCANCE DEL PROYECTO

### 4.1 Alcance – Entrega 1 (MVP Técnico – Diciembre 2025)

**Objetivo de la Entrega 1:** Demostrar la viabilidad técnica mediante un prototipo funcional que implemente el **núcleo de la arquitectura** y las **funcionalidades base**.

**Incluye (resumen):**

- Backend API RESTful con Spring Boot + PostgreSQL + PostGIS.
- CRUD completo de:
    - Avistadores
    - Personas desaparecidas
    - Avistamientos
- Relaciones entre entidades (persona ↔ avistamientos ↔ avistador).
- Mapa interactivo con Leaflet que muestra avistamientos geolocalizados.
- Formularios de carga para las tres entidades, con validaciones básicas.
- Autenticación JWT básica.
- Despliegue en ambiente productivo (tier gratuito).
- Documentación técnica mínima (arquitectura + APIs).
- Cobertura de tests ≥ 70 % sobre lógica crítica.

### 4.2 Alcance – Entrega 2 (Sistema Completo – Febrero 2026)

**Objetivo de la Entrega 2:** Completar las funcionalidades previstas, optimizar el sistema y prepararlo para uso real por usuarios finales y ONGs.

**Incluye (resumen):**

- Sistema de búsqueda multiparámetro (DNI exacto, nombre/apellido aproximado).
- Perfiles individuales de personas con:
    - Mapa personalizado de avistamientos.
    - Timeline cronológica.
- Diferenciación visual de casos recientes (últimos 30 días) en el mapa.
- Clustering de marcadores y optimizaciones de performance.
- Validación de DNI con base municipal (si se obtiene acceso; en caso contrario, dejar documentado como opcional).
- UAT con al menos 3 usuarios externos.
- Cobertura de tests ≥ 80 % y tests de integración E2E.
- Documentación de usuario, guía de deployment y plan de difusión.

### 4.3 Fuera del Alcance

Quedan explícitamente **fuera del alcance** de este proyecto (ambas entregas):

- Aplicaciones móviles nativas (iOS/Android).
- Integración en tiempo real con RENAPER o bases de datos nacionales.
- Integración real-time con SIFEBU (se limita a dejar endpoints preparados).
- Sistema de notificaciones push o e-mail masivo.
- Integración automática con redes sociales.
- Módulos avanzados de machine learning para detección de patrones.
- Alertas masivas tipo “Alerta Sofía” integradas institucionalmente.

> El detalle funcional del alcance por entrega, así como la lista de funcionalidades fuera de alcance ampliada, se encuentra en el Anexo 1, sección 3.

---

## 5. ENTREGABLES E HITOS PRINCIPALES

### 5.1 Entregables Resumidos

| # | Entregable Principal | Descripción | Entrega |
|---|----------------------|-------------|---------|
| 1 | Documento de Arquitectura | Diseño técnico, modelo de datos, diagramas | E1 |
| 2 | Backend API v1.0 | CRUD de entidades principales + PostGIS | E1 |
| 3 | Frontend v1.0 | Mapa con avistamientos + formularios básicos | E1 |
| 4 | **MVP Técnico Completo** | Sistema base desplegado y operativo | **E1** |
| 5 | Sistema de Búsqueda | Fichero multiparámetro | E2 |
| 6 | Perfiles Individuales | Mapas personalizados + timeline | E2 |
| 7 | Suite de Tests Completa | Unitarios + integración (≥80 % cobertura) | E2 |
| 8 | UAT + Ajustes Finales | Pruebas con usuarios reales | E2 |
| 9 | **Sistema en Producción** | Deploy final + documentación completa | **E2** |

### 5.2 Hitos Principales

| Hito | Fecha | Entrega | Criterio de Aceptación |
|------|-------|---------|------------------------|
| H1: Diseño completado | 28/02/2025 | E1 | Arquitectura aprobada, modelo de datos definido |
| H2: Backend operativo | 30/06/2025 | E1 | API CRUD funcional con PostGIS |
| H3: Frontend integrado | 30/09/2025 | E1 | Mapa muestra avistamientos correctamente |
| H4: CRUD completo | 30/11/2025 | E1 | Todas las entidades base operativas |
| **H5: ENTREGA 1 – MVP Técnico** | **20/12/2025** | **E1** | Sistema base desplegado y testeado |
| H6: Búsqueda operativa | 10/01/2026 | E2 | Fichero devuelve resultados correctos en <3s |
| H7: Perfiles individuales listos | 25/01/2026 | E2 | Mapas personalizados y timeline funcional |
| H8: UAT exitosa | 15/02/2026 | E2 | Usuarios reales validan el sistema |
| **H9: ENTREGA 2 – Sistema Completo** | **20/02/2026** | **E2** | Sistema listo para adopción real |

---

## 6. SUPUESTOS Y RESTRICCIONES

### 6.1 Supuestos Clave

- El desarrollador dispone de aproximadamente 15–20 horas semanales para el proyecto.
- Se podrá utilizar hosting y servicios cloud en tiers gratuitos (Vercel, Railway/Render, Cloudinary, etc.).
- Los usuarios objetivo acceden principalmente desde navegadores modernos en dispositivos móviles y de escritorio.
- La validación de DNI con base municipal es deseable pero no crítica; si no se logra integración, quedará documentada como funcionalidad opcional.

### 6.2 Restricciones Principales

- **Tiempo:**
    - Entrega 1: 20/12/2025 (no modificable).
    - Entrega 2: 20/02/2026 (no modificable).
    - Máximo total: 150 horas/hombre.

- **Presupuesto:**
    - Presupuesto total: $0 ARS (solo herramientas gratuitas u open source).

- **Tecnología:**
    - Backend obligatorio en Java Spring Boot (requisito académico).
    - Base de datos: PostgreSQL + PostGIS.
    - Sin uso de APIs comerciales de mapas (uso de Leaflet + OpenStreetMap).

- **Legal y normativo:**
    - Cumplimiento de la Ley 25.326 (Protección de Datos Personales).
    - Manejo responsable de datos de menores y personas vulnerables.
    - No almacenamiento de datos biométricos.
    - Consentimiento explícito para la publicación de datos personales.

---

## 7. RIESGOS PRINCIPALES (RESUMEN)

| ID | Riesgo | Prob. | Impacto | Mitigación |
|----|--------|-------|---------|-----------|
| R1 | Falta de acceso a base municipal para validar DNI | Alta | Medio | Dejar validación como opcional y documentar; evaluar alternativas públicas. |
| R2 | Problemas de rendimiento con muchos marcadores | Media | Alto | Implementar clustering, paginación y optimización de queries en Entrega 2. |
| R3 | Mal uso del sistema (reportes falsos, trolls) | Media | Alto | Moderación básica, límites de uso, términos y condiciones claros. |
| R4 | Riesgos legales por tratamiento de datos sensibles | Media | Crítico | Redacción cuidada de términos, asesoramiento básico, minimización de datos. |
| R5 | Falta de tiempo para completar todas las funcionalidades planificadas | Media | Alto | Priorización de objetivos Must Have; posibilidad de postergar funcionalidades deseables (Could Have). |

> El detalle completo de riesgos (R1–R12), con probabilidad, impacto y estrategias de mitigación ampliadas, se presenta en el Anexo 1, sección 4.

---

## 8. STAKEHOLDERS PRINCIPALES

| Stakeholder | Rol | Interés | Estrategia |
|-------------|-----|---------|-----------|
| Juan Pablo Verdondoni | Director/Sponsor | Crítico | Gestión directa del proyecto. |
| Tribunal académico | Evaluador | Alto | Informar avances y cumplir requerimientos formales. |
| Familias de personas desaparecidas | Usuarios finales | Alto | Considerar sus necesidades en diseño y UAT. |
| Ciudadanos colaboradores | Usuarios reportantes | Medio | Ofrecer interfaz simple y directa. |
| ONGs (ej. Missing Children) | Potenciales aliados | Alto | Presentar sistema tras Entrega 2 para exploración de uso. |

---

## 9. ORGANIZACIÓN DEL PROYECTO

### 9.1 Roles y Responsabilidades

| Rol | Responsable | Responsabilidades |
|-----|-------------|-------------------|
| Sponsor | Juan Pablo Verdondoni | Aprobación de cambios, definición de prioridades. |
| Director de Proyecto | Juan Pablo Verdondoni | Planificación, seguimiento, gestión de riesgos. |
| Arquitecto de Software | Juan Pablo Verdondoni | Diseño de arquitectura y modelo de datos. |
| Desarrollador Backend | Juan Pablo Verdondoni | Implementación de API y lógica de negocio. |
| Desarrollador Frontend | Juan Pablo Verdondoni | Implementación de UI y mapas. |
| Tester QA | Juan Pablo Verdondoni | Pruebas, detección y reporte de errores. |

---

## 10. APROBACIONES

| Rol | Nombre | Firma | Fecha |
|-----|--------|-------|-------|
| Sponsor | Juan Pablo Verdondoni | _____________ | ____/____/2026 |
| Director del Proyecto | Juan Pablo Verdondoni | _____________ | ____/____/2026 |

---

## 11. CONTROL DE VERSIONES

| Versión | Fecha | Autor | Descripción del Cambio |
|---------|-------|-------|------------------------|
| 1.0 | 21/11/2024 | JPV | Versión inicial del acta de constitución. |
| 2.0 | 08/12/2025 | JPV | Ajuste de fechas, alcance por entregas, criterios de aceptación. |
| 3.0 | 08/12/2025 | JPV | Integración de objetivos detallados como referencia a anexos. |
| 3.1 | 08/12/2025 | JPV | Versión final resumida del acta + referencia formal al Anexo 1. |

---

**Fin del documento – Acta de Constitución del Proyecto Find Me**  
(Ver Anexo 1 para detalles de objetivos, alcance fino y criterios de aceptación.)

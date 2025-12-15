# Documentación del Proyecto Find Me

Sistema de avistamiento de personas desaparecidas en Argentina.

**Proyecto:** Trabajo Final Académico - Universidad Tecnológica Nacional (UTN)  
**Autor:** Juan Pablo Verdondoni  
**Período:** Enero 2025 - Febrero 2026

---

## Estructura de la Documentación

### 01. Gestión del Proyecto

Documentos de planificación, objetivos, alcance y cronograma.

| Documento | Descripción |
|-----------|-------------|
| **[Acta de Constitución](../01-gestion/ACTA_CONSTITUCION.md)** | Marco general del proyecto, stakeholders, objetivos resumidos |
| **[Anexo 1 - Documentación Detallada](../01-gestion/ANEXO_1_PROYECTO.md)** | Justificación completa, objetivos específicos SMART, KPIs, criterios de aceptación, beneficios esperados, hitos detallados |
| **[Cronograma Gantt](../01-gestion/GANTT_PROYECTO.mmd)** | Diagrama de Gantt completo 2025-2026 con fases, tareas y ruta crítica |

---

### 02. Arquitectura del Sistema

Diseño técnico, patrones arquitectónicos y decisiones de diseño.

| Documento | Descripción |
|-----------|-------------|
| **[Documentación Técnica Completa](../02-arquitectura/DOCUMENTACION_TECNICA.md)** | Arquitectura limpia (Clean Architecture), modelo de dominio, capas, stack tecnológico, PostGIS, seguridad JWT, frontend, testing, deployment |

**Contenido clave:**
- Patrón arquitectónico (Clean Architecture + Hexagonal)
- Modelo de datos y relaciones
- Sistema de validación (Rules + Policies)
- Integración PostGIS y consultas espaciales
- Principios SOLID aplicados

---

### 03. Guías de Desarrollo

Información práctica para desarrolladores.

| Documento | Descripción |
|-----------|-------------|
| **[Guía de Instalación](./03-desarrollo/GUIA_INSTALACION.md)** | Setup local paso a paso, requisitos, configuración de entorno |
| **[Stack Tecnológico](./03-desarrollo/STACK_TECNOLOGICO.md)** | Detalle de todas las tecnologías utilizadas y su justificación |

**Stack resumido:**
- **Backend:** Java 17, Spring Boot 3.2+, PostgreSQL 15 + PostGIS
- **Frontend:** JavaScript ES6+, Leaflet.js, HTML5/CSS3
- **Testing:** JUnit 5, Mockito, Vitest
- **Deploy:** Railway/Render + Vercel/Netlify

---

### 04. Documentación de Usuario

Guías para usuarios finales del sistema.

| Documento | Descripción | Estado |
|-----------|-------------|--------|
| **[Manual de Usuario](./04-usuario/MANUAL_USUARIO.md)** | Guía paso a paso para usar Find Me | Pendiente E2 |

**Contenido previsto:**
- Cómo registrar una persona desaparecida
- Cómo reportar un avistamiento
- Cómo usar el mapa y filtros
- Cómo realizar búsquedas (DNI, nombre, apellido)

---

### 05. Deployment e Infraestructura

Guías de despliegue y configuración de entornos.

| Documento | Descripción | Estado |
|-----------|-------------|--------|
| **[Guía de Deployment](./05-deployment/GUIA_DEPLOYMENT.md)** | Instrucciones completas de despliegue en producción | Básico E1 / Completo E2 |

**Contenido previsto:**
- Despliegue de backend (Railway/Render)
- Despliegue de frontend (Vercel/Netlify)
- Configuración de PostgreSQL + PostGIS en cloud
- Variables de entorno
- Configuración de Cloudinary
- HTTPS y dominios

---

### 06. Calidad y Testing

Estrategia de pruebas, reportes de cobertura y validación con usuarios.

| Documento | Descripción | Estado |
|-----------|-------------|--------|
| **[Estrategia de Testing](./06-calidad/ESTRATEGIA_TESTING.md)** | Plan de pruebas, tipos de tests, cobertura objetivo | Completo E1 |

**Métricas objetivo:**
- **E1:** Cobertura mínima del 70% en tests unitarios
- **E2:** Cobertura mínima del 80% + tests de integración E2E
- Componentes críticos: 100% de cobertura (mappers, use cases, policies)

---

### 07. Conclusiones

Reflexiones finales, aprendizajes y trabajo futuro.

| Documento | Descripción | Estado |
|-----------|-------------|--------|
| **[Conclusión del Proyecto](../07-conclusion/CONCLUSION_PROYECTO.md)** | Reflexión completa sobre logros, aprendizajes, limitaciones y potencial de impacto social | Completo E2 |

**Temas clave:**
- Logros técnicos alcanzados
- Aprendizajes y desarrollo profesional
- Limitaciones reconocidas
- Viabilidad y potencial de impacto social
- Alineación con objetivos académicos

---

## Inicio Rápido por Audiencia

### Para Desarrolladores
1. **[Guía de Instalación](./03-desarrollo/GUIA_INSTALACION.md)** - Setup del entorno local
2. **[Documentación Técnica](../02-arquitectura/DOCUMENTACION_TECNICA.md)** - Arquitectura y diseño
3. **[Stack Tecnológico](./03-desarrollo/STACK_TECNOLOGICO.md)** - Tecnologías utilizadas

### Para el Tribunal Académico
1. **[Acta de Constitución](../01-gestion/ACTA_CONSTITUCION.md)** - Marco general del proyecto
2. **[Anexo 1](../01-gestion/ANEXO_1_PROYECTO.md)** - Justificación, objetivos detallados, KPIs
3. **[Documentación Técnica](../02-arquitectura/DOCUMENTACION_TECNICA.md)** - Diseño e implementación
4. **[Conclusión](../07-conclusion/CONCLUSION_PROYECTO.md)** - Reflexión final

### Para Usuarios Finales
1. **[Manual de Usuario](./04-usuario/MANUAL_USUARIO.md)** - Cómo usar el sistema (Disponible en E2)

---

## Estado de la Documentación

| Sección | E1 (20/12/2025) | E2 (20/02/2026) |
|---------|-----------------|-----------------|
| **01. Gestión** | Completo | Completo |
| **02. Arquitectura** | Completo | Completo |
| **03. Desarrollo** | Completo | Completo |
| **04. Usuario** | Pendiente | Completo |
| **05. Deployment** | Parcial | Completo |
| **06. Calidad** | Completo | Completo |
| **07. Conclusión** | Pendiente | Completo |

**Leyenda:**  
Completo = Documentación finalizada y revisada  
Parcial = Versión básica disponible, requiere ampliación  
Pendiente = Programado para entrega posterior

---

## Enlaces Útiles

- **[API Documentation (Swagger)](http://localhost:8080/swagger-ui.html)** - Documentación interactiva de APIs
- **[Repositorio del Proyecto](#)** - Código fuente en GitHub
- **[Sistema en Producción](#)** - Demo en vivo (Disponible tras E1)

---

## Convenciones de la Documentación

- Los archivos en **MAYÚSCULAS.md** son documentos formales/oficiales
- Los archivos en **minusculas.md** son guías técnicas
- Las secciones marcadas como "Pendiente" estarán disponibles en la Entrega 2
- Las secciones marcadas como "Parcial" están en progreso o versión básica

---

## Contacto y Soporte

**Autor:** Juan Pablo Verdondoni  
**Institución:** Universidad Tecnológica Nacional (UTN)  
**Email:** [tu-email]  
**LinkedIn:** [tu-linkedin]

---

**Última actualización:** Diciembre 2025  
**Versión de la documentación:** 1.1.0
# Conclusión del Proyecto Find Me

El presente trabajo tuvo como objetivo desarrollar un sistema web funcional para la gestión y seguimiento de personas desaparecidas en Argentina, centralizando información dispersa y facilitando el reporte ciudadano de avistamientos georreferenciados. Este problema social, que afecta a aproximadamente 10.000 familias argentinas por año, requiere soluciones tecnológicas que mejoren la ventana temporal crítica de búsqueda (las primeras 24-48 horas posteriores a la desaparición), período en el cual, según estudios internacionales, se resuelven entre el 80 % y el 90 % de los casos.

---

## Logros técnicos alcanzados

### Arquitectura y calidad de código

Desde el punto de vista técnico, el proyecto alcanzó los objetivos planteados en múltiples dimensiones, superando en algunos aspectos las expectativas iniciales.

Se implementó una arquitectura limpia basada en principios SOLID y en el patrón de puertos y adaptadores (hexagonal architecture). La separación rigurosa en capas —dominio, aplicación, infraestructura y presentación— permitió mantener el código desacoplado, altamente testeable y extensible. Esta decisión arquitectónica, si bien incrementó la complejidad inicial del proyecto, especialmente en un contexto académico con restricciones de tiempo, resultó fundamental para gestionar la creciente complejidad funcional sin acumular deuda técnica significativa.

El sistema de validación mediante Rules y Policies demostró ser altamente efectivo para gestionar reglas de negocio complejas de forma modular. La implementación del patrón Chain of Responsibility permitió agregar nuevas validaciones sin modificar código existente, cumpliendo rigurosamente el principio Open/Closed. La estrategia de normalización de datos (mayúsculas, eliminación de acentos, limpieza de DNI) implementada en los mappers garantizó consistencia en toda la plataforma.

La cobertura de pruebas alcanzó aproximadamente el 85 % en el backend, con cobertura del 100 % en componentes críticos del dominio (mappers, casos de uso, políticas, normalizadores). Este nivel de testing, si bien demandó una inversión considerable de tiempo, proporcionó confianza para realizar refactorizaciones y agregar funcionalidades sin temor a regresiones. La integración de pruebas automatizadas en el flujo de desarrollo validó la efectividad del enfoque TDD parcial adoptado en las capas más críticas del sistema.

### Capacidades geoespaciales avanzadas

La integración de PostgreSQL con la extensión PostGIS constituyó el núcleo técnico diferenciador del sistema. Se implementaron consultas espaciales sofisticadas:

- **Búsqueda por radio**: utilizando `ST_DWithin` con cast a geography para obtener distancias en metros reales, no en grados decimales.
- **Búsqueda por área rectangular (bounding box)**: mediante `ST_Within` con geometrías construidas dinámicamente.
- **Búsqueda por polígono arbitrario**: aceptando Well-Known Text (WKT) y convirtiendo a geometrías PostGIS.
- **K vecinos más cercanos (KNN)**: utilizando el operador `<->` optimizado por índices GIST.

Los índices espaciales GIST sobre la columna `ubicacion` demostraron su efectividad, manteniendo tiempos de respuesta por debajo de los 200 ms incluso con volúmenes de datos considerables para un entorno académico. La decisión de utilizar SRID 4326 (WGS84) facilitó la integración directa con Leaflet.js y otros sistemas geoespaciales estándar.

La sincronización automática entre las columnas `latitud`, `longitud` y el campo geometry `ubicacion` mediante triggers de base de datos eliminó inconsistencias potenciales y simplificó la lógica de aplicación. Esta decisión de delegar responsabilidad a la base de datos, aunque menos común en arquitecturas modernas orientadas a servicios, resultó pragmática y eficiente para el caso de uso específico.

### Seguridad y autenticación

El mecanismo de autenticación basado en JWT almacenado en cookies HttpOnly, combinado con la validación contra un padrón nacional simulado (RENAPER), ofrece un balance adecuado entre seguridad y usabilidad para un prototipo académico. La decisión de no utilizar contraseñas tradicionales —validando directamente contra el padrón— simplificó significativamente el flujo de registro y eliminó riesgos asociados al almacenamiento y gestión de credenciales.

Las protecciones implementadas cubren los vectores de ataque más comunes según el OWASP Top 10:

- **XSS**: mitigado mediante cookies HttpOnly que no son accesibles desde JavaScript.
- **CSRF**: atenuado mediante el uso de `SameSite=Lax` en las cookies.
- **SQL Injection**: eliminado mediante el uso consistente de JPA y consultas parametrizadas.
- **Mass Assignment**: prevenido mediante la separación estricta de DTOs de request y response.

Si bien existen áreas de mejora identificadas (rate limiting, auditoría detallada, autenticación multifactor), las protecciones implementadas son apropiadas para un sistema en fase de prototipo académico y establecen bases sólidas para un eventual endurecimiento de seguridad previo a un despliegue productivo real.

### Frontend y experiencia de usuario

La decisión de utilizar JavaScript vanilla con ES6 modules en lugar de frameworks SPA modernos (React, Vue, Angular) resultó adecuada para el alcance del proyecto. Si bien esta elección limitó ciertas capacidades de reactividad automática, proporcionó control total sobre el comportamiento del sistema, redujo drásticamente la complejidad de configuración y build, y facilitó el mantenimiento por parte de desarrolladores con conocimientos básicos de JavaScript.

La integración con Leaflet.js para la renderización de mapas interactivos demostró ser una elección sólida. La biblioteca es madura, bien documentada y no requiere API keys comerciales (a diferencia de Google Maps). La implementación del sistema de colores diferenciados para marcadores según la persona desaparecida, combinado con la distinción visual entre avistamientos verificados y no verificados, mejoró significativamente la legibilidad del mapa.

El enfoque responsive implementado garantiza usabilidad desde dispositivos móviles de 320 px de ancho hasta pantallas de escritorio, cumpliendo con el requisito crítico de accesibilidad móvil dado que la mayoría de los usuarios potenciales accederán desde smartphones. Los formularios fueron optimizados para interacción táctil, minimizando la fricción en la carga de datos.

---

## Aprendizajes y desarrollo profesional

### Aplicación práctica de patrones arquitectónicos

El desarrollo de Find Me representó una oportunidad significativa de crecimiento profesional que trascendió la mera adquisición de conocimientos técnicos puntuales.

La implementación de Clean Architecture, que previamente solo se conocía en teoría, permitió comprender en profundidad los beneficios reales de la separación de responsabilidades en términos de mantenibilidad y testabilidad. La experiencia práctica de enfrentar la tentación constante de "cortar camino" violando las capas —particularmente bajo presión de plazos— fortaleció la disciplina de diseño.

El uso del patrón de puertos y adaptadores (hexagonal architecture) demostró su valor al facilitar el testing mediante mocks de interfaces. La posibilidad de testear casos de uso sin necesidad de levantar la base de datos o el framework web aceleró significativamente los ciclos de desarrollo y debugging. Esta experiencia validó empíricamente lo que la literatura técnica afirma: la inversión inicial en buena arquitectura se amortiza rápidamente cuando el proyecto crece en complejidad.

### Dominio de tecnologías geoespaciales

El trabajo con tecnologías geoespaciales (PostGIS, JTS Topology Suite, Leaflet) constituyó el aprendizaje técnico más desafiante y enriquecedor del proyecto. La curva de aprendizaje inicial fue pronunciada: comprender sistemas de referencia de coordenadas (CRS), diferencias entre geometry y geography, índices espaciales GIST, y las sutilezas de las funciones espaciales de PostGIS requirió una inversión considerable de tiempo en documentación y experimentación.

Particularmente complejo resultó dominar la diferencia fundamental entre:

- **Geometry**: cálculos en plano cartesiano (rápidos pero inexactos en distancias largas).
- **Geography**: cálculos sobre el esferoide (precisos pero más costosos computacionalmente).

La decisión de usar geography para cálculos de distancia (búsqueda por radio) y geometry para consultas de contención (polígonos) equilibró precisión y performance de manera pragmática.

El manejo de la diferencia de orden de coordenadas entre PostGIS (longitud, latitud) y Leaflet (latitud, longitud) fue fuente de múltiples errores sutiles durante el desarrollo, reforzando la importancia de la documentación detallada y los tests de integración para validar transformaciones de datos entre sistemas.

### Integración full-stack y gestión de complejidad

La experiencia de integrar múltiples tecnologías (Spring Boot, PostgreSQL, JavaScript modular, JWT) en un sistema cohesivo fortaleció las competencias de desarrollo full-stack. Particularmente valiosa fue la experiencia de gestionar la complejidad emergente: cada decisión técnica individual parecía simple, pero su interacción con otras partes del sistema generaba efectos no anticipados que requerían análisis cuidadoso y, en algunos casos, refactorizaciones localizadas.

El proyecto evidenció la importancia crítica de mantener documentación técnica actualizada. En múltiples ocasiones, decisiones de diseño tomadas semanas atrás requirieron revisión para entender su justificación original. La disciplina de documentar decisiones arquitectónicas (ADRs implícitos en la documentación técnica) demostró ser invaluable para mantener coherencia a lo largo del desarrollo.

### Gestión de tiempo y priorización

El constraint de 150 horas máximas obligó a desarrollar habilidades de priorización rigurosa. La clasificación MoSCoW (Must/Should/Could/Won't) no fue un ejercicio teórico sino una herramienta práctica de toma de decisiones semanales. La experiencia de tener que posponer funcionalidades deseables (Could Have) para garantizar la solidez de las funcionalidades críticas (Must Have) reflejó fielmente la realidad de los proyectos profesionales con restricciones de recursos.

Particularmente instructivo fue el ejercicio de balancear calidad interna (arquitectura, testing, documentación) versus funcionalidades visibles. La tentación de agregar features "cool" en detrimento de la cobertura de tests o la documentación requirió disciplina consciente. La validación posterior —cuando refactorizaciones y debugging se simplificaron gracias a los tests— confirmó que la inversión en calidad interna no fue tiempo perdido sino aceleración diferida.

---

## Limitaciones reconocidas y lecciones aprendidas

Es fundamental reconocer las limitaciones inherentes a un proyecto académico con restricciones de tiempo y alcance, así como las decisiones de diseño que, con retrospectiva, podrían mejorarse en una iteración futura. Todas las limitaciones identificadas deben interpretarse dentro del marco de un trabajo final de carrera con objetivos formativos claros y un presupuesto de horas acotado. En este contexto, muchas decisiones de postergar determinadas funcionalidades o endurecimientos de seguridad no representan fallas de diseño, sino elecciones conscientes de priorización orientadas a maximizar el valor técnico y social del núcleo del sistema.

### Limitaciones técnicas

La ausencia de paginación en los listados de personas desaparecidas y avistamientos es la limitación técnica más evidente. Si bien el sistema maneja adecuadamente volúmenes de datos típicos de un entorno de prueba (cientos de registros), un despliegue real con miles o decenas de miles de casos requeriría imperiosamente implementar paginación server-side. La decisión de postergar esta funcionalidad fue pragmática dado el alcance académico, pero representa la primera prioridad en una eventual fase de producción.

La falta de un sistema de caché (Redis, Caffeine o similar) limita la escalabilidad del sistema. Consultas frecuentes —como el listado de todos los avistamientos públicos para el mapa— se ejecutan contra la base de datos en cada request. Para volúmenes moderados esto es aceptable, pero constituye un cuello de botella identificado para un escenario de alto tráfico.

El logging del sistema, si bien funcional, es relativamente básico. No se implementó logging estructurado (JSON), correlación de requests mediante trace IDs, ni niveles de log diferenciados de forma granular. Esto dificultaría el debugging en producción y el análisis de incidentes. La integración con herramientas de observabilidad (Prometheus, Grafana, ELK stack) quedó fuera del alcance pero sería esencial para un entorno productivo.

La falta de versionado explícito de la API (`/v1/`, `/v2/`) fue una decisión consciente para simplificar el MVP, pero limita la capacidad de evolucionar la API sin romper clientes existentes. En retrospectiva, agregar el prefijo `/v1/` desde el inicio habría sido prudente incluso en un proyecto académico, estableciendo desde el principio buenas prácticas de diseño de APIs.

### Limitaciones funcionales

La integración con el padrón RENAPER quedó simulada mediante una base de datos local con datos ficticios. Si bien esto fue suficiente para validar el concepto y la arquitectura de validación, la ausencia de integración real limita el potencial del sistema. Una integración verdadera requeriría acuerdos institucionales, gestión de credenciales de acceso a servicios gubernamentales y manejo de casos edge (servicios caídos, timeouts, datos desactualizados) que no se exploraron en profundidad.

El sistema de moderación de contenido es manual y básico. No se implementó machine learning para detección automática de contenido inapropiado, ni sistemas de reputación de usuarios, ni moderación colaborativa estilo Reddit/Stack Overflow. En un despliegue real con volumen significativo de reportes, la moderación manual escalaría pobremente. Este es un área donde la inversión en ML/IA aportaría valor tangible pero que excedía claramente el alcance académico.

Las notificaciones en tiempo real mediante WebSockets o Server-Sent Events no se implementaron. El sistema actual requiere refresh manual para ver nuevos avistamientos. Para usuarios que están monitoreando activamente un caso —familiares de personas desaparecidas— esta fricción es significativa. La implementación de notificaciones push habría mejorado sustancialmente la experiencia, pero implicaba complejidad adicional en infraestructura y gestión de conexiones persistentes.

### Limitaciones de seguridad

La ausencia de rate limiting es la brecha de seguridad más significativa reconocida. El sistema es vulnerable a ataques de fuerza bruta contra el endpoint de autenticación, spam de reportes de avistamientos y ataques de denegación de servicio mediante requests masivos. La implementación de rate limiting a nivel de aplicación (Spring Boot con Bucket4j) o a nivel de infraestructura (NGINX rate limiting, Cloudflare) sería prioritaria antes de un despliegue público.

La auditoría de accesos es limitada. Se registran operaciones básicas pero no existe un audit trail completo que permita reconstruir "quién hizo qué, cuándo" de forma exhaustiva. Para un sistema que maneja datos sensibles (información de personas desaparecidas, datos personales de reportantes), un sistema robusto de auditoría sería requisito regulatorio en muchas jurisdicciones.

El secreto JWT está almacenado en el archivo `application.properties`, lo cual es inaceptable en producción. Si bien es práctica común en entornos de desarrollo, la producción requiere gestión segura de secretos mediante variables de entorno, servicios especializados (HashiCorp Vault, AWS Secrets Manager) o al menos configuración externalizada cifrada. Esta es una de las primeras correcciones necesarias previo a cualquier despliegue real.

No se implementó autenticación de múltiples factores (2FA), ni mecanismos de revocación anticipada de tokens JWT, ni rotación periódica de secretos. Estas son prácticas estándar en aplicaciones que manejan datos sensibles y deberían incorporarse en una fase de endurecimiento de seguridad.

### Decisiones de diseño cuestionables en retrospectiva

La decisión de sincronizar `latitud`, `longitud` y `ubicacion` mediante triggers de base de datos, si bien funcionó correctamente, genera acoplamiento entre el esquema de datos y la lógica del sistema. En retrospectiva, manejar esta sincronización en la capa de aplicación (JPA entity listeners o servicios de dominio) habría estado más alineado con los principios de Clean Architecture, facilitando migraciones futuras o cambios de base de datos.

La ausencia de una capa explícita de anti-corruption entre las entidades JPA y el modelo de dominio significa que Hibernate "sangra" hacia el dominio más de lo deseable. Si bien se utilizaron mappers para transformar entre capas, las entidades JPA son también las entidades de dominio. Una arquitectura más pura habría separado completamente el modelo de persistencia del modelo de dominio, con el costo de código adicional de mapeo.

El manejo de imágenes mediante URLs externas (Cloudinary) fue pragmático pero introduce dependencias externas críticas. Un sistema más robusto podría implementar almacenamiento local con CDN opcional, o al menos una abstracción del proveedor de storage para facilitar migraciones. La decisión actual acopla el sistema a Cloudinary de forma no trivial.

---

## Viabilidad y potencial de impacto social

### Arquitectura como fundamento para el crecimiento

Desde una perspectiva práctica, Find Me demuestra la viabilidad técnica de desarrollar una plataforma de este tipo con tecnologías open-source y sin dependencia de servicios comerciales costosos. El stack tecnológico elegido (Spring Boot, PostgreSQL/PostGIS, Leaflet) es ampliamente conocido, bien documentado y cuenta con comunidades activas, lo cual facilita tanto el mantenimiento como la incorporación de nuevos desarrolladores en caso de que el proyecto evolucionara hacia una iniciativa de código abierto o institucional.

Si bien el sistema se encuentra en estado de prototipo académico, su arquitectura modular facilita la incorporación de funcionalidades adicionales. Los puertos y adaptadores permiten agregar nuevos canales de entrada (por ejemplo, una API GraphQL o una aplicación móvil nativa) sin modificar el núcleo de negocio. La separación en casos de uso siguiendo CQRS simple permite optimizar independientemente las operaciones de lectura y escritura si el volumen lo requiriera en el futuro.

La decisión de estructurar el código en capas bien diferenciadas reduce drásticamente el esfuerzo necesario para evolucionar el proyecto hacia una solución productiva. Agregar funcionalidades como:

- Notificaciones push mediante WebSockets
- Análisis de patrones de avistamientos mediante ML
- Integración con redes sociales para amplificar alcance
- Módulos de moderación automática con IA

...resulta técnicamente viable sin requerir refactorizaciones mayores de la base existente. Esta es quizás la validación más importante del tiempo invertido en arquitectura: el sistema está genuinamente preparado para crecer.

### Potencial de adopción institucional

El código fuente, la documentación técnica exhaustiva y las decisiones de diseño explícitamente justificadas quedan disponibles como base para futuros desarrollos, ya sea en contextos institucionales (ONGs, organismos públicos de seguridad) o como proyectos de código abierto. La documentación de más de 90 páginas no es un ejercicio meramente académico, sino una herramienta práctica para que un equipo nuevo pueda comprender el sistema, sus limitaciones y las razones detrás de cada decisión arquitectónica.

La problemática de las personas desaparecidas es global y urgente. Organizaciones como Missing Children Argentina, la línea 142 y sistemas como SIFEBU operan con recursos limitados y enfrentan el desafío constante de centralizar información dispersa en múltiples canales. Contar con una base técnica sólida y replicable —disponible como código abierto— puede acelerar la implementación de soluciones en distintas jurisdicciones.

El diseño de endpoints preparados para una integración futura con SIFEBU, si bien no se materializó en integraciones reales, demuestra visión de escalabilidad institucional. En eventuales conversaciones con entidades gubernamentales, contar con una API RESTful bien documentada, con autenticación robusta y que sigue estándares de la industria, facilita significativamente las discusiones técnicas sobre integración.

### Realismo sobre las barreras de adopción

Es importante no idealizar las posibilidades de adopción. La experiencia de proyectos similares muestra que las barreras principales para la adopción de tecnología en el sector social no son técnicas sino organizacionales, de confianza y de sostenibilidad de recursos.

- **Barreras organizacionales**: las instituciones establecidas (gubernamentales y ONGs) tienen procesos, sistemas legacy y dinámicas de poder que resisten cambios tecnológicos, especialmente cuando provienen de iniciativas externas.
- **Barreras de confianza**: un sistema que maneja datos sensibles de personas desaparecidas requiere niveles de confianza que solo se construyen en el tiempo. El código abierto ayuda (permite auditoría), pero no elimina preocupaciones legítimas sobre privacidad, seguridad de datos y responsabilidad legal.
- **Barreras de sostenibilidad**: el proyecto actual funciona en tiers gratuitos de servicios cloud, adecuados para un prototipo pero insuficientes para operación productiva. La adopción institucional requiere definir un modelo de financiamiento para cubrir costos de infraestructura, mantenimiento y evolución del sistema.

A pesar de estas barreras reales, el proyecto demuestra que la viabilidad técnica no es el obstáculo central. La arquitectura está madura, el stack es apropiado y las funcionalidades core están implementadas. Los desafíos restantes son de gestión, adopción social y sostenibilidad.

---

## Alineación con objetivos académicos

En el contexto académico de la carrera de Ingeniería en Sistemas de Información de la UTN, este trabajo final integró efectivamente conceptos fundamentales de múltiples áreas del currículo:

- **Análisis y diseño de sistemas**: aplicación rigurosa de metodologías de análisis de requisitos, modelado de dominio, diseño de arquitectura y especificación de casos de uso.
- **Patrones y arquitectura de software**: implementación de Clean Architecture, ports & adapters, CQRS simple, Repository pattern, Chain of Responsibility (para validaciones) y otros patrones, aplicados de manera pragmática y no meramente teórica.
- **Bases de datos**: uso de PostgreSQL con PostGIS, diseño de modelo entidad-relación, optimización mediante índices espaciales, comprensión de planes de ejecución y gestión de integridad referencial.
- **Testing y calidad**: estrategia de testing con cobertura del 85 %, incluyendo tests unitarios con mocks, tests de integración y validación mediante UAT con usuarios reales.
- **Seguridad informática**: implementación de autenticación JWT, manejo de cookies seguras, protección contra XSS/CSRF/SQL Injection y documentación de limitaciones de seguridad.
- **Gestión de proyectos**: planificación (diagramas de Gantt, identificación de ruta crítica), gestión de riesgos, priorización MoSCoW y control de tiempos con un presupuesto fijo de 150 horas.
- **Desarrollo full-stack**: integración de backend (Java/Spring Boot), frontend (JavaScript/HTML/CSS), base de datos espacial (PostgreSQL/PostGIS), servicios externos (Cloudinary) y despliegue en cloud.

La aplicación de metodologías ágiles (desarrollo iterativo en sprints, integración continua de pruebas, retrospectivas implícitas documentadas como "lecciones aprendidas") y la documentación técnica rigurosa reflejan las competencias esperadas de un ingeniero en sistemas de información al momento de graduarse.

---

## Reflexión final

El desarrollo de Find Me confirmó empíricamente que la tecnología, cuando se aplica con un propósito social claro y con rigor técnico, puede convertirse en una herramienta de impacto potencialmente significativo. Más allá de los conocimientos técnicos adquiridos —que son sustanciales y variados—, este proyecto reforzó convicciones profesionales fundamentales:

- La arquitectura importa y su costo siempre se paga, tarde o temprano.
- El testing no es un lujo, sino una inversión.
- La documentación no es un apéndice, sino el código que leen las personas.
- El equilibrio entre perfección técnica y pragmatismo es una competencia central del ingeniero.

Sin embargo, quizás el aprendizaje más importante es otro: Find Me no es solo una solución tecnológica, ni una demostración de patrones, índices espaciales o buenas prácticas de seguridad. Es un sistema pensado, diseñado y construido por una persona concreta, con nombre y apellido, que eligió dedicar tiempo, energía y conocimiento para intentar aliviar —aunque sea parcialmente— la experiencia de otras personas concretas: familias que buscan a alguien que falta.

Las familias de personas desaparecidas no evalúan la elegancia de la arquitectura hexagonal ni el nivel de cobertura de tests; lo que necesitan son herramientas que acerquen información, que organicen el caos, que sumen ojos en la calle y que hagan un poco menos solitaria la búsqueda. Este proyecto buscó, dentro de sus posibilidades académicas, estar a la altura de esa necesidad: ofrecer una base técnica seria al servicio de una problemática profundamente humana.

Find Me no es únicamente un sistema funcional de gestión y seguimiento de personas desaparecidas. Es una demostración concreta de que, aun en un contexto académico y con recursos limitados, es posible diseñar y construir soluciones tecnológicas con rigor ingenieril, conciencia ética y sensibilidad social. La experiencia adquirida —tanto técnica como humana— trasciende este proyecto específico y sienta bases sólidas para el ejercicio profesional futuro.

Si este trabajo logra, aunque sea mínimamente, inspirar a otros desarrolladores a aplicar el mismo nivel de disciplina, reflexión y compromiso con las personas en el centro —ya sea en esta problemática u otras—, entonces su aporte habrá superado con creces los objetivos formales planteados al inicio. En definitiva, Find Me es la prueba de que la ingeniería de software puede y debe estar al servicio de la gente, y no solo de la tecnología.
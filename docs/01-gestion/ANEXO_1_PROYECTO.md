# ANEXO
### Proyecto FIND ME – Sistema de Avistamiento de Personas Desaparecidas

> Este anexo complementa el Acta de Constitución del Proyecto Find Me.  
> Aquí se desarrollan en detalle la justificación del proyecto, los objetivos específicos, el alcance funcional por entrega, los criterios de éxito y aceptación, así como el modelo de datos preliminar, el stack tecnológico, los entregables, entre otro.

---

## ÍNDICE DEL ANEXO 1

1. [JUSTIFICACIÓN DEL PROYECTO FIND ME](#justificación-del-proyecto-find-me)
    - [El problema que no vemos](#el-problema-que-no-vemos)
    - [Marco normativo vs. realidad: la brecha que cuesta vidas](#marco-normativo-vs-realidad-la-brecha-que-cuesta-vidas)
    - [La ventana crítica: cuando cada hora cuenta](#la-ventana-crítica-cuando-cada-hora-cuenta)
    - [El ecosistema actual: sólido pero incompleto](#el-ecosistema-actual-sólido-pero-incompleto)
    - [Find Me: el eslabón que falta](#find-me-el-eslabón-que-falta)
    - [Valor diferencial en tres dimensiones](#valor-diferencial-en-tres-dimensiones)
    - [Alcance más allá de los casos críticos](#alcance-más-allá-de-los-casos-críticos)
    - [El costo de no actuar](#el-costo-de-no-actuar)
    - [Llamado a la acción](#llamado-a-la-acción)
    - [Fuentes y referencias](#fuentes-y-referencias)

2. [OBJETIVO GENERAL (REFERENCIA)](#objetivo-general-referencia)

3. [OBJETIVOS ESPECÍFICOS DETALLADOS](#objetivos-específicos-detallados)
    - [3.1 Objetivos Técnicos – Infraestructura y Arquitectura](#31-objetivos-técnicos--infraestructura-y-arquitectura)
    - [3.2 Objetivos Funcionales – Features Core del Sistema](#32-objetivos-funcionales--features-core-del-sistema)
    - [3.3 Objetivos de Integración y Datos](#33-objetivos-de-integración-y-datos)
    - [3.4 Objetivos de Calidad y Experiencia de Usuario](#34-objetivos-de-calidad-y-experiencia-de-usuario)
    - [3.5 Objetivos de Validación con Usuarios](#35-objetivos-de-validación-con-usuarios)
    - [3.6 Objetivos de Gestión del Proyecto](#36-objetivos-de-gestión-del-proyecto)
    - [3.7 Matriz de Objetivos SMART (Resumida)](#37-matriz-de-objetivos-smart-resumida)
    - [3.8 Priorización de Objetivos (MoSCoW)](#38-priorización-de-objetivos-moscow)

4. [ALCANCE DETALLADO POR ENTREGA](#alcance-detallado-por-entrega)
    - [4.1 Alcance Detallado - ENTREGA 1 (Diciembre 2025) – MVP Técnico](#41-alcance-detallado---entrega-1-diciembre-2025--mvp-técnico)
    - [4.2 Alcance Detallado - ENTREGA 2 (Febrero 2026) – Sistema Completo](#42-alcance-detallado---entrega-2-febrero-2026--sistema-completo)
    - [4.3 Fuera del Alcance (Ambas Entregas)](#43-fuera-del-alcance-ambas-entregas)

5. [CRITERIOS DE ÉXITO, KPIS Y CRITERIOS DE ACEPTACIÓN](#criterios-de-éxito-kpis-y-criterios-de-aceptación)
    - [5.1 KPIs Técnicos](#51-kpis-técnicos)
    - [5.2 KPIs Funcionales](#52-kpis-funcionales)
    - [5.3 KPIs de Proyecto](#53-kpis-de-proyecto)
    - [5.4 Criterios de Aceptación - ENTREGA 1 (Diciembre 2025)](#54-criterios-de-aceptación---entrega-1-diciembre-2025)
    - [5.5 Criterios de Aceptación - ENTREGA 2 (Febrero 2026)](#55-criterios-de-aceptación---entrega-2-febrero-2026)

6. [RIESGOS DETALLADOS](#riesgos-detallados)

7. [MODELO DE DATOS PRELIMINAR](#modelo-de-datos-preliminar)
    - [7.1 Tablas Principales](#71-tablas-principales)

8. [STACK TECNOLÓGICO COMPLETO](#stack-tecnológico-completo)
    - [8.1 Backend](#81-backend)
    - [8.2 Frontend](#82-frontend)
    - [8.3 DevOps y Herramientas](#83-devops-y-herramientas)

9. [ENTREGABLES DEL PROYECTO](#entregables-del-proyecto)
    - [9.1 Clasificación de Entregables](#91-clasificación-de-entregables)
    - [9.2 Entregables - Entrega 1 (MVP Técnico)](#92-entregables---entrega-1-mvp-técnico)
    - [9.3 Entregables - Entrega 2 (Sistema Completo)](#93-entregables---entrega-2-sistema-completo)
    - [9.4 Resumen de Entregables por Tipo](#94-resumen-de-entregables-por-tipo)
    - [9.5 Total de Entregables](#95-total-de-entregables)
    - [9.6 Matriz de Dependencias entre Entregables](#96-matriz-de-dependencias-entre-entregables)
    - [9.7 Criterios de Calidad Transversales](#97-criterios-de-calidad-transversales)

10. [BENEFICIOS ESPERADOS](#beneficios-esperados)
    - [10.1 Beneficios Cuantitativos (Medibles)](#101-beneficios-cuantitativos-medibles)
    - [10.2 Beneficios Cualitativos (Intangibles)](#102-beneficios-cualitativos-intangibles)
    - [10.3 Beneficios por Stakeholder](#103-beneficios-por-stakeholder)
    - [10.4 Impacto Social Estimado](#104-impacto-social-estimado)
    - [10.5 Beneficios Adicionales No Cuantificados](#105-beneficios-adicionales-no-cuantificados)
    - [10.6 Condiciones y Limitaciones de los Beneficios](#106-condiciones-y-limitaciones-de-los-beneficios)
    - [10.7 Resumen Ejecutivo de Beneficios](#107-resumen-ejecutivo-de-beneficios)

11. [HITOS DEL PROYECTO](#hitos-del-proyecto)
    - [11.1 Definición y Función de los Hitos](#111-definición-y-función-de-los-hitos)
    - [11.2 Tabla Completa de Hitos del Proyecto](#112-tabla-completa-de-hitos-del-proyecto)
    - [11.3 Descripción Detallada de Cada Hito](#113-descripción-detallada-de-cada-hito)
    - [11.4 Ruta Crítica del Proyecto](#114-ruta-crítica-del-proyecto)
    - [11.5 Relación entre Hitos y Entregables](#115-relación-entre-hitos-y-entregables)
    - [11.6 Relación entre Hitos y Objetivos Específicos](#116-relación-entre-hitos-y-objetivos-específicos)
    - [11.7 Estrategia de Seguimiento de Hitos](#117-estrategia-de-seguimiento-de-hitos)
    - [11.8 Resumen Ejecutivo de Hitos](#118-resumen-ejecutivo-de-hitos)

---

## 1. JUSTIFICACIÓN DEL PROYECTO FIND ME

### El problema que no vemos

El incendio de Cromañón, ocurrido el 30 de diciembre de 2004, dejó expuesta una carencia crítica del sistema de respuesta ante emergencias masivas. Más de 3.000 personas se encontraban dentro del local cuando comenzó el fuego y, en los días posteriores, cientos de familias recorrieron hospitales, comisarías y morgues buscando a sus hijos y allegados. No existía un sistema centralizado donde reportar avistamientos o verificar si alguien había sido identificado. La información circulaba en listas manuscritas, planillas parciales, llamadas telefónicas y fotocopias pegadas en el espacio público, con demoras significativas en la actualización de datos. Este ejemplo extremo ilustra un problema que, en menor escala, persiste hasta hoy: la ausencia de herramientas tecnológicas que permitan concentrar y estructurar, en tiempo real, información sobre personas desaparecidas.

Veinte años después, en un país donde cada día desaparecen aproximadamente 27 personas según datos del Sistema Federal de Búsqueda (SIFEBU), seguimos sin contar con una herramienta ciudadana que permita canalizar avistamientos de forma inmediata, estructurada y geolocalizada. En Argentina se registran alrededor de 10.000 casos de personas desaparecidas por año, y esto representa solo lo efectivamente denunciado; la Defensa Pública reconoce que "no existe un registro oficial completo que refleje el número total de personas desaparecidas y denuncias". Si se considera únicamente a niñas, niños y adolescentes, se estima que desaparecen entre 4 y 5 menores por día, lo que equivale a entre 1.200 y 1.800 casos anuales de menores en situación de búsqueda activa.

En la Ciudad de Buenos Aires, el Registro de Chicos Perdidos reporta aproximadamente 1.125 casos por año, lo que implica alrededor de 3 casos diarios de adolescentes extraviados solo en CABA. En la Provincia de Buenos Aires, durante el primer semestre de 2025 se relevaron 2.803 casos, lo que proyectado a un año completo representa aproximadamente 5.600 casos anuales, es decir, unos 15 casos por día solo en PBA.

Estos números no son meras estadísticas: representan familias reales, horas de incertidumbre y oportunidades de hallazgo que se pierden por falta de información centralizada y estructurada en el momento más crítico.

---

### Marco normativo vs. realidad: la brecha que cuesta vidas

En la actualidad, el marco normativo argentino en materia de desaparición de personas es claro respecto de la inmediatez con la que debe actuarse. El Ministerio de Seguridad de la Nación establece expresamente que la denuncia debe realizarse de forma inmediata y que no es necesario esperar 24 ni 48 horas para radicarla. En la misma línea, la Defensoría del Pueblo, en sus protocolos específicos sobre niñas, niños y adolescentes extraviados, afirma que tampoco corresponde imponer un plazo de espera previo a la denuncia. A su vez, los lineamientos y protocolos vinculados a desapariciones de mujeres y posibles casos de trata de personas enfatizan que no existe obligación de espera alguna, justamente porque las primeras horas de búsqueda son fundamentales.

En consecuencia, desde el punto de vista jurídico y normativo, puede afirmarse que en Argentina no existe un lapso mínimo obligatorio antes de poder realizar una denuncia por desaparición. Sin embargo, en la práctica persiste el denominado "mito de las 24 horas", que se traduce tanto en representaciones sociales como, en algunos casos, en prácticas policiales que desalientan o demoran la toma de la denuncia. El propio SIFEBU (Sistema Federal de Búsqueda de Personas Desaparecidas y Extraviadas) alude explícitamente a la necesidad de erradicar este mito como una práctica antigua que todavía se intenta desterrar. Esta brecha entre el marco normativo y las prácticas efectivas tiene un impacto directo en la eficacia de las búsquedas, ya que implica la pérdida de horas críticas.

---

### La ventana crítica: cuando cada hora cuenta

La relevancia de este problema se vuelve aún más evidente cuando se consideran los datos provenientes de estudios internacionales, que muestran que las primeras 24 a 48 horas tras la desaparición de una persona son decisivas. Diversos análisis estadísticos del National Crime Agency del Reino Unido y estudios académicos europeos indican que entre el 80 % y el 90 % de las personas desaparecidas son encontradas dentro de las primeras 48 horas.

De forma aproximada, estos estudios pueden desglosarse del siguiente modo:

- **Primeras 24 horas:** alrededor del 76 % de los adultos son encontrados.
- **Primeras 48 horas:** el 86–88 % del total de casos se resuelven.
- **Primera semana:** se suma aproximadamente un 5–15 % adicional.
- **Después de 7 días:** solo entre un 2 % y un 5 % de los casos permanece sin resolver o resulta en hallazgo fatal.

Para menores de edad, los números son aún más alentadores: entre el 85 % y el 95 % son encontrados dentro de las primeras 48 horas, especialmente cuando se trata de fugas voluntarias o conflictos familiares. En la Ciudad de Buenos Aires, el Registro de Chicos Perdidos reporta que aproximadamente el 95 % de los adolescentes extraviados son efectivamente hallados.

En este contexto, cada hora de demora no solo reduce la probabilidad de hallazgo en esa ventana crítica, sino que además implica la pérdida de información fresca y potencialmente valiosa: últimos avistamientos, personas que vieron a la persona desaparecida, medios de transporte utilizados, recorridos habituales, descripciones precisas de vestimenta y estado emocional, entre otros datos. Por lo tanto, resulta central contar con herramientas que permitan movilizar información y recursos lo antes posible, en lugar de reproducir pasivamente el mito de la espera.

---

### El ecosistema actual: sólido pero incompleto

Frente a este escenario cabe preguntarse si existe hoy una herramienta centralizada, de uso público, que permita obtener información rápida, verosímil y estructurada para colaborar en la búsqueda de personas. En Argentina existen dispositivos relevantes, pero con alcances y lógicas específicas:

#### SIFEBU (Sistema Federal de Búsqueda de Personas Desaparecidas y Extraviadas)

El SIFEBU constituye el sistema federal del Ministerio de Seguridad: registra personas desaparecidas y NN a nivel nacional, coordina la actuación de fuerzas de seguridad, Poder Judicial y otros organismos, y centraliza denuncias provenientes de SIFCOP y bases provinciales. Es, en términos institucionales, la columna vertebral de la búsqueda de personas en el país.

Sin embargo, se trata de un sistema pensado principalmente "de adentro hacia afuera" (Estado → instituciones), y no ofrece una interfaz ciudadana masiva y geolocalizada para que la población pueda cargar avistamientos en tiempo real. Su diseño responde a necesidades de coordinación interinstitucional, no de participación ciudadana directa.

#### Línea 142 – Registro Nacional de Información de Personas Menores Extraviadas

Este registro cumple un rol esencial para niñas, niños y adolescentes extraviados, ofreciendo líneas telefónicas gratuitas 24/7, información y acompañamiento.

No obstante, su canal principal de interacción es el teléfono o el correo electrónico, lo que implica depender de una conversación oral que luego un operador debe sistematizar, con riesgo de pérdidas o distorsiones de detalle. La información que ingresa como relato telefónico debe ser transcrita manualmente, sin geolocalización automática ni evidencia fotográfica inmediata.

#### Missing Children Argentina

Missing Children Argentina es una ONG con gran capacidad comunicacional, que recibe denuncias, difunde casos en medios y redes sociales y orienta a las familias en el proceso de denuncia y autorización de difusión. Su interacción con la ciudadanía se da principalmente a través de WhatsApp, teléfono, redes sociales y correo electrónico.

El desafío radica en que la información ingresa como mensajes dispersos en múltiples canales: un mensaje por WhatsApp, otro por Instagram, un comentario en Facebook, un llamado telefónico. Esta fragmentación dificulta la construcción de líneas temporales coherentes, mapas de avistamientos y análisis de patrones geográficos.

#### Alerta Sofía

Alerta Sofía opera como sistema de alerta de emergencia rápida para niñas, niños y adolescentes en alto riesgo inminente, coordinando al Ministerio de Seguridad (a través de SIFEBU), el sector privado, los medios de comunicación y la sociedad civil. Su fortaleza radica en la capacidad de difusión masiva: cuando se activa una alerta, la prioridad es máxima y la información se expande rápidamente.

El punto débil se encuentra en el retorno de información: cuando un ciudadano ve al menor, suele llamar o escribir por distintos canales, lo que dificulta estructurar esa información en un mapa coherente de avistamientos con coordenadas, hora exacta y evidencia fotográfica.

---

### Find Me: el eslabón que falta

En este punto se ubica el aporte diferencial de Find Me. Mientras SIFEBU organiza la dimensión institucional, Find Me está diseñado "de afuera hacia adentro", es decir, desde las personas hacia el Estado. Se propone como una aplicación web amigable que permita a cualquier ciudadano:

- Buscar casos activos por nombre, DNI o visualización en un mapa interactivo.
- Registrar un avistamiento con hora automática, ubicación GPS, descripción y fotografía.
- Generar datos estructurados con latitud, longitud, marca temporal y nivel de confianza.
- Construir automáticamente mapas de calor, trayectorias probables y líneas de tiempo por persona.
- Alimentar modelos probabilísticos centrados en las primeras 24–48 horas.

De este modo, Find Me se concentra precisamente en el tramo en el que, según la evidencia internacional, se resuelve entre el 80 % y el 90 % de los casos.

---

### Valor diferencial en tres dimensiones

#### 1. Plataforma única de avistamientos geolocalizados

Find Me ofrece un entorno en el que la ciudadanía puede cargar información estructurada en segundos, especialmente durante las primeras 24–48 horas. A diferencia de una llamada telefónica, que depende de la memoria y la descripción verbal del testigo, Find Me captura:

- **Ubicación exacta**, mediante coordenadas GPS.
- **Marca temporal precisa**, registrada automáticamente.
- **Fotografía** del avistamiento o del lugar.
- **Descripción estructurada**, con campos predefinidos (vestimenta, acompañantes, dirección de desplazamiento, estado emocional aparente, entre otros).

#### 2. Capa de analítica y modelización probabilística

Sobre la base de la evidencia internacional relativa a la ventana crítica de hallazgo, el sistema puede:

- Mostrar en tiempo real cómo evoluciona la probabilidad de hallazgo según el tiempo transcurrido.
- Identificar patrones espaciales y temporales (por ejemplo, varios avistamientos en una misma estación de tren en un intervalo corto).
- Sugerir áreas de búsqueda en función del tiempo transcurrido y la velocidad de desplazamiento esperable.
- Generar alertas automáticas para usuarios cercanos a zonas de alta probabilidad.

#### 3. Hub de integración con sistemas existentes

Find Me está concebido como un hub capaz de integrarse, mediante APIs, con los sistemas ya existentes:

- **Con SIFEBU:** cada caso de Find Me puede vincularse a un ID de SIFEBU y cada avistamiento ciudadano puede ponerse a disposición de las fuerzas de seguridad en tiempo real.
- **Con la línea 142:** cada registro referido a un menor puede generar automáticamente una "ficha digital" que llegue al equipo del Registro Nacional de Información de Personas Menores Extraviadas.
- **Con Missing Children Argentina:** se puede incorporar un botón "Reportar avistamiento" en sus publicaciones en redes sociales que abra Find Me con el caso precargado; los avistamientos recibidos por WhatsApp podrían consolidarse en la plataforma.
- **Con Alerta Sofía:** Find Me puede contribuir a la construcción de un "mapa vivo" de la alerta, indicando zonas de interés y últimos avistamientos, así como canalizando reportes ciudadanos de manera estructurada.

---

### Alcance más allá de los casos críticos

A diferencia de Alerta Sofía, que se reserva para situaciones de alto riesgo, Find Me puede utilizarse también para casos que no alcanzan ese umbral pero que son igualmente críticos para las familias involucradas, tales como:

- Adultos mayores con Alzheimer o demencia que se desorientan.
- Personas con discapacidad cognitiva que se pierden en espacios públicos.
- Mujeres en contexto de violencia de género que desaparecen súbitamente.
- Víctimas de trata, sobre las que existen avistamientos esporádicos en distintos puntos del país.
- Desapariciones en contexto de catástrofes (incendios, inundaciones, accidentes masivos), donde se requieren mecanismos rápidos de cruce de información entre hospitales, testigos y autoridades.

En escenarios como Cromañón o la tragedia de Once, por ejemplo, una herramienta como Find Me podría funcionar como un centro de información unificado, donde hospitales reporten ingresos de personas no identificadas, testigos carguen avistamientos de personas vistas saliendo del lugar, familiares busquen coincidencias con descripciones de personas internadas y fuerzas de seguridad actualicen el estado de la búsqueda en tiempo real.

---

### El costo de no actuar

Cada año, aproximadamente 10.000 familias argentinas atraviesan la experiencia de no saber dónde está un ser querido. Sobre ese universo, y siguiendo la evidencia internacional:

- Entre un 80 % y un 90 % de los casos se resuelven en las primeras 48 horas.
- Un 5–10 % adicional se resuelve durante la primera semana.
- Entre un 2 % y un 5 % permanece sin resolver o tiene desenlaces fatales a largo plazo.

Si, a modo ilustrativo, una herramienta como Find Me contribuyera a mejorar en apenas un 5 % la tasa de resolución en las primeras 48 horas, el impacto potencial sería del orden de cientos de personas adicionales encontradas con vida por año. Del mismo modo, una reducción moderada en la proporción de casos que permanecen sin resolver después de la primera semana podría traducirse en decenas de vidas salvadas anualmente. Se trata de estimaciones hipotéticas, pero permiten dimensionar la magnitud del problema y el alcance posible de una intervención tecnológica focalizada en la ventana temporal más crítica.

---

### Llamado a la acción

En síntesis, Find Me no pretende reemplazar los mecanismos institucionales existentes, sino amplificar su eficacia al habilitar un canal ciudadano estructurado que opere precisamente en la ventana temporal más crítica. Al reducir la fricción para reportar avistamientos y centralizar información geolocalizada, el proyecto puede contribuir directamente a mejorar las tasas de hallazgo en las primeras 48 horas, cuando la evidencia internacional indica que se resuelven alrededor de 8 de cada 10 casos.

Los sistemas nacionales (SIFEBU, línea 142, Missing Children Argentina, Alerta Sofía) son dispositivos fundamentales, pero orientados principalmente desde la lógica institucional, centrados en la denuncia formal, la coordinación interinstitucional y la difusión masiva. Find Me introduce tres aportes que hoy no se encuentran cubiertos de manera integral:

1. **Una plataforma única de avistamientos geolocalizados**, donde la ciudadanía puede cargar información estructurada en segundos, especialmente durante las primeras 24–48 horas.
2. **Una capa de analítica y modelización probabilística del tiempo**, basada en evidencia internacional sobre la ventana crítica de hallazgo.
3. **Un rol de "hub" de integración**, capaz de conectarse por API con los sistemas existentes y organizar información que hoy se encuentra dispersa.

De esta forma, el proyecto no busca competir con las herramientas actuales, sino potenciar su eficacia al aumentar la cantidad y la calidad de la información disponible en el tramo temporal donde la curva de resolución es más pronunciada. A la vez, genera las condiciones para producir datos propios y desagregados que, a futuro, permitan ajustar los modelos probabilísticos a la realidad argentina y contribuir a la formulación de políticas públicas basadas en evidencia.

---

### Fuentes y referencias

- Sistema Federal de Búsqueda de Personas Desaparecidas y Extraviadas (SIFEBU) – Ministerio de Seguridad de la Nación.
- Defensa Pública Argentina – Documentos sobre registro de personas desaparecidas.
- National Crime Agency (NCA), Reino Unido – Estadísticas de personas desaparecidas.
- Missing People UK – Estudios sobre ventana temporal crítica de hallazgo.
- Registro de Chicos Perdidos – Consejo de los Derechos de Niñas, Niños y Adolescentes de CABA.
- Dirección Provincial de Registro de Personas Desaparecidas – Provincia de Buenos Aires.
- Missing Children Argentina – Estadísticas y protocolos de búsqueda.
- Estudios académicos, entre ellos *Geographies of Missing People* (Discovery UCL).

**Nota metodológica:**  
Los porcentajes de hallazgo citados provienen de estudios del Reino Unido y de Europa, utilizados como modelo de referencia para el caso argentino ante la ausencia de estadísticas públicas locales desagregadas por horizonte temporal. El proyecto Find Me no pretende validar científicamente estos porcentajes, sino utilizarlos como base para un modelo probabilístico inicial, susceptible de ser ajustado a partir de los datos que el propio sistema genere una vez operativo.

---
## 2. OBJETIVO GENERAL (REFERENCIA)

**Desarrollar una aplicación web funcional que permita a ciudadanos y autoridades reportar, visualizar y buscar avistamientos geolocalizados de personas desaparecidas en Argentina, reduciendo el tiempo de respuesta en las primeras 48 horas críticas mediante la centralización de información dispersa en un mapa interactivo y un sistema de fichero estructurado.**

---

## 3. OBJETIVOS ESPECÍFICOS DETALLADOS

### 3.1 Objetivos Técnicos – Infraestructura y Arquitectura

#### OE-T1: Implementar backend RESTful con stack tecnológico definido

**Descripción:**  
Desarrollar la capa de backend utilizando Java Spring Boot 3.x con arquitectura limpia (Clean Architecture), PostgreSQL 15+ con extensión PostGIS para manejo de datos geoespaciales y autenticación mediante JWT para seguridad de endpoints.

**Justificación:**  
La arquitectura limpia garantiza mantenibilidad y escalabilidad futura. PostGIS es fundamental para consultas espaciales eficientes. JWT permite autenticación stateless adecuada para APIs RESTful.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- API RESTful operativa con al menos 12 endpoints documentados en Swagger.
- Consultas espaciales PostGIS ejecutándose en < 200 ms.
- Autenticación JWT implementada y funcional en endpoints protegidos.

---

#### OE-T2: Desarrollar frontend responsivo con visualización geoespacial

**Descripción:**  
Crear la interfaz de usuario utilizando JavaScript ES6+ modular, HTML5 y CSS3, integrando Leaflet.js para renderizado de mapas interactivos con soporte para dispositivos móviles y desktop.

**Justificación:**  
Una gran proporción de usuarios en Argentina accede desde dispositivos móviles. Leaflet.js es open source y no requiere API keys pagas (a diferencia de Google Maps). JavaScript vanilla garantiza control total, bajo peso y menor complejidad de dependencias.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- Mapa interactivo funcional con Leaflet.
- Responsive design operativo desde 320 px de ancho.
- Lighthouse Performance Score ≥ 75 en E1 y ≥ 85 en E2.

---

#### OE-T3: Establecer modelo de datos con relaciones espaciales

**Descripción:**  
Diseñar e implementar el modelo entidad-relación con tablas de `usuarios`, `personas_desaparecidas`, `avistamientos` y `avistadores`, incorporando tipos de datos geométricos (`GEOMETRY POINT`) para almacenar coordenadas con precisión de 6 decimales (~10 cm).

**Justificación:**  
El modelo debe soportar relaciones 1:N (una persona → muchos avistamientos) y consultas espaciales complejas. La precisión geométrica es crítica para mapas de calor y análisis espaciales futuros.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- Al menos 4 tablas principales con sus relaciones implementadas.
- Índices espaciales GIST operativos.
- Consultas de distancia funcionando correctamente.

---

### 3.2 Objetivos Funcionales – Features Core del Sistema

#### OE-F1: Implementar sistema CRUD completo de entidades principales

**Descripción:**  
Desarrollar funcionalidades completas de creación, lectura, actualización y eliminación lógica (soft delete) para las entidades Avistador, Persona Desaparecida y Avistamiento, con validaciones de negocio y manejo adecuado de errores.

**Justificación:**  
El CRUD es la base funcional del sistema. Sin operaciones básicas bien implementadas, no se puede agregar complejidad. La eliminación lógica permite auditoría y recuperación de información.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- 3 entidades con CRUD completo operativo.
- Validaciones de campos obligatorios funcionando correctamente.
- Manejo de errores con códigos HTTP apropiados (400, 404, 500).
- Tiempo de respuesta CRUD < 500 ms (percentil 95).

---

#### OE-F2: Desarrollar visualización geolocalizada de avistamientos en mapa interactivo

**Descripción:**  
Crear un mapa interactivo principal que muestre todos los avistamientos activos como marcadores geolocalizados, con capacidad de zoom, navegación y click para ver detalles del avistamiento.

**Justificación:**  
La visualización geográfica es el valor diferencial del sistema frente a listas tradicionales. Permite identificar patrones espaciales y zonas de concentración de avistamientos.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- El mapa carga y muestra marcadores correctamente.
- El click en marcador despliega información completa del avistamiento.
- Zoom y navegación sin errores de renderizado.
- Carga de hasta 100 marcadores en E1 sin lag notable.

---

#### OE-F3: Crear sistema de carga de avistamientos con selección geográfica

**Descripción:**  
Implementar un formulario (por ejemplo, modal) que permita al usuario seleccionar un punto en el mapa, completar datos obligatorios (DNI, nombre, apellido, edad, descripción, foto) y campos opcionales (vestimenta, estado aparente), asociando el avistamiento a una persona desaparecida y a un avistador.

**Justificación:**  
Reducir fricción en la carga de datos es crítico para la adopción. La selección en mapa es más intuitiva que ingresar coordenadas manualmente. Los campos obligatorios garantizan una calidad mínima de datos.

**Entrega:** E1 (MVP Técnico)

**Medible:**
- Usuario completa la carga de un avistamiento en < 2 minutos.
- Validación de campos obligatorios funcionando correctamente.
- Foto subida a un servicio externo (ej. Cloudinary) exitosamente.
- Geolocalización captura coordenadas con la precisión esperada.

---

#### OE-F4: Desarrollar sistema de búsqueda multiparámetro (fichero)

**Descripción:**  
Crear un módulo de búsqueda que permita localizar personas desaparecidas por DNI (exacto), nombre (aproximado con fuzzy search) y apellido (aproximado), devolviendo resultados en menos de 3 segundos con vista previa de foto y datos básicos.

**Justificación:**  
Familias y ciudadanos necesitan verificar rápidamente si una persona ya está reportada antes de crear un nuevo registro. La búsqueda aproximada tolera errores de tipeo y mejora la experiencia de uso.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- Búsqueda por DNI devuelve resultado exacto en < 1 segundo.
- Búsqueda por nombre/apellido con tolerancia a hasta 2 caracteres de error.
- Tiempo de respuesta total < 3 segundos.
- Resultados incluyen preview con foto y datos básicos.

---

#### OE-F5: Implementar perfiles individuales con timeline de avistamientos

**Descripción:**  
Desarrollar una vista de perfil por persona que agrupe todos sus avistamientos en un mapa personalizado con marcadores cronológicos, timeline ordenada por fecha/hora, y detalles completos de cada ficha, incluyendo datos del avistador.

**Justificación:**  
Ver la evolución espacial y temporal de los avistamientos permite identificar patrones de movimiento, zonas frecuentadas y reconstruir trayectorias probables. Es información crítica para fuerzas de seguridad y familias.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- El mapa personalizado muestra todos los avistamientos de la persona.
- Timeline ordenada cronológicamente (más reciente primero).
- El click en un elemento de la timeline centra el mapa en ese avistamiento.
- Carga del perfil completo en < 3 segundos.

---

#### OE-F6: Crear diferenciación visual de casos recientes versus antiguos

**Descripción:**  
Implementar un código de colores en los marcadores del mapa que diferencie visualmente avistamientos de los últimos 30 días (color destacado) de los anteriores (color atenuado), con leyenda explicativa y filtro de rango de fechas.

**Justificación:**  
Los casos recientes tienen mayor probabilidad de resolución (80-90% en 48h según estudios internacionales). La diferenciación visual permite priorizar rápidamente la información más relevante.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- Marcadores cambian de color según antigüedad de forma automática.
- Leyenda visible y clara en el mapa.
- Filtro por rango de fechas funcional.
- Actualización de colores en tiempo real al modificar el filtro de fechas.

---

### 3.3 Objetivos de Integración y Datos

#### OE-I1: Integrar validación de DNI con base de datos municipal

**Descripción:**  
Establecer conexión con una base de datos del municipio de Vicente López para validar la existencia del DNI ingresado, marcando en el sistema si el DNI está verificado o no verificado.

**Justificación:**  
La validación reduce reportes falsos y mejora la confiabilidad del sistema. Utilizar Vicente López como piloto permite probar el concepto antes de escalar a nivel nacional.

**Entrega:** E2 (Sistema Completo) – Opcional, sujeto a acceso a la base

**Medible:**
- API de validación operativa (si se obtiene acceso).
- Marca "DNI verificado" se muestra correctamente.
- Tiempo de validación < 2 segundos.
- Fallback documentado si no hay acceso a la base municipal.

---

#### OE-I2: Preparar endpoints para integración futura con SIFEBU

**Descripción:**  
Diseñar y documentar la estructura de endpoints de la API con potencial de integración con el Sistema Federal de Búsqueda (SIFEBU), siguiendo estándares REST y documentando en OpenAPI/Swagger.

**Justificación:**  
Aunque la integración real está fuera del alcance del proyecto, diseñar con esta posibilidad en mente facilita una adopción futura por parte de instituciones y demuestra visión de escalabilidad.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- Endpoints documentados en formato OpenAPI 3.0.
- Estructura de datos compatible con JSON estándar.
- Documentación incluye escenarios de uso de integración.
- Al menos 3 endpoints "integration-ready".

---

### 3.4 Objetivos de Calidad y Experiencia de Usuario

#### OE-Q1: Garantizar usabilidad multidispositivo con responsive design

**Descripción:**  
Asegurar que todas las funcionalidades del sistema sean completamente operativas y usables en dispositivos móviles (desde 320 px), tablets (768 px+) y desktop (1024 px+), con interfaces táctiles optimizadas para móviles.

**Justificación:**  
En Argentina, la mayor parte del tráfico web es móvil. Si el sistema no funciona bien en celulares, pierde gran parte de su potencial de adopción. Además, las familias en crisis no siempre tienen acceso a computadoras.

**Entrega:** E1 (MVP Técnico) – versión básica; E2 (Sistema Completo) – versión refinada

**Medible:**
- Sistema funcional en pantallas desde 320 px de ancho.
- Formularios completables en dispositivos táctiles sin necesidad de zoom.
- Mapa navegable con gestos táctiles (pinch, swipe).
- Pruebas exitosas en al menos: un iPhone SE, un Android gama media, una tablet y un desktop.

---

#### OE-Q2: Optimizar performance para carga de múltiples marcadores

**Descripción:**  
Implementar técnicas de optimización (clustering de marcadores, lazy loading, paginación) que permitan visualizar hasta 1000 avistamientos en el mapa sin degradación notable de performance (carga inicial < 3 segundos).

**Justificación:**  
A medida que el sistema crece, el número de avistamientos puede volverse crítico para la performance. El clustering agrupa marcadores cercanos según nivel de zoom, manteniendo el mapa usable incluso con muchos puntos.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- El mapa carga 1000+ marcadores en < 3 segundos.
- Clustering agrupa/desagrupa según nivel de zoom.
- El desplazamiento del mapa no presenta lag perceptible.
- Lighthouse Performance Score ≥ 85.

---

#### OE-Q3: Implementar cobertura de tests unitarios y de integración

**Descripción:**  
Desarrollar una suite de pruebas automatizadas con JUnit 5 y Mockito para la lógica de negocio del backend, alcanzando cobertura mínima del 70 % en Entrega 1 y del 80 % en Entrega 2, priorizando código crítico (CRUD, queries espaciales, validaciones).

**Justificación:**  
Los tests automatizados reducen regresiones, facilitan refactoring y dan confianza para agregar nuevas funcionalidades. Una cobertura del 80 % es un estándar razonable para proyectos serios. La cobertura progresiva (70 % → 80 %) es realista dentro del tiempo disponible.

**Entrega:** E1 (≥ 70 %), E2 (≥ 80 %)

**Medible:**
- Cobertura reportada por JaCoCo ≥ 70 % en E1.
- Cobertura reportada por JaCoCo ≥ 80 % en E2.
- Tests de integración E2E funcionales en E2.
- Pipeline de CI ejecuta tests automáticamente.

---

### 3.5 Objetivos de Validación con Usuarios

#### OE-V1: Realizar UAT (User Acceptance Testing) con usuarios reales

**Descripción:**  
Ejecutar una sesión de pruebas de aceptación con al menos 3 usuarios externos representativos (por ejemplo: familiar de persona desaparecida, voluntario de ONG, ciudadano promedio), documentando feedback, incorporando ajustes críticos y obteniendo un nivel de satisfacción ≥ 75 %.

**Justificación:**  
El sistema puede ser técnicamente sólido pero poco usable en la práctica. La UAT con usuarios reales valida que el sistema cumple necesidades reales y que la interfaz es intuitiva para la población general sin entrenamiento técnico.

**Entrega:** E2 (Sistema Completo)

**Medible:**
- Al menos 3 usuarios externos completan la sesión de UAT.
- Cada usuario completa 5 tareas predefinidas.
- Tasa de completitud de tareas ≥ 80 %.
- Encuesta de satisfacción ≥ 75 % (escala 1–10 → ≥ 7,5).
- Feedback documentado en formato estructurado.

---

### 3.6 Objetivos de Gestión del Proyecto

#### OE-G1: Completar Entrega 1 (MVP Técnico) antes del 20/12/2025
#### OE-G2: Completar Entrega 2 (Sistema Completo) antes del 20/02/2026
#### OE-G3: Desarrollar el proyecto con máximo 150 horas/hombre
#### OE-G4: Mantener presupuesto en $0 ARS

*(Metas y métricas sintetizadas en el Acta; aquí se mantienen como referencia.)*

---

### 3.7 Matriz de Objetivos SMART (Resumida)

| ID | Objetivo | Specific | Measurable | Achievable | Relevant | Time-bound |
|----|----------|----------|------------|------------|----------|------------|
| OE-T1 | Backend RESTful | ✅ Stack definido | ✅ 12+ endpoints | ✅ Tecnología conocida | ✅ Core del sistema | ✅ E1: 20/12/25 |
| OE-T2 | Frontend Leaflet | ✅ JS + Leaflet | ✅ Score ≥75 | ✅ Doc extensa | ✅ Visualización crítica | ✅ E1: 20/12/25 |
| OE-F1 | CRUD completo | ✅ 3 entidades | ✅ <500ms P95 | ✅ Patrón estándar | ✅ Funcionalidad base | ✅ E1: 20/12/25 |
| OE-F4 | Sistema búsqueda | ✅ DNI/nombre/apellido | ✅ <3 seg | ✅ Queries SQL estándar | ✅ Feature clave E2 | ✅ E2: 20/02/26 |
| OE-Q3 | Tests unitarios | ✅ JUnit + Mockito | ✅ ≥70/80% | ✅ Framework conocido | ✅ Calidad del código | ✅ E1/E2 |
| OE-V1 | UAT usuarios | ✅ 3 usuarios externos | ✅ ≥75% satisfacción | ✅ Red contactos disponible | ✅ Validación real | ✅ E2: 20/02/26 |
| OE-G1 | Completar E1 | ✅ MVP Técnico | ✅ Fecha límite | ✅ Alcance acotado | ✅ Compromiso académico | ✅ 20/12/25 |
| OE-G3 | 150 horas máx | ✅ Registro semanal | ✅ ≤150h totales | ✅ 15–20h/semana | ✅ Gestión de tiempo | ✅ Transversal |

---

### 3.8 Priorización de Objetivos (MoSCoW)

**Must Have (críticos):**
- OE-T1, OE-T2, OE-T3
- OE-F1, OE-F2, OE-F3
- OE-G1, OE-G2

**Should Have (importantes):**
- OE-F4, OE-F5
- OE-Q1, OE-Q3
- OE-V1

**Could Have (deseables):**
- OE-F6
- OE-Q2
- OE-I1

**Won't Have (fuera de alcance actual):**
- Integración real con SIFEBU (más allá de endpoints preparados).
- App móvil nativa.
- Sistema de notificaciones masivas.

---

## 4. ALCANCE DETALLADO POR ENTREGA

### 4.1 Alcance Detallado - ENTREGA 1 (Diciembre 2025) – MVP Técnico

**MVP Técnico - Funcionalidades Core:**

#### 4.1.1 Gestión de Entidades Base

- **CRUD completo de Avistadores**
    - Crear avistador con datos personales.
    - Consultar información de avistadores.
    - Actualizar datos de avistador existente.
    - Relación con avistamientos realizados.

- **CRUD completo de Personas Desaparecidas**
    - Registrar persona desaparecida con datos completos.
    - DNI, nombre, apellido, edad, foto.
    - Fecha de primer reporte.
    - Estado (activo/encontrado/cerrado).
    - Consultar y actualizar información.

- **CRUD completo de Avistamientos**
    - Crear avistamiento con geolocalización.
    - Fecha y hora del avistamiento.
    - Descripción detallada, vestimenta, estado aparente.
    - Asociación con persona desaparecida.
    - Asociación con avistador que reporta.
    - Consultar y actualizar avistamientos.

#### 4.1.2 Mapa Interactivo Básico

- Visualización de avistamientos en mapa con Leaflet.js.
- Marcadores geolocalizados por cada avistamiento.
- Click en marcador muestra información básica del avistamiento.
- Zoom y navegación funcional.
- Botón "+" para agregar nuevo avistamiento (abre formulario).
- Centrado automático en área de interés.

#### 4.1.3 Formularios de Carga

- Formulario de alta de persona desaparecida.
- Formulario de alta de avistador.
- Formulario de carga de avistamiento con selección de punto en mapa.
- Validaciones básicas de campos obligatorios.
- Carga de imagen (foto de la persona desaparecida).

#### 4.1.4 Backend API RESTful

- Endpoints CRUD para las 3 entidades principales.
- Autenticación JWT básica.
- Persistencia en PostgreSQL con PostGIS.
- Consultas espaciales básicas (puntos en área).
- Documentación Swagger de endpoints.
- Manejo de errores y validaciones.

#### 4.1.5 Frontend Responsivo

- HTML5 + CSS3 + JavaScript ES6.
- Integración con Leaflet.js para mapas.
- Responsive básico (desktop + mobile).
- Interfaz intuitiva y amigable.

#### 4.1.6 Infraestructura

- Sistema desplegado en ambiente productivo (Vercel/Railway u otro similar).
- Base de datos PostgreSQL con PostGIS operativa.
- Almacenamiento de imágenes en cloud (Cloudinary Free).

#### 4.1.7 Documentación

- Documentación técnica de arquitectura.
- Diagramas de componentes y modelo de datos.
- README con instrucciones de instalación.
- Documentación de APIs con Swagger.

---

### 4.2 Alcance Detallado - ENTREGA 2 (Febrero 2026) – Sistema Completo

**Sistema Completo - Funcionalidades Adicionales:**

#### 4.2.1 Sistema de Búsqueda (Fichero)

- **Búsqueda por DNI (exacta)**
    - Input de DNI devuelve persona específica.
    - Respuesta en menos de 3 segundos.

- **Búsqueda por Nombre (aproximada)**
    - Búsqueda fuzzy que tolera errores ortográficos.
    - Resultados ordenados por relevancia.

- **Búsqueda por Apellido (aproximada)**
    - Similar a búsqueda por nombre.
    - Listado de resultados con preview.

#### 4.2.2 Perfiles Individuales de Persona

- **Mapa personalizado** con todos los avistamientos de esa persona.
- **Timeline cronológica** de avistamientos.
- **Detalles de cada ficha** registrada con información completa.
- **Foto principal** (de la primera carga).
- **Datos de contacto** del reportante original.
- **Historial completo** de reportes.

#### 4.2.3 Diferenciación Visual

- Avistamientos de últimos 30 días en color diferenciado.
- Indicador visual de casos recientes vs. antiguos.
- Filtros por fecha en mapa principal.

#### 4.2.4 Optimizaciones de Performance

- **Clustering de marcadores** para manejar muchos puntos.
- Carga lazy de avistamientos.
- Paginación en listados.
- Optimización de queries espaciales.

#### 4.2.5 Validaciones e Integraciones

- Validación de DNI contra base de datos municipal (Vicente López) si es posible.
- Términos y condiciones del servicio.
- Política de privacidad.
- Sistema básico de moderación de contenido.

#### 4.2.6 Testing y Calidad

- Suite de tests unitarios completa (≥80% cobertura).
- Tests de integración E2E.
- Tests de carga básicos.
- Corrección de bugs identificados en UAT.

#### 4.2.7 UAT y Validación

- Pruebas de aceptación con al menos 3 usuarios externos.
- Incorporación de feedback de usuarios reales.
- Ajustes basados en observaciones de UAT.

#### 4.2.8 Documentación Completa

- Manual de usuario final.
- Guía de deployment.
- Plan de difusión y adopción.
- Lecciones aprendidas.

---

### 4.3 Fuera del Alcance (Ambas Entregas)

- Integración completa con RENAPER o bases de datos nacionales.
- Sistema de notificaciones push o email automáticas.
- Chat entre usuarios en tiempo real.
- App móvil nativa (iOS/Android).
- Integración automatizada con redes sociales.
- Sistema de verificación de identidad del reportante con biometría.
- Geofencing o alertas automáticas por proximidad.
- Integración total y en tiempo real con APIs de SIFEBU.
- Sistema de machine learning para detección avanzada de patrones.
- Alertas masivas tipo Alerta Sofía integradas institucionalmente.
- Moderación automática con IA avanzada.

---

## 5. CRITERIOS DE ÉXITO, KPIs Y CRITERIOS DE ACEPTACIÓN

### 5.1 KPIs Técnicos

- **Tiempo de respuesta API:** < 500 ms (percentil 95).
- **Uptime:** ≥ 99 % en horario productivo (9-23 hs).
- **Errores en producción:** < 5 por semana.
- **Lighthouse Performance:** ≥ 75 (E1), ≥ 85 (E2).

### 5.2 KPIs Funcionales

**Entrega 1:**
- CRUD operativo para 3 entidades.
- Visualización correcta de avistamientos en el mapa.
- Tiempo de carga inicial < 5 segundos.

**Entrega 2:**
- Búsqueda por DNI 100 % precisa.
- Tiempo de búsqueda < 3 segundos.
- Mapa soporta 1000+ marcadores con clustering en < 3 segundos.
- Compatibilidad con navegadores modernos.

### 5.3 KPIs de Proyecto

**Entrega 1:**
- Cumplimiento de cronograma ≥ 85 %.
- Horas ejecutadas ≤ 80.

**Entrega 2:**
- Cumplimiento de cronograma ≥ 90 %.
- Horas ejecutadas totales ≤ 150.
- Documentación ≥ 100 % de funcionalidades.

---

### 5.4 Criterios de Aceptación - ENTREGA 1 (Diciembre 2025)

La primera entrega se considerará exitosa cuando:

#### Funcionalidad Core

- ✅ Backend API REST está desplegado y operativo.
- ✅ Un usuario puede crear un avistador con datos completos.
- ✅ Un usuario puede crear una persona desaparecida con foto, DNI, nombre, apellido y edad.
- ✅ Un usuario puede crear un avistamiento asociado a una persona y un avistador.
- ✅ El avistamiento incluye geolocalización, descripción, fecha/hora.

#### Visualización y Mapa

- ✅ El avistamiento aparece en el mapa interactivo en su ubicación correcta.
- ✅ Click en marcador muestra información completa del avistamiento.
- ✅ El mapa permite zoom, navegación y selección de punto para nuevo avistamiento.
- ✅ Botón "+" abre formulario de carga correctamente.

#### Persistencia y Datos

- ✅ Las relaciones entre entidades persisten correctamente en BD.
- ✅ Las geometrías PostGIS se almacenan y consultan correctamente.
- ✅ Las imágenes se suben y almacenan en Cloudinary.
- ✅ Los datos persisten después de reiniciar el servidor.

#### Calidad y Testing

- ✅ Tests unitarios de lógica crítica pasan (≥70% cobertura).
- ✅ No hay errores críticos en consola del navegador.
- ✅ El sistema maneja correctamente errores de validación.

#### Documentación y Deploy

- ✅ Existe documentación técnica de arquitectura.
- ✅ APIs documentadas en Swagger.
- ✅ Sistema desplegado y accesible públicamente.
- ✅ README con instrucciones de instalación y uso.

#### Demo y Presentación

- ✅ Demo funcional del flujo CRUD completo.
- ✅ Flujo end-to-end: crear avistador → crear persona → crear avistamiento → ver en mapa.

#### Responsive

- ✅ Sistema responsive en mobile (≥ 360 px).
- ✅ Formularios usables en dispositivos móviles.

---

### 5.5 Criterios de Aceptación - ENTREGA 2 (Febrero 2026)

La entrega final se considerará exitosa cuando cumpla todo lo anterior más:

#### Sistema de Búsqueda

- ✅ Búsqueda por DNI con resultados exactos en < 3 segundos.
- ✅ Búsqueda por nombre con resultados aproximados (fuzzy search).
- ✅ Búsqueda por apellido con resultados aproximados.
- ✅ Resultados muestran preview con foto y datos básicos.

#### Perfiles Individuales

- ✅ Perfil individual muestra mapa con todos los avistamientos de la persona.
- ✅ Timeline cronológica de avistamientos.
- ✅ Cada avistamiento muestra información completa.
- ✅ Foto principal visible y correcta.
- ✅ Datos de contacto del reportante original disponibles.

#### Diferenciación Visual

- ✅ Avistamientos de últimos 30 días diferenciados visualmente.
- ✅ Filtro funcional por rango de fechas.
- ✅ Leyenda del mapa explica la diferenciación de colores.

#### Performance y Optimización

- ✅ Mapa maneja más de 1000 puntos con clustering.
- ✅ Clustering agrupa y desagrupa marcadores correctamente.
- ✅ Carga inicial < 3 segundos.
- ✅ Sin degradación notable de performance con muchos datos.

#### Validación e Integraciones

- ✅ Validación de DNI con base municipal funciona (o está claramente documentada como opcional).
- ✅ Términos y condiciones implementados.
- ✅ Política de privacidad visible y accesible.

#### Testing y Calidad

- ✅ Cobertura de tests ≥ 80 %.
- ✅ Tests de integración E2E pasan.
- ✅ Sin bugs críticos conocidos sin resolver.

#### UAT y Usuarios

- ✅ UAT exitosa con al menos 3 usuarios externos.
- ✅ Feedback incorporado en el sistema.
- ✅ Usuarios pueden completar flujos principales sin ayuda.

#### Documentación

- ✅ Manual de usuario completo.
- ✅ Guía de deployment documentada.
- ✅ Plan de difusión definido.

#### Sistema Completo

- ✅ Funcionalidades de Entrega 1 siguen funcionando.
- ✅ Sistema estable en producción.
- ✅ Plan de contingencia documentado.

---

## 6. RIESGOS DETALLADOS

| ID | Riesgo | Probabilidad | Impacto | Fase | Mitigación |
|----|--------|--------------|---------|------|-----------|
| R1 | Falta de acceso a base de datos municipal | Alta | Medio | E2 | Implementar validación opcional; usar otras fuentes si existieran; documentar claramente. |
| R2 | Problemas de rendimiento con muchos marcadores | Media | Alto | E2 | Implementar clustering, paginación, lazy loading; optimizar consultas PostGIS. |
| R3 | Carga de imágenes falsas o inapropiadas | Alta | Alto | E2 | Moderación manual, términos de uso, posibilidad de reporte de contenido. |
| R4 | Mal uso del sistema (trolls, datos falsos) | Media | Alto | E2 | Captcha, límites por IP, moderación, logs y trazabilidad. |
| R5 | Problemas legales por privacidad de datos | Media | Crítico | E1–E2 | Consultar normativa básica, definir términos y condiciones, minimizar datos sensibles. |
| R6 | Falta de adopción/usuarios para UAT | Alta | Medio | E2 | Contactar ONGs, red de amigos/conocidos, difusión acotada en redes. |
| R7 | Costos de hosting si escala | Baja | Medio | E2 | Monitorear uso de recursos, evaluar migración a planes pagos sólo si el proyecto escala. |
| R8 | Bloqueo técnico por complejidad de PostGIS | Media | Alto | E1 | POCs tempranas, simplificar queries, apoyo en documentación oficial. |
| R9 | Abandono del proyecto por falta de tiempo | Media | Crítico | E1–E2 | Scope claro, objetivos Must Have priorizados, planificación de sprints realistas. |
| R10 | Dependencia de servicios externos (Cloudinary, Railway, etc.) | Media | Medio | E1–E2 | Abstraer integraciones, tener plan B (otros proveedores, almacenamiento local). |
| R11 | Retraso en Entrega 1 afecta Entrega 2 | Media | Alto | E1 | Reducir alcance de features no críticas; asegurar cumplimiento mínimo de MVP. |
| R12 | Feedback de UAT requiere cambios mayores | Media | Medio | E2 | Priorizar cambios críticos, dejar mejoras menores para una fase posterior. |

---

## 7. MODELO DE DATOS PRELIMINAR

### 7.1 Tablas Principales

**Tabla: `usuarios`**
- `id_usuario` (PK)
- `email`
- `password_hash`
- `nombre`
- `apellido`
- `fecha_registro`

**Tabla: `personas_desaparecidas`**
- `dni` (PK)
- `nombre`
- `apellido`
- `edad`
- `foto_url`
- `fecha_primer_reporte`
- `estado` (activo/encontrado/cerrado)

**Tabla: `avistadores`**
- `id_avistador` (PK)
- `nombre`
- `apellido`
- `relacion_con_persona`
- `contacto`

**Tabla: `avistamientos`**
- `id_avistamiento` (PK)
- `dni_persona` (FK → personas_desaparecidas.dni)
- `fecha_avistamiento`
- `ubicacion` (GEOMETRY POINT, SRID adecuado)
- `descripcion`
- `vestimenta`
- `estado_aparente`
- `id_avistador` (FK → avistadores.id_avistador)
- `fecha_carga`

---

## 8. STACK TECNOLÓGICO COMPLETO

### 8.1 Backend

- Java 17+
- Spring Boot 3.2+
- Spring Security + JWT
- Spring Data JPA
- Hibernate Spatial
- PostgreSQL 15+
- PostGIS 3.x
- Maven
- JUnit 5 + Mockito
- JaCoCo (cobertura)

### 8.2 Frontend

- JavaScript ES6+
- Leaflet.js 1.9+
- HTML5 / CSS3
- Fetch API
- Módulos ES6

### 8.3 DevOps y Herramientas

- Git + GitHub
- GitHub Actions (CI/CD)
- Railway/Render (hosting backend)
- Vercel/Netlify (hosting frontend)
- Cloudinary (almacenamiento de imágenes)
- DBeaver (gestión de BD)
- Postman / Thunder Client (test API)
- Figma (mockups UI)
- IntelliJ IDEA / VSCode (IDE)

---

## 9. ENTREGABLES DEL PROYECTO

### 9.1 Clasificación de Entregables

Los entregables del proyecto se agrupan en cinco categorías principales:

1. **Entregables de Documentación:** Especificaciones, arquitectura, manuales, planes.
2. **Entregables de Desarrollo:** Código fuente, módulos funcionales.
3. **Entregables de Infraestructura:** Despliegues, configuración de entornos.
4. **Entregables de Validación:** Reportes de pruebas, cobertura, UAT.
5. **Entregables de Presentación:** Presentaciones y demos para el tribunal.

**Nota general:**  
Los objetivos cuantitativos detallados (tiempos de respuesta, cobertura de tests, performance, etc.) se definen en la sección de KPIs del Anexo 1. En esta sección, los criterios de aceptación se refieren a dichos KPIs cuando corresponda, sin repetir todos los valores numéricos.

**Nota sobre prioridad (MoSCoW):**  
La mayoría de estos entregables son Must/Should para las entregas académicas. Algunos entregables de carácter estratégico (por ejemplo, Plan de Difusión y Lecciones Aprendidas) se consideran Deseables (Could Have) y su alcance final podrá ajustarse sin comprometer la aprobación del proyecto.

---

### 9.2 Entregables - Entrega 1 (MVP Técnico)

#### E1-D1: Documento de Arquitectura del Sistema (Documentación – Must)

**Descripción:**  
Documento técnico que detalla la arquitectura del sistema, incluyendo diagramas de componentes, capas, modelo de datos y decisiones arquitectónicas clave.

**Contenido mínimo:**
- Diagrama de arquitectura general (Frontend, Backend, BD, Storage).
- Diagrama de capas (Presentación, Lógica de Negocio, Persistencia).
- Modelo entidad-relación (ERD) con tablas y relaciones principales.
- Decisiones arquitectónicas (ADRs básicos):
    - Clean Architecture vs alternativas.
    - PostGIS vs PostgreSQL puro.
    - Leaflet vs Google Maps u otros.
    - JWT vs sesiones tradicionales.
- Stack tecnológico completo con breve justificación.

**Formato:** Markdown (.md) o PDF  
**Fecha de entrega:** 28/02/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Diagramas claros y legibles.
- ✓ Modelo de datos coherente con el alcance del proyecto.
- ✓ Decisiones arquitectónicas justificadas de forma sintética.
- ✓ Stack tecnológico documentado y consistente con el resto del Anexo.

**Dependencias:** Ninguna (primer entregable).

---

#### E1-D2: Prototipo de UI (Mockups) (Documentación/UX – Should)

**Descripción:**  
Mockups estáticos o navegables que muestran las pantallas principales y flujos básicos del sistema.

**Contenido mínimo:**
- Pantalla principal con mapa y menú básico.
- Formulario de alta de persona desaparecida.
- Formulario de alta de avistamiento.
- Modal de información del avistamiento.
- Vista responsive (mobile + desktop) a nivel de diseño.

**Formato:** Figma (link compartible) o imágenes PNG/JPG  
**Fecha de entrega:** 15/03/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Mockups cubren el flujo básico de carga y visualización de avistamientos.
- ✓ Se visualizan variantes mobile y desktop.
- ✓ Están definidos lineamientos básicos de colores y tipografía.

**Dependencias:** E1-D1 (arquitectura y modelo conceptual definidos).

---

#### E1-DEV1: API Backend v1.0 (Desarrollo – Must)

**Descripción:**  
Backend API RESTful funcional con endpoints CRUD para las entidades principales, autenticación básica con JWT y documentación Swagger inicial.

**Contenido mínimo:**
- Endpoints CRUD de Avistadores.
- Endpoints CRUD de Personas Desaparecidas.
- Endpoints CRUD de Avistamientos.
- Endpoint(s) de autenticación (login/registro o equivalente).
- Documentación Swagger accesible.
- Primer set de tests unitarios básicos para lógica de negocio.

**Formato:** Código fuente Java + Spring Boot (repositorio GitHub).  
**Fecha de entrega:** 30/06/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ La API permite realizar operaciones CRUD sobre las 3 entidades principales.
- ✓ Swagger expone todos los endpoints implementados.
- ✓ La autenticación JWT protege los endpoints que corresponda.
- ✓ Existe una primera base de tests automatizados, cuya cobertura y detalle se amplían en el entregable E1-VAL1.
- ✓ Los tiempos de respuesta se mantienen alineados con los KPIs técnicos definidos en el Anexo 1.

**Dependencias:** E1-D1 (modelo de datos y decisiones de arquitectura).

---

#### E1-DEV2: Frontend v1.0 con Mapa Interactivo (Desarrollo – Must)

**Descripción:**  
Interfaz web inicial que consume la API y muestra avistamientos geolocalizados en un mapa con Leaflet.

**Contenido mínimo:**
- Página principal con mapa (vista dominante).
- Mapa Leaflet con marcadores de avistamientos.
- Popup con información básica al hacer click en un marcador.
- Formularios de alta de persona desaparecida y avistamiento.
- Integración con servicio de imágenes (Cloudinary u otro) mediante URLs.
- Diseño responsive básico (mobile y desktop).

**Formato:** HTML/CSS/JavaScript (repositorio GitHub).  
**Fecha de entrega:** 30/09/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El mapa se visualiza correctamente y muestra marcadores provenientes de la API.
- ✓ Los formularios permiten crear registros que se reflejan en el mapa.
- ✓ Las imágenes se almacenan y recuperan vía Cloudinary (u otro proveedor).
- ✓ La UI es utilizable en pantallas pequeñas (≈320px) y desktop.
- ✓ La performance general es razonable y acorde a los KPIs de frontend definidos en el Anexo.

**Dependencias:** E1-DEV1 (API operativa).

---

#### E1-DEV3: Sistema CRUD Completo Integrado (Desarrollo – Must)

**Descripción:**  
Sistema integrado backend+frontend con el flujo CRUD completo operativo para las entidades principales.

**Contenido mínimo:**
- Flujo completo:
    - Crear avistador.
    - Crear persona desaparecida.
    - Crear avistamiento asociado.
    - Visualizar avistamiento en el mapa.
- Validaciones básicas en cliente y servidor.
- Manejo de errores con mensajes legibles para el usuario.
- Relaciones entre entidades persistiendo correctamente en base de datos.

**Formato:** Sistema funcional en GitHub (mismo repo o repos relacionados).  
**Fecha de entrega:** 30/11/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ CRUD de las 3 entidades funciona en forma estable.
- ✓ El flujo end-to-end (avistador → persona → avistamiento → mapa) puede completarse sin errores.
- ✓ Las validaciones impiden datos evidentemente inválidos.
- ✓ Las relaciones FK (persona–avistamientos, avistador–avistamientos) funcionan correctamente.

**Dependencias:** E1-DEV1 + E1-DEV2.

---

#### E1-INF1: Sistema Desplegado en Producción (MVP) (Infraestructura – Must)

**Descripción:**  
MVP desplegado en un ambiente accesible públicamente, con backend, frontend, base de datos y almacenamiento de imágenes operativos.

**Contenido mínimo:**
- Backend desplegado en Railway/Render (u otro) con URL pública.
- Frontend desplegado en Vercel/Netlify (u otro) con URL pública.
- Base de datos PostgreSQL + PostGIS configurada.
- Proveedor de imágenes configurado (Cloudinary u otro).
- Variables de entorno configuradas correctamente.
- Certificado SSL activo (HTTPS, ya sea propio o del proveedor).

**Formato:** URLs públicas del sistema.  
**Fecha de entrega:** 20/12/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El sistema es accesible desde navegadores modernos sin errores de certificado.
- ✓ Los datos persisten entre reinicios del backend.
- ✓ Las imágenes se visualizan correctamente.
- ✓ El sistema se mantiene estable durante el período de presentación de la Entrega 1, con un nivel de disponibilidad acorde a los recursos del tier gratuito.

**Dependencias:** E1-DEV3.

---

#### E1-DOC1: README y Documentación de Instalación (Documentación – Must)

**Descripción:**  
Archivo README con instrucciones para clonar, configurar y ejecutar el proyecto localmente.

**Contenido mínimo:**
- Descripción breve del proyecto.
- Requisitos técnicos (Java, PostgreSQL, etc.).
- Instrucciones paso a paso:
    - Clonar repositorio.
    - Configurar variables de entorno.
    - Crear base de datos y habilitar PostGIS.
    - Ejecutar migraciones.
    - Levantar backend y frontend.
- Scripts básicos (build, test, run).
- Sección de troubleshooting con errores frecuentes.

**Formato:** README.md  
**Fecha de entrega:** 20/12/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Un desarrollador externo puede levantar el entorno siguiendo el README sin asistencia directa.
- ✓ Los comandos indicados funcionan (o se documentan variantes).
- ✓ Se incluyen soluciones a errores típicos de configuración.

**Dependencias:** E1-DEV3.

---

#### E1-DOC2: Documentación Swagger de APIs (Documentación – Must)

**Descripción:**  
Documentación automática de APIs generada por Springdoc OpenAPI y expuesta vía Swagger UI.

**Contenido mínimo:**
- Endpoints principales documentados.
- Parámetros de entrada y salida.
- Ejemplos de JSON de request/response.
- Códigos de estado HTTP más frecuentes.

**Formato:** Interfaz Swagger UI accesible vía navegador.  
**Fecha de entrega:** 20/12/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Swagger UI es accesible en el entorno desplegado.
- ✓ Todos los endpoints relevantes de la API aparecen documentados.
- ✓ Los ejemplos de request/response son coherentes con el comportamiento real.

**Dependencias:** E1-DEV1.

---

#### E1-VAL1: Reporte de Tests Unitarios (Cobertura inicial) (Validación – Should)

**Descripción:**  
Reporte de cobertura de tests unitarios de la lógica de negocio backend, generado con JaCoCo u otra herramienta.

**Contenido mínimo:**
- Reporte HTML de cobertura (global y por paquete/clase).
- Lista de clases y casos críticos testeados.
- Identificación de las áreas aún no cubiertas relevantemente.

**Formato:** Reporte HTML + breve resumen (Markdown).  
**Fecha de entrega:** 20/12/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Existe un conjunto de tests significativo sobre la lógica de negocio crítica (CRUD, validaciones).
- ✓ La cobertura global se ajusta a los objetivos de la Entrega 1 definidos en la sección de KPIs (≥70% como objetivo, pudiendo justificarse desvíos menores).
- ✓ Los tests se ejecutan correctamente en local y/o CI.

**Dependencias:** E1-DEV1 (API implementada).

---

#### E1-PRES1: Presentación y Demo Funcional E1 (Presentación – Must)

**Descripción:**  
Presentación del MVP Técnico al tribunal, acompañada de una demo en vivo.

**Contenido mínimo:**
- Presentación (10–15 diapositivas) con:
    - Contexto y problema.
    - Objetivos de la Entrega 1.
    - Arquitectura implementada.
    - Funcionalidades principales del MVP.
    - Próximos pasos hacia Entrega 2.
- Demo en vivo del flujo principal:
    - Crear persona desaparecida.
    - Crear avistamiento con geolocalización.
    - Visualizar avistamiento en mapa.

**Formato:** Presentación (PPT/PDF) + demo en sistema desplegado.  
**Fecha de entrega:** 20/12/2025  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El flujo principal se puede demostrar sin errores críticos.
- ✓ La presentación explica claramente el alcance logrado en E1.
- ✓ El tribunal puede ver, en tiempo real, el funcionamiento básico del sistema.

**Dependencias:** E1-INF1.

---

### 9.3 Entregables - Entrega 2 (Sistema Completo)

#### E2-DEV1: Módulo de Búsqueda Multiparámetro (Desarrollo – Must)

**Descripción:**  
Funcionalidad de búsqueda por DNI (exacto), nombre y apellido (búsqueda aproximada) sobre personas desaparecidas.

**Contenido mínimo:**
- Endpoint de búsqueda en la API.
- Búsqueda exacta por DNI.
- Búsqueda aproximada por nombre/apellido (fuzzy search).
- Componente de interfaz de búsqueda (frontend).
- Resultados con vista previa (foto y datos básicos).

**Formato:** Código backend + frontend.  
**Fecha de entrega:** 10/01/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ La búsqueda por DNI devuelve el registro correcto cuando existe.
- ✓ La búsqueda por nombre/apellido tolera errores ortográficos básicos y devuelve resultados relevantes.
- ✓ El tiempo de respuesta es consistente con los KPIs de performance definidos.
- ✓ La UI muestra resultados de manera clara y utilizable.

**Dependencias:** E1-INF1 (sistema E1 operativo).

---

#### E2-DEV2: Módulo de Perfiles Individuales con Timeline (Desarrollo – Should/Must alto)

**Descripción:**  
Vista de perfil de persona desaparecida que agrupa todos sus avistamientos en un mapa y una línea de tiempo.

**Contenido mínimo:**
- Endpoint de perfil individual en backend.
- Página /perfil/{dni} o equivalente en frontend.
- Mapa con todos los avistamientos de esa persona.
- Timeline ordenada cronológicamente.
- Interacción mapa–timeline (seleccionar un evento centra el mapa).
- Datos completos de la persona y del reportante original.

**Formato:** Código backend + frontend.  
**Fecha de entrega:** 25/01/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El perfil muestra todos los avistamientos asociados a la persona.
- ✓ La ordenación temporal es correcta y clara.
- ✓ La interacción entre timeline y mapa funciona de forma intuitiva.
- ✓ El tiempo de carga es razonable y acorde a los KPIs.

**Dependencias:** E2-DEV1 (para llegar al perfil desde la búsqueda).

---

#### E2-DEV3: Diferenciación Visual y Filtros de Fecha (Desarrollo – Should)

**Descripción:**  
Código de colores y filtros temporales para distinguir visualmente casos recientes de casos antiguos.

**Contenido mínimo:**
- Cálculo de antigüedad del avistamiento en el backend o frontend.
- Marcadores con colores distintos según rango temporal (por ejemplo, últimos 30 días vs. anteriores).
- Leyenda explicativa en el mapa.
- Filtros por rango de fechas (date-picker o similar).

**Formato:** Principalmente frontend (con apoyo de backend si se requiere).  
**Fecha de entrega:** 25/01/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Los avistamientos recientes se distinguen claramente en el mapa.
- ✓ La leyenda explica de manera simple los códigos de color.
- ✓ El filtro por fecha permite acotar la visualización de avistamientos.

**Dependencias:** E1-INF1 (mapa básico ya implementado).

---

#### E2-OPT1: Optimizaciones de Performance (Clustering) (Desarrollo/Infra – Should)

**Descripción:**  
Optimización del mapa para soportar una cantidad creciente de avistamientos mediante clustering y otras técnicas.

**Contenido mínimo:**
- Integración de clustering de marcadores (por ejemplo, Leaflet.markercluster o equivalente).
- Configuración de comportamiento según nivel de zoom.
- Ajustes de consultas y paginación para no sobrecargar al cliente.
- Revisión de índices en la base de datos.

**Formato:** Código backend + frontend.  
**Fecha de entrega:** 01/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El mapa puede representar al menos ~1000 avistamientos de forma utilizable, sin degradación severa de la experiencia de uso.
- ✓ El clustering agrupa/desagrupa puntos de manera lógica según el zoom.
- ✓ La performance global del mapa está alineada con los KPIs de performance del Anexo.

**Dependencias:** E2-DEV2 (perfiles con múltiples avistamientos).

---

#### E2-INT1: Términos, Privacidad y Moderación Básica (Desarrollo/Documentación – Must)

**Descripción:**  
Implementación de términos y condiciones, política de privacidad y un mecanismo básico de moderación de contenido.

**Contenido mínimo:**
- Página de Términos y Condiciones.
- Página de Política de Privacidad (alineada con Ley 25.326 a nivel básico).
- Aceptación de términos (checkbox) en el registro o primer uso.
- Botón "Reportar contenido" en las fichas de avistamiento.
- Mecanismo simple para ocultar/gestionar contenido reportado.

**Formato:** Código frontend + backend + textos legales en formato web.  
**Fecha de entrega:** 05/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Términos y Política están accesibles desde la interfaz.
- ✓ El usuario debe aceptar términos para avanzar (cuando aplique).
- ✓ Es posible reportar contenido y marcarlo para revisión u ocultamiento manual.

**Dependencias:** Ninguna crítica (puede evolucionar en paralelo).

---

#### E2-VAL1: Suite de Tests Completa (Cobertura avanzada) (Validación – Should)

**Descripción:**  
Conjunto ampliado de tests unitarios, de integración y, en lo posible, E2E para los flujos críticos del sistema.

**Contenido mínimo:**
- Tests unitarios que cubran la mayor parte de la lógica de negocio.
- Tests de integración sobre la API y base de datos.
- Tests E2E para los flujos principales (opcional, según tiempo).
- Reporte de cobertura actualizado (JaCoCo u otro).

**Formato:** Código de tests + reporte HTML.  
**Fecha de entrega:** 05/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Se alcanza una cobertura global de código acorde a los objetivos de la Entrega 2 (≈80% como meta, con justificación de exclusiones si las hubiera).
- ✓ Los flujos críticos (CRUD, búsqueda, carga de avistamientos) están cubiertos por tests.
- ✓ La suite de tests se integra al flujo de CI (local o GitHub Actions).

**Dependencias:** E2-DEV1, E2-DEV2 y resto de módulos funcionales.

---

#### E2-UAT1: Reporte de UAT con Usuarios Reales (Validación – Should)

**Descripción:**  
Documento que resume las pruebas de aceptación realizadas con un pequeño grupo de usuarios externos.

**Contenido mínimo:**
- Breve perfil de los participantes (3+ usuarios).
- Tareas diseñadas para la prueba (al menos 5 por usuario).
- Resultados (tasa de completitud, dificultades encontradas).
- Satisfacción general (encuesta simple).
- Principales problemas detectados y ajustes implementados.
- Aprendizajes clave.

**Formato:** PDF o Markdown.  
**Fecha de entrega:** 15/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Se realizaron pruebas con al menos 3 usuarios.
- ✓ Se documentaron las tareas y tasas de completitud.
- ✓ Se midió la satisfacción utilizando una escala simple (por ejemplo, 1–10).
- ✓ Se registraron e implementaron (o planificaron) los cambios críticos sugeridos.

**Dependencias:** E2-OPT1, E2-INT1 (sistema ya casi completo).

---

#### E2-DOC1: Manual de Usuario Final (Documentación – Should)

**Descripción:**  
Guía para personas no técnicas sobre cómo usar Find Me.

**Contenido mínimo:**
- Descripción del propósito del sistema.
- Guías paso a paso (con capturas) para:
    - Registrar persona desaparecida.
    - Reportar avistamiento.
    - Usar el mapa y filtros básicos.
    - Buscar personas por DNI, nombre, apellido.
- Preguntas frecuentes (FAQ).

**Formato:** PDF o sección /ayuda en el propio sistema.  
**Fecha de entrega:** 15/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El contenido es comprensible para usuarios no técnicos.
- ✓ Cubre los flujos principales definidos en el sistema.
- ✓ Incluye capturas o ejemplos visuales.

**Dependencias:** E2-UAT1 (la experiencia real de usuarios puede influir en el contenido).

---

#### E2-DOC2: Guía de Deployment (Documentación – Should)

**Descripción:**  
Instrucciones técnicas para desplegar el sistema en producción (o replicar el entorno actual).

**Contenido mínimo:**
- Requisitos de infraestructura.
- Pasos para desplegar backend y frontend.
- Configuración de PostgreSQL + PostGIS.
- Configuración de proveedor de imágenes.
- Variables de entorno requeridas.
- Indicaciones para configurar dominio y HTTPS.
- Sugerencias de monitoreo básico y rollback.

**Formato:** Markdown o PDF.  
**Fecha de entrega:** 15/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Un tercero con conocimientos técnicos básicos puede replicar el entorno de producción.
- ✓ Los pasos son claros, ordenados y completos.
- ✓ Se incluyen recomendaciones ante errores frecuentes.

**Dependencias:** E1-INF1 (primer deploy ya realizado).

---

#### E2-DOC3: Plan de Difusión y Adopción (Documentación – Could Have / Deseable)

**Descripción:**  
Documento estratégico para difundir y promover la adopción de Find Me por ONGs, familias e instituciones.

**Contenido mínimo (adaptable según tiempo):**
- Públicos objetivo.
- Propuesta de valor para cada público.
- Canales de difusión sugeridos (redes, ONGs, medios).
- Ideas de materiales de comunicación (posts, flyers, etc.).
- Métricas básicas de éxito (ejemplo: cantidad de usuarios activos, casos cargados).

**Formato:** Markdown o PDF.  
**Fecha de entrega:** 15/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El plan presenta al menos una propuesta concreta de aproximación a ONGs o instituciones.
- ✓ Define públicamente a quién se quiere llegar y con qué mensaje.
- ✓ Es realista en función de los recursos disponibles.

**Nota:** Este entregable es deseable (Could Have). Puede simplificarse su extensión o nivel de detalle si el tiempo es limitado, sin afectar los criterios de aprobación académica.

**Dependencias:** E2-DOC1 (manual de usuario como insumo de comunicación).

---

#### E2-DOC4: Lecciones Aprendidas (Documentación – Could Have / Deseable)

**Descripción:**  
Retrospectiva del proyecto, tanto en lo técnico como en lo de gestión.

**Contenido mínimo (adaptable):**
- Qué funcionó bien (decisiones acertadas).
- Qué se haría distinto en un proyecto futuro.
- Comentario sobre decisiones clave:
    - Clean Architecture.
    - Uso de PostGIS.
    - Leaflet y mapa.
- Desafíos enfrentados y cómo se resolvieron.
- Próximos pasos sugeridos para una fase futura.

**Formato:** Markdown o PDF.  
**Fecha de entrega:** 20/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ Contiene reflexiones concretas y no meramente formales.
- ✓ Destaca aprendizajes técnicos y de gestión.
- ✓ Identifica al menos 3 mejoras claras para una siguiente iteración.

**Nota:** Este entregable también es deseable (Could Have). Su profundidad puede ajustarse según la disponibilidad de tiempo.

**Dependencias:** Todos los entregables previos (es una síntesis final).

---

#### E2-INF1: Sistema Completo en Producción (Infraestructura – Must)

**Descripción:**  
Sistema final desplegado con todas las funcionalidades previstas para la Entrega 2.

**Contenido mínimo:**
- Todas las funcionalidades de E1 operativas.
- Búsqueda multiparámetro operativa.
- Perfiles individuales con timeline.
- Diferenciación visual de casos recientes.
- Clustering y optimizaciones de performance.
- Términos, privacidad y moderación funcionando.
- Entorno estable y monitorizado a nivel básico.

**Formato:** URLs públicas + sistema funcional.  
**Fecha de entrega:** 20/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El sistema ofrece el conjunto de funcionalidades definidas para E2.
- ✓ No se presentan errores críticos en los principales flujos de uso.
- ✓ La performance y estabilidad son razonables para un entorno académico y de tier gratuito.

**Dependencias:** Todos los entregables de desarrollo de E2.

---

#### E2-PRES1: Presentación Final y Demo Completa (Presentación – Must)

**Descripción:**  
Presentación de cierre del proyecto Find Me (E1+E2) al tribunal académico.

**Contenido mínimo:**
- Presentación (20–30 diapositivas aprox.) con:
    - Resumen del problema y justificación.
    - Evolución desde E1 (MVP) a E2 (sistema completo).
    - Arquitectura final.
    - Funcionalidades clave.
    - Resultados de UAT.
    - KPIs alcanzados (resumen).
    - Lecciones aprendidas (si se documentaron).
    - Próximos pasos.
- Demo en vivo que muestre:
    - CRUD y visualización en mapa.
    - Búsqueda multiparámetro.
    - Perfil individual con timeline.
    - Diferenciación visual.
    - Comportamiento con muchos marcadores (si aplica).

**Formato:** PPT/PDF + demo en sistema desplegado.  
**Fecha de entrega:** 20/02/2026  
**Responsable:** Juan Pablo Verdondoni

**Criterios de aceptación:**
- ✓ El tribunal puede ver claramente el valor del sistema.
- ✓ La demo recorre los flujos más importantes sin errores críticos.
- ✓ Se evidencia el cumplimiento de los objetivos planteados en el Acta de Constitución.

**Dependencias:** E2-INF1 (sistema completo operativo).

---

### 9.4 Resumen de Entregables por Tipo

#### Entregables de Documentación

| ID | Nombre | Entrega | Fecha |
|----|--------|---------|-------|
| E1-D1 | Documento de Arquitectura | E1 | 28/02/2025 |
| E1-D2 | Prototipo de UI | E1 | 15/03/2025 |
| E1-DOC1 | README | E1 | 20/12/2025 |
| E1-DOC2 | Swagger APIs | E1 | 20/12/2025 |
| E2-DOC1 | Manual de Usuario | E2 | 15/02/2026 |
| E2-DOC2 | Guía de Deployment | E2 | 15/02/2026 |
| E2-DOC3 | Plan de Difusión (Could) | E2 | 15/02/2026 |
| E2-DOC4 | Lecciones Aprendidas (Could) | E2 | 20/02/2026 |

**Total Documentación: 8 entregables (2 de ellos deseables).**

---

#### Entregables de Desarrollo

| ID | Nombre | Entrega | Fecha |
|----|--------|---------|-------|
| E1-DEV1 | API Backend v1.0 | E1 | 30/06/2025 |
| E1-DEV2 | Frontend v1.0 | E1 | 30/09/2025 |
| E1-DEV3 | CRUD Completo Integrado | E1 | 30/11/2025 |
| E2-DEV1 | Búsqueda Multiparámetro | E2 | 10/01/2026 |
| E2-DEV2 | Perfiles Individuales | E2 | 25/01/2026 |
| E2-DEV3 | Diferenciación Visual | E2 | 25/01/2026 |
| E2-OPT1 | Optimizaciones de Performance | E2 | 01/02/2026 |
| E2-INT1 | Términos y Moderación | E2 | 05/02/2026 |

**Total Desarrollo: 8 entregables.**

---

#### Entregables de Infraestructura

| ID | Nombre | Entrega | Fecha |
|----|--------|---------|-------|
| E1-INF1 | Sistema Desplegado (MVP) | E1 | 20/12/2025 |
| E2-INF1 | Sistema Completo en Producción | E2 | 20/02/2026 |

**Total Infraestructura: 2 entregables.**

---

#### Entregables de Validación

| ID | Nombre | Entrega | Fecha |
|----|--------|---------|-------|
| E1-VAL1 | Reporte Tests (Cobertura inicial) | E1 | 20/12/2025 |
| E2-VAL1 | Suite Tests Completa | E2 | 05/02/2026 |
| E2-UAT1 | Reporte de UAT | E2 | 15/02/2026 |

**Total Validación: 3 entregables.**

---

#### Entregables de Presentación

| ID | Nombre | Entrega | Fecha |
|----|--------|---------|-------|
| E1-PRES1 | Presentación y Demo E1 | E1 | 20/12/2025 |
| E2-PRES1 | Presentación Final | E2 | 20/02/2026 |

**Total Presentación: 2 entregables.**

---

### 9.5 Total de Entregables

| Categoría | E1 | E2 | Total |
|-----------|----|----|-------|
| Documentación | 4 | 4 | 8 |
| Desarrollo | 3 | 5 | 8 |
| Infraestructura | 1 | 1 | 2 |
| Validación | 1 | 2 | 3 |
| Presentación | 1 | 1 | 2 |
| **TOTAL** | **10** | **13** | **23** |

**Con la salvedad de que E2-DOC3 y E2-DOC4 son Could Have (deseables).**

---

### 9.6 Matriz de Dependencias entre Entregables

```
Flujo de Dependencias Entrega 1:

E1-D1 (Arquitectura)
    ↓
E1-D2 (Mockups) + E1-DEV1 (Backend)
    ↓
E1-DEV2 (Frontend)
    ↓
E1-DEV3 (CRUD Completo)
    ↓
E1-VAL1 (Tests iniciales) + E1-DOC1 (README) + E1-DOC2 (Swagger)
    ↓
E1-INF1 (Deploy MVP)
    ↓
E1-PRES1 (Demo E1)

---

Flujo de Dependencias Entrega 2:

E1-INF1 (Sistema E1 operativo)
    ↓
E2-DEV1 (Búsqueda) + E2-INT1 (Términos/Moderación)
    ↓
E2-DEV2 (Perfiles) + E2-DEV3 (Diferenciación Visual)
    ↓
E2-OPT1 (Optimizaciones)
    ↓
E2-VAL1 (Tests avanzados)
    ↓
E2-UAT1 (UAT con usuarios)
    ↓
E2-DOC1 (Manual) + E2-DOC2 (Deployment) + E2-DOC3 (Difusión - Could)
    ↓
E2-INF1 (Sistema Completo)
    ↓
E2-DOC4 (Lecciones Aprendidas - Could)
    ↓
E2-PRES1 (Demo Final)
```

---

### 9.7 Criterios de Calidad Transversales

Para todos los entregables:

**Documentos:**
- ✓ Buena ortografía y redacción.
- ✓ Estructura clara con títulos y subtítulos.
- ✓ Versionados en Git cuando corresponda.

**Código:**
- ✓ Estilo consistente.
- ✓ Nombres de variables y métodos significativos.
- ✓ Sin "dead code" comentado de manera permanente.
- ✓ Sin warnings críticos conocidos.

**Sistemas desplegados:**
- ✓ URLs funcionales durante los períodos clave (entregas y defensas).
- ✓ HTTPS habilitado (cuando el proveedor lo permita).
- ✓ Tiempos de respuesta razonables, alineados con los KPIs definidos.

---

## 10. BENEFICIOS ESPERADOS

Los beneficios esperados del proyecto Find Me se clasifican en tres dimensiones:
1) beneficios cuantitativos (medibles),
2) beneficios cualitativos (intangibles pero de alto valor) y
3) beneficios específicos por stakeholder.

Se basan en la justificación del proyecto y en el análisis del ecosistema actual de búsqueda de personas desaparecidas.

---

### 10.1 Beneficios Cuantitativos (Medibles)

Estos beneficios pueden expresarse con métricas concretas y verificables.

#### 10.1.1 Reducción del Tiempo de Reporte de Avistamientos

**Situación actual:**
- Llamada telefónica a líneas de emergencia: 5–10 minutos promedio.
- Dependencia de un operador para transcribir la información.
- Sin geolocalización automática ni fotografía adjunta.
- Riesgo de pérdida o distorsión de información en la transcripción.

**Con Find Me:**
- Reporte de avistamiento completo: < 2 minutos.
- Geolocalización automática (GPS).
- Fotografía adjunta inmediata.
- Datos estructurados sin intermediación humana en la carga.

**Beneficio cuantificable:**
- **Reducción del tiempo de reporte del orden del 60–80 %** (de 5–10 min → < 2 min).
- Por cada 100 avistamientos cargados en la plataforma, se ahorran aproximadamente **6,5–13 horas** respecto de canales tradicionales basados en llamadas telefónicas.

---

#### 10.1.2 Centralización de Información Dispersa

**Situación actual:**
- Información dispersa en múltiples canales (WhatsApp, Instagram, Facebook, llamadas telefónicas, correos electrónicos, etc.).
- Missing Children Argentina y otras organizaciones reciben reportes por varios canales en paralelo.
- Dificultad para construir líneas temporales coherentes.
- Imposibilidad de generar mapas de avistamientos en tiempo real a partir de esos canales.

**Con Find Me:**
- Plataforma única y centralizada para la carga de avistamientos.
- Todos los avistamientos quedan geolocalizados en un mapa interactivo.
- Timeline automática de avistamientos por persona.
- Datos estructurados y consultables de forma inmediata.

**Beneficio cuantificable:**
- **Reducción de múltiples canales no estructurados → una plataforma unificada**.
- **100 % de avistamientos cargados con geolocalización precisa** y timestamp automático.
- **Disponibilidad inmediata de la información** dentro del sistema (sin depender de transcripciones posteriores).

---

#### 10.1.3 Mejora Potencial en la Tasa de Resolución de Casos

**Base de referencia (evidencia internacional):**
- Entre el 80–90 % de los casos se resuelven en las primeras 48 horas.
- Cada hora perdida reduce la probabilidad de hallazgo.
- La información fresca (última ubicación, vestimenta, acompañantes, dirección de desplazamiento) es crítica.

**Hipótesis de impacto de Find Me:**
Si Find Me contribuye a mejorar, aunque sea de forma **modesta**, la eficacia de la búsqueda en las primeras 48 horas (por ejemplo, del orden de un 5 % adicional sobre el universo de ~10.000 casos anuales en Argentina), el impacto potencial se ubica en el **orden de cientos de personas adicionales encontradas por año**.

**Nota metodológica:**  
Se trata de estimaciones ilustrativas para dimensionar el orden de magnitud del impacto. La validación real dependerá de datos generados por el sistema una vez operativo y de estudios comparativos de tasas de hallazgo.

---

#### 10.1.4 Optimización de Recursos de Búsqueda

**Situación actual:**
- Fuerzas de seguridad realizan patrullajes con información geográfica parcial o no estructurada.
- Búsqueda manual y fragmentada en comisarías, hospitales, redes sociales, etc.
- Familias que deben recorrer físicamente hospitales, comisarías y morgues (como ocurrió en Cromañón).

**Con Find Me:**
- Mapas de calor que indican zonas de concentración de avistamientos.
- Timeline que permite reconstruir trayectorias probables.
- Optimización de patrullajes basada en datos geoespaciales y temporales.
- Reducción de búsquedas redundantes y desplazamientos innecesarios.

**Beneficio cuantificable (a nivel operativo):**
- **Reducción de horas/hombre de búsqueda manual en fuentes dispersas.**
- **Priorización inteligente de áreas de patrullaje** según densidad de avistamientos.
- **Menor necesidad de traslados físicos** de familiares para obtener información básica.

---

#### 10.1.5 Velocidad de Búsqueda en Fichero Digital

**Situación actual:**
- Consultas telefónicas a líneas específicas (como 142) o comisarías, con tiempos variables (5–30 minutos).
- Búsqueda manual en bases de datos y planillas.
- Dependencia de horarios de atención y de disponibilidad de operadores.

**Con Find Me:**
- Búsqueda por DNI: < 1 segundo.
- Búsqueda por nombre/apellido (fuzzy search): < 3 segundos.
- Acceso 24/7, sin intermediarios humanos para la consulta.

**Beneficio cuantificable:**
- **Reducción de tiempo de consulta del orden del 95 % o más** (de 5–30 minutos → segundos).
- **Disponibilidad permanente** de la información, independientemente de horarios o guardias.

---

### 10.2 Beneficios Cualitativos (Intangibles)

Son beneficios que no siempre pueden expresarse numéricamente, pero tienen alto valor social y emocional.

#### 10.2.1 Para Familias de Personas Desaparecidas

**Reducción de ansiedad y angustia:**
- Acceso inmediato a información centralizada sobre avistamientos.
- Visibilidad en tiempo real del estado de la búsqueda.
- Posibilidad de compartir el perfil en redes sociales mediante un link único.
- Menor necesidad de recorrer físicamente múltiples instituciones para obtener actualizaciones.

**Empoderamiento:**
- Capacidad de tomar acción inmediata sin depender exclusivamente de las autoridades.
- Participación activa en la búsqueda (carga, consulta y difusión de información).
- Sensación de “estar haciendo algo” en un contexto de alta incertidumbre.

**Transparencia:**
- Trazabilidad completa de los avistamientos reportados.
- Menor dependencia de llamados reiterados a líneas de emergencia para obtener novedades.

---

#### 10.2.2 Para ONGs y Organizaciones de la Sociedad Civil

**Profesionalización de la gestión de casos:**
- Herramienta unificada en lugar de mensajes dispersos en múltiples plataformas.
- Datos estructurados para generar reportes y estadísticas.
- Facilitación de la coordinación entre distintas organizaciones.

**Escalabilidad:**
- Capacidad de gestionar decenas o cientos de casos en paralelo.
- Reducción de la carga operativa asociada a transcripción manual de mensajes.
- Liberación de recursos humanos para tareas de mayor valor (contención, estrategia de difusión).

**Evidencia y rendición de cuentas:**
- Registro permanente de los avistamientos con fecha y hora.
- Soporte para elaborar informes a donantes y autoridades.
- Base de datos para análisis y mejora continua de protocolos.

---

#### 10.2.3 Para Autoridades y Fuerzas de Seguridad

**Inteligencia operativa:**
- Vista geográfica en tiempo real para optimizar despliegue de patrullajes.
- Identificación de patrones espaciales y temporales.
- Datos estructurados (latitud/longitud, timestamp, foto) que complementan la información verbal.

**Interoperabilidad potencial:**
- Endpoints preparados para integración futura con SIFEBU y otros sistemas.
- Posibilidad de incorporar datos ciudadanos validados a los sistemas institucionales.

**Reducción de carga administrativa:**
- Menos llamadas redundantes a líneas de emergencia.
- Información ya estructurada y consolidada por caso y por persona.
- Trazabilidad de quién reportó qué, cuándo y desde dónde.

---

#### 10.2.4 Para la Sociedad en General

**Participación ciudadana efectiva:**
- Herramienta concreta para colaborar en búsquedas de forma simple y rápida.
- Reducción de la sensación de impotencia frente a casos de desaparición.
- Potencial para generar redes de solidaridad basadas en tecnología.

**Conciencia social:**
- Visibilización de la magnitud del problema (miles de casos/año).
- Desmitificación del “mito de las 24 horas” a través de la práctica y la comunicación.
- Promoción del reporte temprano y la denuncia inmediata.

**Datos para políticas públicas:**
- Generación de estadísticas desagregadas por zona, edad, género, horario, etc.
- Base empírica para diseñar políticas de prevención y búsqueda.

**Prevención en emergencias masivas:**
- En escenarios como Cromañón o la tragedia de Once, Find Me podría funcionar como centro unificado de información, cruzando datos de hospitales, testigos y autoridades en tiempo casi real.

---

### 10.3 Beneficios por Stakeholder

Resumen de beneficios específicos para cada grupo de interés:

| Stakeholder | Beneficios Principales | Métricas de Éxito (orientativas) |
|-------------|------------------------|----------------------------------|
| **Familias de Personas Desaparecidas** | • Información centralizada en tiempo real<br>• Reporte de avistamiento en < 2 min<br>• Posibilidad de compartir perfiles por link | • Tiempo de reporte<br>• Resultados de encuestas de satisfacción (UAT) |
| **ONGs (Missing Children, etc.)** | • Gestión estructurada de casos<br>• Menor carga de transcripción manual<br>• Mapa de avistamientos en tiempo real | • Horas dedicadas a tareas administrativas vs. tareas de valor<br>• Uso de la plataforma en sus flujos de trabajo |
| **Autoridades y Fuerzas de Seguridad** | • Vista geográfica para optimizar patrullajes<br>• Datos estructurados y trazables<br>• Potencial integración con SIFEBU | • Uso de la información geoespacial en operativos<br>• Integraciones exploradas o implementadas |
| **Ciudadanos (Avistadores)** | • Participación activa en búsquedas<br>• Proceso simple y rápido<br>• No requerir pasos complejos para colaborar | • Tiempo promedio de carga de un reporte<br>• Cantidad de avistamientos reportados |
| **Investigadores y Academia** | • Datos para investigación<br>• Base empírica para modelos probabilísticos y políticas públicas | • Existencia de datasets anonimizados<br>• Estudios o trabajos académicos derivados |
| **Sociedad Argentina en General** | • Potencial reducción de casos sin resolver<br>• Desmitificación del “mito de las 24 horas”<br>• Mayor conciencia sobre la problemática | • Cobertura y adopción institucional<br>• Incremento en reportes tempranos |

---

### 10.4 Impacto Social Estimado

El proyecto Find Me presenta un desbalance muy favorable entre la inversión requerida y el impacto potencial.

**Inversión del proyecto:**
- Costo monetario directo: **$0 ARS**, utilizando tiers gratuitos de servicios cloud.
- Esfuerzo estimado: **150 horas** de desarrollo distribuidas entre las dos entregas.
- Recursos humanos: 1 desarrollador, en el marco de un trabajo final académico.

**Impacto social potencial (escenario ilustrativo):**
- Operando a escala nacional, sobre un universo de aproximadamente 10.000 casos anuales, incluso una mejora **modesta** en la eficacia durante las primeras 48 horas (por ejemplo, un incremento del orden del 5 % en la tasa de resolución en esa ventana crítica) podría traducirse en **cientos de casos adicionales resueltos por año**.
- La centralización de avistamientos y la generación de datos geoespaciales estructurados permitirían optimizar recursos de búsqueda, reducir recorridos físicos innecesarios por parte de las familias y mejorar la coordinación entre ONGs y autoridades.

Estos valores no deben leerse como una proyección exacta, sino como una forma de dimensionar el **orden de magnitud** del beneficio potencial: una inversión acotada de tiempo y recursos tecnológicos puede traducirse en un impacto social significativamente desproporcionado.

---

### 10.5 Beneficios Adicionales No Cuantificados

Además de las métricas específicas, Find Me genera valor en varias dimensiones difíciles de cuantificar:

#### 10.5.1 Innovación Tecnológica para el Bien Social
- Demuestra la viabilidad de usar tecnología geoespacial para problemáticas sociales críticas.
- Puede inspirar otros proyectos en áreas como violencia de género, catástrofes naturales, personas mayores extraviadas, etc.

#### 10.5.2 Generación de Conocimiento
- Produce datos inéditos sobre patrones espaciales y temporales de desapariciones en Argentina.
- Permite ajustar modelos probabilísticos internacionales a la realidad local.

#### 10.5.3 Construcción de Confianza Institucional
- Aumenta la transparencia en el manejo de información sobre desaparecidos.
- Facilita la colaboración entre Estado, ONGs y ciudadanía mediante una herramienta compartida.

#### 10.5.4 Escalabilidad Regional
- El modelo es replicable en otros países de América Latina con problemáticas similares.
- Podría contribuir a una red regional de sistemas de búsqueda interconectados.

---

### 10.6 Condiciones y Limitaciones de los Beneficios

La concreción de los beneficios anteriores depende de ciertas condiciones y enfrenta algunos riesgos, coherentes con la matriz de riesgos incluida en la Sección 5 del Anexo.

#### 10.6.1 Condiciones para la Realización de Beneficios

- **Adopción mínima crítica:** el sistema necesita alcanzar cierto volumen de usuarios y casos cargados para que la información sea densa y útil.
- **Respaldo de organizaciones:** el apoyo de ONGs y, eventualmente, de organismos públicos potencia su credibilidad y adopción.
- **Calidad de datos:** los beneficios dependen de la calidad y veracidad de los avistamientos reportados, por lo que son clave los mecanismos de moderación y validación.

#### 10.6.2 Riesgos que Podrían Reducir Beneficios

- **Mal uso del sistema:** reportes falsos o malintencionados, uso indebido de datos personales.
- **Baja adopción:** si la plataforma no logra masa crítica de usuarios, su impacto se limita.
- **Limitaciones técnicas:** problemas de performance o dependencias de servicios externos pueden afectar la experiencia de uso.

#### 10.6.3 Medición de Beneficios en una Fase Posterior

En una fase posterior a este trabajo final, podrían definirse y monitorearse métricas como:

- Métricas de adopción (usuarios activos, casos cargados, avistamientos reportados).
- Métricas de impacto (casos resueltos en los que se utilizó la plataforma, tiempos promedio de respuesta).
- Métricas de escalamiento (ONGs que la adoptan, municipios o provincias integradas, alcance en medios y redes sociales).

El presente proyecto se limita a establecer el **diseño y la implementación técnica base**, dejando la medición empírica para una etapa futura de despliegue real.

---

### 10.7 Resumen Ejecutivo de Beneficios

En síntesis, Find Me ofrece:

✅ **Beneficios inmediatos (a nivel funcional):**
- Disminución significativa del tiempo necesario para reportar un avistamiento.
- Centralización de información que hoy se encuentra dispersa en múltiples canales.
- Búsqueda rápida de personas desaparecidas mediante un fichero digital accesible 24/7.

✅ **Beneficios a corto plazo (primer año de uso real):**
- Mejora en la organización y visualización de los casos activos.
- Reducción de la carga operativa para ONGs y equipos de búsqueda.
- Mayor empoderamiento y contención informativa para las familias.

✅ **Beneficios a mediano plazo (2–3 años):**
- Generación de datos estructurados para políticas públicas de prevención y búsqueda.
- Potencial integración con sistemas federales (por ejemplo, SIFEBU) y registros provinciales.
- Adopción institucional por ONGs, municipios y otras entidades.

✅ **Beneficios a largo plazo (5+ años):**
- Contribución a instalar la idea de que no es necesario “esperar 24 horas” para actuar.
- Replicabilidad del modelo en otros contextos y países de la región.
- Consolidación de un estándar tecnológico abierto para el reporte ciudadano de avistamientos.

---

## 11. HITOS DEL PROYECTO

Los hitos representan puntos de control clave en el cronograma del proyecto, donde se verifica el cumplimiento de objetivos específicos y se toman decisiones estratégicas. El proyecto Find Me define **13 hitos principales** distribuidos a lo largo de las dos entregas académicas (E1 y E2), de los cuales **11 conforman la ruta crítica** del proyecto.

---

### 11.1 Definición y Función de los Hitos

Un **hito** (o *milestone*) es un punto de verificación que:
- Marca la finalización de una fase o actividad crítica.
- Permite evaluar el avance del proyecto respecto del cronograma.
- Facilita la identificación temprana de retrasos o riesgos.
- No tiene duración propia (son instantes en el tiempo, no tareas).

En Find Me, los hitos están alineados con:
- Los entregables definidos en la Sección 9.
- Las fases del diagrama de Gantt (disponible en GANTT_PROYECTO_FINDME.md).
- Los objetivos específicos de la Sección 3.
- Las fechas de entrega académicas (20/12/2025 para E1 y 20/02/2026 para E2).

**Nota sobre "fases" y "entregas":**  
Las fases que se mencionan en esta sección son agrupaciones lógicas de actividades (Planificación, Backend E1, Frontend E1, etc.) y no sustituyen las Entregas Académicas E1 y E2, que siguen siendo los hitos formales de evaluación ante el tribunal.

---

### 11.2 Tabla Completa de Hitos del Proyecto

A continuación se presenta la tabla consolidada de los 13 hitos, con sus atributos principales:

| ID | Nombre del Hito | Fecha Objetivo | Fase | Entregables Asociados | Ruta Crítica |
|----|-----------------|----------------|------|----------------------|--------------|
| **H1** | Arquitectura Definida | 28/02/2025 | Fase 0: Planificación | E1-D1 | ✅ |
| **H2** | UI/UX Diseñada | 15/03/2025 | Fase 0: Planificación | E1-D2 | ❌ |
| **H3** | Backend MVP Operativo | 30/06/2025 | Fase 1: Backend E1 | E1-DEV1 | ✅ |
| **H4** | Frontend MVP Integrado | 30/09/2025 | Fase 2: Frontend E1 | E1-DEV2 | ✅ |
| **H5** | Sistema E1 Completo | 30/11/2025 | Fase 3: Integración E1 | E1-DEV3 | ✅ |
| **H6** | Deploy Producción E1 | 20/12/2025 | Fase 4: Deploy E1 | E1-INF1 | ✅ |
| **H7** | Presentación E1 | 20/12/2025 | Fase 4: Deploy E1 | E1-PRES1, E1-DOC1, E1-DOC2, E1-VAL1 | ✅ |
| **H8** | Búsqueda Implementada | 10/01/2026 | Fase 5: Desarrollo E2 | E2-DEV1 | ✅ |
| **H9** | Perfiles con Timeline | 25/01/2026 | Fase 5: Desarrollo E2 | E2-DEV2, E2-DEV3 | ✅ |
| **H10** | Performance Optimizada | 01/02/2026 | Fase 6: Optimización E2 | E2-OPT1 | ❌ |
| **H11** | UAT Completada | 15/02/2026 | Fase 8: UAT y Entrega Final | E2-UAT1, E2-DOC1, E2-DOC2 | ✅ |
| **H12** | Deploy Producción E2 | 20/02/2026 | Fase 8: UAT y Entrega Final | E2-INF1 | ✅ |
| **H13** | Presentación Final | 20/02/2026 | Fase 8: UAT y Entrega Final | E2-PRES1, E2-DOC3, E2-DOC4 | ✅ |

**Aclaración sobre H9 y H10:**  
En H9 se incluye una versión funcional básica de la diferenciación visual (E2-DEV3) suficiente para operar el sistema y validar con usuarios.  
En H10 se consolida la optimización de performance y refinamiento visual, incluyendo clustering y mejoras de rendimiento, sobre la base funcional ya disponible en H9.

---

### 11.3 Descripción Detallada de Cada Hito

A continuación se detallan los 13 hitos con sus criterios de cumplimiento, impacto en caso de retraso y observaciones específicas.

---

#### H1: Arquitectura Definida
**Fecha objetivo:** 28/02/2025  
**Fase:** Fase 0 - Planificación  
**Entregables asociados:** E1-D1 (Documento de Arquitectura del Sistema)

**Criterios de cumplimiento:**
- ✅ Documento de arquitectura completo con diagramas de componentes, capas y modelo de datos.
- ✅ Decisiones arquitectónicas clave documentadas (Clean Architecture, PostGIS, Leaflet, JWT).
- ✅ Stack tecnológico definido y justificado.
- ✅ Modelo entidad-relación (ERD) preliminar validado.

**Impacto si se retrasa:**  
Impacto crítico: retrasa directamente el inicio del desarrollo backend (H3). Mayor riesgo de decisiones arquitectónicas apresuradas y deuda técnica.

**Mitigación:**  
Mantener este hito en ruta crítica con prioridad alta. Ante cualquier retraso, reducir alcance de funcionalidades "Could Have" de E2 antes que recortar este hito.

**Margen de tolerancia:** Máximo 5 días de retraso sin impacto severo en H3.

---

#### H2: UI/UX Diseñada
**Fecha objetivo:** 15/03/2025  
**Fase:** Fase 0 - Planificación  
**Entregables asociados:** E1-D2 (Prototipo de UI - Mockups)

**Criterios de cumplimiento:**
- ✅ Mockups de pantallas principales (mapa, formularios, perfiles) en Figma o imágenes.
- ✅ Variantes responsive (mobile + desktop) diseñadas.
- ✅ Lineamientos de colores y tipografía definidos.

**Impacto si se retrasa:**  
Impacto medio: no bloquea el backend, pero puede retrasar el inicio del frontend (H4). Riesgo de retrabajo si el frontend avanza sin diseño claro.

**Mitigación:**  
Permitir que los mockups se sigan refinando en paralelo mientras avance el backend. Asegurar, como mínimo, wireframes funcionales antes de comenzar H4.

**Margen de tolerancia:** Hasta 10 días de retraso sin impacto crítico (NO está en ruta crítica).

---

#### H3: Backend MVP Operativo
**Fecha objetivo:** 30/06/2025  
**Fase:** Fase 1 - Backend E1  
**Entregables asociados:** E1-DEV1 (API Backend v1.0)

**Criterios de cumplimiento:**
- ✅ API RESTful con endpoints CRUD operativos para las 3 entidades principales.
- ✅ Autenticación JWT implementada.
- ✅ Base de datos PostgreSQL + PostGIS configurada.
- ✅ Documentación Swagger accesible.
- ✅ Primera base de tests unitarios.

**Impacto si se retrasa:**  
Impacto crítico: bloquea el desarrollo frontend (H4) y comprime toda la ventana de E1.

**Mitigación:**  
Priorizar endpoints Must Have y diferir endpoints no críticos a E2. Evitar agregar complejidad extra (features "nice to have") en esta fase.

**Margen de tolerancia:** Máximo 5 días de retraso sin comprometer H4.

---

#### H4: Frontend MVP Integrado
**Fecha objetivo:** 30/09/2025  
**Fase:** Fase 2 - Frontend E1  
**Entregables asociados:** E1-DEV2 (Frontend v1.0 con Mapa Interactivo)

**Criterios de cumplimiento:**
- ✅ Mapa Leaflet operativo con marcadores de avistamientos.
- ✅ Formularios de alta de persona desaparecida y avistamiento funcionales.
- ✅ Integración completa con la API backend.
- ✅ Diseño responsive básico (mobile + desktop).
- ✅ Integración con Cloudinary para imágenes vía URL.

**Impacto si se retrasa:**  
Impacto crítico: reduce el tiempo disponible para integración, testing y ajustes antes de H5 y H7.

**Mitigación:**  
Priorizar funcionalidad sobre estética: si hay retrasos, simplificar diseño visual, pero no recortar flujo funcional.

**Margen de tolerancia:** Máximo 5 días de retraso sin comprometer H5.

---

#### H5: Sistema E1 Completo
**Fecha objetivo:** 30/11/2025  
**Fase:** Fase 3 - Integración E1  
**Entregables asociados:** E1-DEV3 (Sistema CRUD Completo Integrado)

**Criterios de cumplimiento:**
- ✅ Flujo end-to-end operativo: crear avistador → crear persona desaparecida → crear avistamiento → visualizar en mapa.
- ✅ Validaciones básicas en cliente y servidor.
- ✅ Relaciones entre entidades persistiendo correctamente.
- ✅ Manejo de errores con mensajes claros para el usuario.

**Impacto si se retrasa:**  
Impacto crítico: estrecha el margen para deploy (H6) y preparación de presentación (H7).

**Mitigación:**  
Congelar alcance de E1 en Must Have si se detecta riesgo de atraso. Mover cualquier mejora cosmética o funcional no crítica a E2.

**Margen de tolerancia:** Máximo 3 días de retraso sin comprometer H6.

---

#### H6: Deploy Producción E1
**Fecha objetivo:** 20/12/2025  
**Fase:** Fase 4 - Deploy E1  
**Entregables asociados:** E1-INF1 (Sistema Desplegado en Producción - MVP)

**Criterios de cumplimiento:**
- ✅ Backend desplegado en Railway/Render (u otro) con URL pública.
- ✅ Frontend desplegado en Vercel/Netlify (u otro) con URL pública.
- ✅ Base de datos PostgreSQL + PostGIS operativa en entorno productivo.
- ✅ Certificado SSL activo (HTTPS).
- ✅ Sistema accesible y estable para la demo de E1.

**Impacto si se retrasa:**  
Impacto crítico: compromete la posibilidad de realizar una demo real en la fecha académica.

**Mitigación:**  
Ensayar el despliegue completo al menos 3 días antes de la fecha objetivo. Tener documentados planes de contingencia (rollback, uso de entorno alternativo).

**Margen de tolerancia:** Cero días (fecha académica inamovible).

---

#### H7: Presentación E1 (ENTREGA ACADÉMICA 1)
**Fecha objetivo:** 20/12/2025  
**Fase:** Fase 4 - Deploy E1  
**Entregables asociados:** E1-PRES1 (Presentación y Demo E1), E1-DOC1 (README), E1-DOC2 (Swagger), E1-VAL1 (Reporte Tests)

**Criterios de cumplimiento:**
- ✅ Presentación de 10–15 diapositivas preparada.
- ✅ Demo en vivo del flujo principal sin errores críticos.
- ✅ Documentación técnica básica disponible (README, Swagger).
- ✅ Reporte de tests con cobertura objetivo cercana o ≥70 %.
- ✅ Sistema desplegado y accesible para el tribunal.

**Impacto si no se cumple:**  
Impacto crítico: no aprobar la Entrega 1 compromete la continuidad de E2.

**Mitigación:**  
Ensayar la demo con antelación. Focalizar en el flujo estable más robusto, evitando mostrar partes inestables.

**Margen de tolerancia:** Cero días (fecha académica inamovible).

---

#### H8: Búsqueda Implementada
**Fecha objetivo:** 10/01/2026  
**Fase:** Fase 5 - Desarrollo E2  
**Entregables asociados:** E2-DEV1 (Módulo de Búsqueda Multiparámetro)

**Criterios de cumplimiento:**
- ✅ Búsqueda por DNI (exacta) operativa en < 1 segundo.
- ✅ Búsqueda por nombre/apellido (fuzzy search) operativa en < 3 segundos.
- ✅ Resultados con vista previa (foto y datos básicos).
- ✅ Componente de interfaz integrado al frontend.

**Impacto si se retrasa:**  
Impacto alto: comprime el tiempo disponible para desarrollar perfiles individuales (H9).

**Mitigación:**  
Si hay retrasos, priorizar búsqueda por DNI (exacta) y una primera versión simple de búsqueda por nombre/apellido, dejando refinamientos para más adelante.

**Margen de tolerancia:** Máximo 5 días de retraso sin comprometer H9.

---

#### H9: Perfiles con Timeline
**Fecha objetivo:** 25/01/2026  
**Fase:** Fase 5 - Desarrollo E2  
**Entregables asociados:** E2-DEV2 (Módulo de Perfiles Individuales), E2-DEV3 (Diferenciación Visual)

**Criterios de cumplimiento:**
- ✅ Vista de perfil individual con mapa personalizado que agregue todos los avistamientos de la persona.
- ✅ Timeline cronológica de avistamientos.
- ✅ Interacción mapa–timeline (seleccionar un evento centra el mapa).
- ✅ Versión funcional básica de diferenciación visual de casos recientes (últimos 30 días) vs. antiguos.
- ✅ Filtro por rango de fechas operativo.

**Nota sobre diferenciación visual en H9 y H10:**  
En H9 se exige una versión funcional básica de diferenciación visual suficiente para uso real y UAT.  
H10 se reserva para optimizar y refinar esa diferenciación en términos de performance y escalabilidad (clustering, carga masiva, etc.).

**Impacto si se retrasa:**  
Impacto alto: reduce la ventana para optimizaciones (H10) y para UAT (H11).

**Mitigación:**  
Simplificar la UI de la timeline (por ejemplo, lista simple en lugar de diseño complejo). Implementar códigos de color simples y claros, dejando refinamientos visuales para H10.

**Margen de tolerancia:** Máximo 5 días de retraso sin comprometer H11.

---

#### H10: Performance Optimizada
**Fecha objetivo:** 01/02/2026  
**Fase:** Fase 6 - Optimización E2  
**Entregables asociados:** E2-OPT1 (Optimizaciones de Performance - Clustering)

**Criterios de cumplimiento:**
- ✅ Clustering de marcadores implementado (por ejemplo, Leaflet.markercluster o equivalente).
- ✅ Mapa capaz de manejar 1000+ avistamientos sin degradación severa de la experiencia.
- ✅ Tiempo de carga inicial < 3 segundos.
- ✅ Lighthouse Performance Score ≥ 85.

**Impacto si se retrasa:**  
Impacto medio: no bloquea la entrega final, pero puede reducir la calidad percibida y afectar parcialmente la experiencia de UAT.

**Mitigación:**  
Priorizar clustering y tiempos de carga por sobre detalles cosméticos. Este hito NO está en ruta crítica: puede recortarse el nivel de refinamiento si otros hitos se retrasan.

**Margen de tolerancia:** Hasta 7 días de retraso sin impacto crítico (NO está en ruta crítica).

---

#### H11: UAT Completada
**Fecha objetivo:** 15/02/2026  
**Fase:** Fase 8 - UAT y Entrega Final  
**Entregables asociados:** E2-UAT1 (Reporte de UAT), E2-DOC1 (Manual de Usuario), E2-DOC2 (Guía de Deployment)

**Criterios de cumplimiento:**
- ✅ Pruebas con al menos 3 usuarios externos representativos completadas.
- ✅ Tasa de completitud de tareas ≥ 80 %.
- ✅ Satisfacción de usuarios ≥ 75 % (escala 1–10 → ≥ 7,5).
- ✅ Feedback documentado de forma estructurada.
- ✅ Ajustes críticos incorporados al sistema.
- ✅ Manual de usuario y guía de deployment actualizados conforme a la versión final.

**Impacto si se retrasa:**  
Impacto crítico: reduce tiempo para aplicar ajustes y estabilizar el sistema antes del deploy final y presentación.

**Mitigación:**  
Coordinar la UAT con al menos 2 semanas de anticipación. Si hay poco tiempo, priorizar ajustes críticos sobre mejoras cosméticas o documentación "Could Have".

**Margen de tolerancia:** Máximo 3 días de retraso sin comprometer H12.

---

#### H12: Deploy Producción E2
**Fecha objetivo:** 20/02/2026  
**Fase:** Fase 8 - UAT y Entrega Final  
**Entregables asociados:** E2-INF1 (Sistema Completo en Producción)

**Criterios de cumplimiento:**
- ✅ Sistema completo desplegado con todas las funcionalidades previstas para E2.
- ✅ Búsqueda, perfiles, diferenciación visual y clustering operativos.
- ✅ Términos, política de privacidad y moderación básica implementados.
- ✅ Sistema estable, sin errores críticos visibles en los flujos principales.
- ✅ Entorno listo para demo ante el tribunal.

**Impacto si se retrasa:**  
Impacto crítico: compromete la presentación final (H13) y la aprobación de la Entrega 2.

**Mitigación:**  
Ensayar el despliegue final completo al menos 5 días antes de la fecha de presentación. Mantener scripts y documentación de deploy al día.

**Margen de tolerancia:** Cero días (fecha académica inamovible).

---

#### H13: Presentación Final (ENTREGA ACADÉMICA 2)
**Fecha objetivo:** 20/02/2026  
**Fase:** Fase 8 - UAT y Entrega Final  
**Entregables asociados:** E2-PRES1 (Presentación Final), E2-DOC3 (Plan de Difusión - Could), E2-DOC4 (Lecciones Aprendidas - Could)

**Criterios de cumplimiento:**
- ✅ Presentación de 20–30 diapositivas que muestre la evolución E1 → E2.
- ✅ Demo completa de flujos principales (CRUD, mapa, búsqueda, perfiles, diferenciación visual, clustering) sin errores críticos.
- ✅ Resultados de UAT presentados y comentados.
- ✅ Evidencia de cumplimiento de los objetivos planteados en el Acta de Constitución.
- ✅ Sistema desplegado y accesible para el tribunal durante la defensa.

**Impacto si no se cumple:**  
Impacto crítico: no aprobar la Entrega 2 implica no completar el proyecto académico.

**Mitigación:**  
Priorización absoluta de la estabilidad del sistema y del guion de la demo. Ensayar la presentación y la demo en condiciones similares a la defensa real.

**Margen de tolerancia:** Cero días (fecha académica inamovible).

---

### 11.4 Ruta Crítica del Proyecto

La **ruta crítica** es la secuencia de hitos que, de retrasarse, impactan directamente la fecha de finalización del proyecto. Cualquier retraso en un hito de la ruta crítica retrasa automáticamente la fecha de entrega final.

**Ruta Crítica de Find Me (11 hitos):**

```
H1 (Arquitectura Definida)
  ↓
H3 (Backend MVP Operativo)
  ↓
H4 (Frontend MVP Integrado)
  ↓
H5 (Sistema E1 Completo)
  ↓
H6 (Deploy Producción E1)
  ↓
H7 (Presentación E1) ← ENTREGA ACADÉMICA 1
  ↓
H8 (Búsqueda Implementada)
  ↓
H9 (Perfiles con Timeline)
  ↓
H11 (UAT Completada)
  ↓
H12 (Deploy Producción E2)
  ↓
H13 (Presentación Final) ← ENTREGA ACADÉMICA 2
```

**Hitos fuera de la ruta crítica:**
- **H2 (UI/UX Diseñada):** Puede retrasarse hasta 10 días sin impacto directo en la fecha final.
- **H10 (Performance Optimizada):** Puede retrasarse hasta 7 días sin impacto directo en la fecha final.

**Margen total de tolerancia en ruta crítica:**
- **E1:** Máximo ~18 días de retraso distribuidos (promedio 3 días por hito crítico).
- **E2:** Máximo ~18 días de retraso distribuidos (promedio 3 días por hito crítico).
- **Total:** ~36 días de margen en todo el proyecto (sobre ~365 días ≈ 10 % de buffer).

**Justificación del margen:**  
Un margen global cercano al 10 % es coherente con proyectos académicos de desarrollo de software de mediana complejidad, donde existen riesgos técnicos (integraciones, despliegue, performance) y riesgos de disponibilidad de tiempo (carga académica, trabajo, etc.). Ese buffer permite absorber contingencias sin comprometer las fechas académicas inamovibles.

**Estrategia de gestión de ruta crítica:**
1. Monitoreo semanal del avance de los hitos en ruta crítica.
2. Alerta temprana si un hito crítico acumula ≥ 2 días de retraso.
3. Ajuste de alcance: reducción de features Could Have antes de tocar los Must Have.
4. Foco en mantener intactas las fechas de H7 y H13.

---

### 11.5 Relación entre Hitos y Entregables

Los hitos están directamente vinculados a los entregables definidos en la Sección 9 del Anexo:

| Hito | Entregables Asociados | Cantidad |
|------|-----------------------|----------|
| H1 | E1-D1 | 1 |
| H2 | E1-D2 | 1 |
| H3 | E1-DEV1 | 1 |
| H4 | E1-DEV2 | 1 |
| H5 | E1-DEV3 | 1 |
| H6 | E1-INF1 | 1 |
| H7 | E1-PRES1, E1-DOC1, E1-DOC2, E1-VAL1 | 4 |
| H8 | E2-DEV1 | 1 |
| H9 | E2-DEV2, E2-DEV3 | 2 |
| H10 | E2-OPT1 | 1 |
| H11 | E2-UAT1, E2-DOC1, E2-DOC2 | 3 |
| H12 | E2-INF1 | 1 |
| H13 | E2-PRES1, E2-DOC3, E2-DOC4 | 3 |
| **TOTAL** | **23 entregables** | **23** |

Los hitos H7 (Presentación E1) y H13 (Presentación Final) agrupan múltiples entregables porque representan puntos de evaluación académica donde se integran resultados técnicos, documentales y de validación.

---

### 11.6 Relación entre Hitos y Objetivos Específicos

Los hitos también están alineados con los objetivos específicos del proyecto (Sección 3 del Anexo):

| Hito | Objetivos Específicos Relacionados |
|------|------------------------------------|
| H1 | OE-T1, OE-T2, OE-T3 (Objetivos Técnicos) |
| H2 | OE-Q1 (Usabilidad multidispositivo) |
| H3 | OE-T1, OE-F1 (Backend RESTful, CRUD) |
| H4 | OE-T2, OE-F2, OE-Q1 (Frontend, Mapa, Responsive) |
| H5 | OE-F1, OE-F2, OE-F3 (CRUD, Mapa, Carga de Avistamientos) |
| H6 | OE-G1 (Completar E1 antes del 20/12/2025) |
| H7 | OE-G1, OE-Q3 (Entrega E1, Tests ≥ 70 %) |
| H8 | OE-F4 (Búsqueda Multiparámetro) |
| H9 | OE-F5, OE-F6 (Perfiles con Timeline, Diferenciación Visual básica) |
| H10 | OE-Q2 (Performance, Clustering) |
| H11 | OE-V1 (UAT con Usuarios Reales) |
| H12 | OE-G2 (Completar E2 antes del 20/02/2026) |
| H13 | OE-G2, OE-G3 (Entrega final, 150 horas máx) |

---

### 11.7 Estrategia de Seguimiento de Hitos

Para garantizar el cumplimiento de los hitos en las fechas objetivo, se propone la siguiente estrategia:

#### 11.7.1 Registro Semanal de Avance

- **Frecuencia:** revisión semanal del estado de los hitos activos.
- **Herramientas posibles:** Trello, Notion, GitHub Projects o planilla propia.
- **Semáforo de estado:**
    - Verde: hito en tiempo o adelantado.
    - Amarillo: retraso de 1–2 días.
    - Rojo: retraso de 3+ días.

#### 11.7.2 Alertas Tempranas

Si un hito de la ruta crítica acumula retraso de 2 o más días:
1. Analizar causas (técnicas, de organización, externas).
2. Repriorizar: pausar tareas "Could Have" o "Should Have" no críticas.
3. Evaluar posibilidad de dedicar horas adicionales puntuales.
4. Ajustar el alcance funcional si fuera necesario (pero sin mover fechas académicas).

#### 11.7.3 Puntos de Decisión

Momentos clave para tomar decisiones estratégicas:

- **30/06/2025 (H3):** Si el backend no está operativo, recortar alcance de E1 a lo estrictamente esencial.
- **30/09/2025 (H4):** Si el frontend se retrasa, simplificar UI y estilo visual.
- **30/11/2025 (H5):** Si la integración no está madura, congelar features y asegurar estabilidad para H6/H7.
- **25/01/2026 (H9):** Si perfiles sufren retrasos, simplificar timeline o diferir parte de la diferenciación visual refinada a H10.
- **15/02/2026 (H11):** Si UAT se realiza tarde, priorizar aplicar solo los ajustes críticos de usabilidad y estabilidad.

---

### 11.8 Resumen Ejecutivo de Hitos

El proyecto Find Me define **13 hitos principales**, de los cuales:
- **11 forman parte de la ruta crítica**.
- **2 (H2 y H10) tienen margen de maniobra** sin afectar la fecha final.

**Los hitos más sensibles son:**
- **H7 – Presentación E1 (20/12/2025):** primera evaluación académica, sin margen de retraso.
- **H13 – Presentación Final (20/02/2026):** cierre del proyecto y condición para la graduación.

El proyecto cuenta con un **margen global de ~10 % del tiempo total**, distribuido como buffer sobre la ruta crítica, adecuado al nivel de complejidad técnica y a la naturaleza académica del trabajo.

La estrategia de gestión combina:
- Priorización de Must Have sobre Should/Could Have.
- Seguimiento semanal y semáforo de avance.
- Alertas tempranas y ajustes de alcance cuando sea necesario.

---
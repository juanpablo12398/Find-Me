# Guía de Deployment - Find Me

Instrucciones completas para despliegue del sistema en diferentes entornos.

---

## Tabla de Contenidos

1. [Arquitectura de Deployment](#arquitectura-de-deployment)
2. [Entorno de Desarrollo Local](#entorno-de-desarrollo-local)
3. [Instalación Local Detallada](#instalación-local-detallada)
4. [Deployment con Docker](#deployment-con-docker)
5. [Deployment en Producción](#deployment-en-producción)
6. [Variables de Entorno](#variables-de-entorno)
7. [Verificación Post-Deployment](#verificación-post-deployment)
8. [Troubleshooting](#troubleshooting)

---

## Arquitectura de Deployment

### Camino de Deployment Recomendado

Camino sugerido para un entorno académico / productivo simple:

- **Backend monolito Spring Boot (Railway)**
    - Sirve la API REST y los archivos estáticos del frontend desde `/resources/static`
- **Base de datos PostgreSQL + PostGIS (Railway Postgres u otro servicio compatible)**
- **Almacenamiento de imágenes mediante URLs externas** (Cloudinary u otro proveedor)

Otras variantes (como frontend separado en Vercel o backend en Render) se describen como **alternativas opcionales**.

### Diagrama de Alto Nivel
```
┌─────────────────────────────────────────────┐
│         FRONTEND (archivos estáticos)       │
│  Servidos por el backend o Vercel/Netlify*  │
│  *Opcional: despliegue separado             │
└──────────────────┬──────────────────────────┘
                   │ HTTPS/REST
                   ↓
┌─────────────────────────────────────────────┐
│          BACKEND API (Railway/Render)       │
│        https://find-me-api.railway.app      │
│                 Puerto: 8080                │
└──────────────────┬──────────────────────────┘
                   │ JDBC
                   ↓
┌─────────────────────────────────────────────┐
│      PostgreSQL 15 + PostGIS 3.3            │
│                 Puerto: 5432                │
└─────────────────────────────────────────────┘
                   │
                   ↓
┌─────────────────────────────────────────────┐
│         Cloudinary (Almacenamiento)         │
│              URLs externas                  │
└─────────────────────────────────────────────┘
```

---

## Entorno de Desarrollo Local

### Componentes del Entorno de Desarrollo

| Componente | Tecnología | Puerto | Descripción |
|------------|-----------|--------|-------------|
| **Backend** | Spring Boot + Tomcat embebido | 8080 | Servidor de aplicación |
| **Frontend** | Archivos estáticos | 8080 | Servidos desde `/resources/static` |
| **Base de Datos** | PostgreSQL + PostGIS | 5432 | Local o contenedor Docker |
| **Imágenes** | URLs externas | - | Cloudinary u otro servicio |

### Acceso al Sistema Local

Una vez levantado el entorno:

- **Aplicación Web:** `http://localhost:8080`
- **API REST:** `http://localhost:8080/api/*`
- **Swagger UI:**
    - Típicamente: `http://localhost:8080/swagger-ui.html`
    - O bien: `http://localhost:8080/swagger-ui/index.html` (según configuración de Springdoc)
- **Actuator Health:** `http://localhost:8080/actuator/health`

---

## Instalación Local Detallada

### Requisitos Previos

| Software | Versión Mínima | Propósito |
|----------|----------------|-----------|
| **Java JDK** | 17 LTS | Runtime del backend |
| **PostgreSQL** | 15+ | Base de datos relacional |
| **PostGIS** | 3.3+ | Extensión geoespacial |
| **Maven** | 3.8+ | Gestión de dependencias/build |
| **Git** | 2.x | Control de versiones |

### Pasos de Instalación

#### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/find-me.git
cd find-me
```

#### 2. Instalar PostgreSQL + PostGIS

**Opción A: Instalación nativa (Linux/Ubuntu)**
```bash
# Instalar PostgreSQL
sudo apt update
sudo apt install postgresql postgresql-contrib

# Instalar PostGIS (ajustar versión si es necesario)
sudo apt install postgis postgresql-15-postgis-3
```

**Opción B: macOS (Homebrew, recomendado)**
```bash
brew install postgresql@15
brew install postgis
```

**Opción C: Windows**

1. Descargar el instalador desde: https://www.postgresql.org/download/windows/
2. Instalar PostgreSQL
3. Desde **Stack Builder** (incluido con PostgreSQL), agregar la extensión **PostGIS** para la versión instalada

#### 3. Crear Base de Datos y Habilitar PostGIS
```bash
# Iniciar PostgreSQL (si no está corriendo)
sudo service postgresql start   # Linux (puede variar según distro)

# Conectar como superusuario
sudo -u postgres psql

# Dentro de psql:
CREATE DATABASE findme;
CREATE USER findme_user WITH PASSWORD 'tu_password_seguro';
GRANT ALL PRIVILEGES ON DATABASE findme TO findme_user;

-- Conectar a la base de datos
\c findme

-- Habilitar extensión PostGIS
CREATE EXTENSION postgis;

-- Verificar instalación de PostGIS
SELECT PostGIS_version();

-- Salir
\q
```

**Nota:** En Windows/macOS, los pasos de creación de base y extensión son equivalentes, realizados desde `psql` o desde una herramienta gráfica (pgAdmin) ejecutando los mismos comandos SQL.

#### 4. Configurar `application.properties`

Editar `src/main/resources/application.properties`:
```properties
# ========================================
# CONFIGURACIÓN DE BASE DE DATOS
# ========================================
spring.datasource.url=jdbc:postgresql://localhost:5432/findme
spring.datasource.username=findme_user
spring.datasource.password=tu_password_seguro
spring.datasource.driver-class-name=org.postgresql.Driver

# ========================================
# CONFIGURACIÓN JPA/HIBERNATE
# ========================================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.spatial.dialect.postgis.PostgisDialect
spring.jpa.properties.hibernate.format_sql=true

# ========================================
# CONFIGURACIÓN DE SEGURIDAD JWT
# ========================================
jwt.secret=tu_secret_muy_largo_minimo_256_bits_de_longitud_para_seguridad

# Nota:
# En producción, se recomienda definir JWT_SECRET como variable de entorno.
# Spring Boot mapeará JWT_SECRET -> jwt.secret automáticamente.

# ========================================
# CONFIGURACIÓN DE CLOUDINARY (OPCIONAL)
# ========================================
cloudinary.cloud-name=tu_cloud_name
cloudinary.api-key=tu_api_key
cloudinary.api-secret=tu_api_secret

# ========================================
# CONFIGURACIÓN DEL SERVIDOR
# ========================================
server.port=8080
server.error.include-message=always
```

#### 5. Ejecutar el Proyecto
```bash
# Compilar el proyecto
mvn clean install

# Ejecutar el backend
mvn spring-boot:run
```

#### 6. Verificar la Instalación
```bash
# Verificar que el servidor está corriendo
curl http://localhost:8080/actuator/health

# Respuesta esperada:
# {"status":"UP"}
```

Abrir navegador en:

- **Aplicación:** `http://localhost:8080`
- **Swagger:** `http://localhost:8080/swagger-ui.html` (o `http://localhost:8080/swagger-ui/index.html` según la versión de Springdoc configurada)

---

## Deployment con Docker

### Archivo `docker-compose.yml`

Crear en la raíz del proyecto:
```yaml
version: '3.8'

services:
  db:
    image: postgis/postgis:15-3.3
    container_name: findme-db
    environment:
      POSTGRES_DB: findme
      POSTGRES_USER: findme_user
      POSTGRES_PASSWORD: password_seguro
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U findme_user"]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - findme-network

  app:
    build: .
    container_name: findme-backend
    depends_on:
      db:
        condition: service_healthy
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/findme
      SPRING_DATASOURCE_USERNAME: findme_user
      SPRING_DATASOURCE_PASSWORD: password_seguro

      # JWT: en contenedor, se recomienda usar variable de entorno
      JWT_SECRET: ${JWT_SECRET}

      # Cloudinary (opcional)
      CLOUDINARY_CLOUD_NAME: ${CLOUDINARY_CLOUD_NAME}
      CLOUDINARY_API_KEY: ${CLOUDINARY_API_KEY}
      CLOUDINARY_API_SECRET: ${CLOUDINARY_API_SECRET}
    restart: unless-stopped
    networks:
      - findme-network

volumes:
  postgres_data:

networks:
  findme-network:
    driver: bridge
```

### Archivo `Dockerfile`

Crear en la raíz del proyecto:
```dockerfile
# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Archivo `.env`

Crear en la raíz (NO commitear a Git):
```env
JWT_SECRET=tu_secret_jwt_muy_largo_256_bits_minimo
CLOUDINARY_CLOUD_NAME=tu_cloud_name
CLOUDINARY_API_KEY=tu_api_key
CLOUDINARY_API_SECRET=tu_api_secret
```

### Comandos Docker
```bash
# Levantar los contenedores
docker-compose up -d

# Ver logs de la app
docker-compose logs -f app

# Detener los contenedores
docker-compose down

# Detener y eliminar volúmenes (CUIDADO: borra los datos)
docker-compose down -v

# Reconstruir después de cambios en el código
docker-compose up -d --build
```

---

## Deployment en Producción

### Opción 1 (Recomendada): Railway (Backend + Base de Datos)

#### Despliegue del Backend

1. Crear cuenta en https://railway.app
2. Crear Nuevo Proyecto → "Deploy from GitHub repo"
3. Seleccionar repositorio `find-me`
4. Railway detecta automáticamente un proyecto Java/Maven
5. Configurar variables de entorno (ver sección Variables de Entorno)
6. Deploy automático

#### Crear Base de Datos PostgreSQL

1. En el proyecto Railway → "New" → "Database" → "PostgreSQL"
2. Railway automáticamente:
    - Crea la base de datos
    - Proporciona credenciales y URL de conexión
    - Expone variables de entorno necesarias

3. Habilitar PostGIS manualmente:
```bash
# Conectar a la base de datos de Railway
railway connect postgres    # o psql con la URL que provea Railway

# Dentro de psql:
CREATE EXTENSION postgis;
SELECT PostGIS_version();
\q
```

#### URL del Backend

Railway proporciona una URL del estilo:
```
https://find-me-production.up.railway.app
```

Desde allí se exponen tanto la API REST como los recursos estáticos del frontend (si se decide que el backend sirva el frontend).

---

### Opción 2 (Opcional): Render (Solo Backend)

1. Crear cuenta en https://render.com
2. New → Web Service → Connect repository
3. Configurar:
    - **Build Command:** `mvn clean package -DskipTests`
    - **Start Command:** `java -jar target/find-me-*.jar`
    - **Environment:** Java 17
4. Agregar variables de entorno (DB, JWT, Cloudinary)
5. Deploy

**Nota:** La base de datos puede residir en otro proveedor (Railway, RDS, etc.) apuntando con `SPRING_DATASOURCE_URL`.

---

### Despliegue del Frontend (Vercel, Opcional)

Aunque el camino recomendado es servir el frontend desde el propio backend, también se puede desplegar una copia estática en Vercel.

1. Crear cuenta en https://vercel.com
2. Import Project → Repositorio GitHub
3. Configurar:
    - **Framework Preset:** Other
    - **Root Directory:** `src/main/resources/static`
    - **Build Command:** (dejar vacío, se usan archivos estáticos)
    - **Output Directory:** `./`
4. Deploy

**Nota:** En este escenario, el frontend en Vercel deberá apuntar a la URL del backend en Railway/Render para las llamadas a la API (`/api/*`).

---

## Variables de Entorno

### Variables Requeridas (Producción)
```env
# Base de Datos
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/database
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=password

# JWT
JWT_SECRET=secret_muy_largo_256_bits_minimo

# Cloudinary (opcional)
CLOUDINARY_CLOUD_NAME=nombre_cloud
CLOUDINARY_API_KEY=api_key
CLOUDINARY_API_SECRET=api_secret

# Perfil activo (opcional)
SPRING_PROFILES_ACTIVE=prod
```

**Nota sobre mapeo de propiedades:**

`JWT_SECRET` (variable de entorno) se mapea automáticamente a `jwt.secret` (propiedad de Spring), por lo que no es necesario duplicar configuración si se usa entorno productivo.

### Configuración en Railway

En Railway → Settings → Variables, definir por ejemplo:
```
SPRING_DATASOURCE_URL = ${Postgres.DATABASE_URL}   # Railway lo genera automáticamente
SPRING_DATASOURCE_USERNAME = [usuario]
SPRING_DATASOURCE_PASSWORD = [password]

JWT_SECRET = [tu_secret_jwt_largo]
CLOUDINARY_CLOUD_NAME = [tu_cloud]
CLOUDINARY_API_KEY = [tu_key]
CLOUDINARY_API_SECRET = [tu_secret]
SPRING_PROFILES_ACTIVE = prod
```

Railway se encarga de exponer la URL de la base de datos en un formato compatible con Spring Boot (o bien puede hacerse un pequeño ajuste si es necesario para usar `jdbc:postgresql://...`).

---

## Verificación Post-Deployment

### Checklist de Verificación
```bash
# 1. Verificar salud del backend
curl https://tu-backend.railway.app/actuator/health
# Respuesta esperada:
# {"status":"UP"}

# 2. Verificar Swagger
curl https://tu-backend.railway.app/swagger-ui.html
# (o /swagger-ui/index.html según configuración)

# 3. Verificar PostGIS en la base de datos
psql $DATABASE_URL -c "SELECT PostGIS_version();"

# 4. Test de endpoint público
curl https://tu-backend.railway.app/api/desaparecidos

# 5. Test de frontend (si está en Vercel)
curl https://tu-frontend.vercel.app
```

### Verificaciones Manuales

- [ ] Backend responde correctamente en la URL de producción
- [ ] Swagger UI es accesible
- [ ] Frontend carga correctamente (desde backend o Vercel)
- [ ] Mapa se visualiza (Leaflet + OpenStreetMap)
- [ ] PostGIS está habilitado y operativo en la base de datos
- [ ] Cloudinary permite subir imágenes (si se usa)
- [ ] HTTPS funciona en el dominio de producción
- [ ] CORS está configurado para el dominio del frontend

---

## Troubleshooting

### Error: "Failed to connect to database"

**Posibles causas:**
- Variables de entorno de la base de datos incorrectas
- Servicio de base de datos caído o inaccesible desde el backend

**Acciones sugeridas:**
```bash
# Verificar variables de entorno en el proveedor
echo $SPRING_DATASOURCE_URL
echo $SPRING_DATASOURCE_USERNAME
echo $SPRING_DATASOURCE_PASSWORD
```

En Railway, comprobar que el servicio de Postgres esté "UP" y que los datos coincidan con la configuración de Spring.

---

### Error: "PostGIS extension not found"

**Causa:** PostGIS no está habilitado en la base de datos.

**Solución:**
```bash
# Conectar a la base de datos
psql $DATABASE_URL   # o railway connect postgres

# Habilitar PostGIS
CREATE EXTENSION IF NOT EXISTS postgis;

# Verificar
SELECT PostGIS_version();
```

---

### Error: "CORS policy blocked"

**Causa:** El dominio del frontend (por ejemplo `https://find-me.vercel.app`) no está en la lista de orígenes permitidos.

**Solución:** Actualizar la configuración de CORS en el backend:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:8080",
                    "https://find-me.vercel.app",
                    "https://tu-dominio-personalizado.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

---

### Error: "Port 8080 already in use" (Local)

**Causa:** Otro proceso ocupa el puerto 8080.

**Soluciones:**

**Opción 1: Cambiar el puerto**

En `application.properties`:
```properties
server.port=8081
```

**Opción 2: Finalizar el proceso que usa el puerto 8080**
```bash
# Linux/Mac:
lsof -ti:8080 | xargs kill -9

# Windows:
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

---

### Error: "JWT secret too short"

**Causa:** El secreto JWT debe tener longitud suficiente (recomendado ≥ 32 caracteres).

**Solución:**
```env
JWT_SECRET=este_es_un_secret_muy_largo_de_al_menos_32_caracteres
```

---

### Error: "Cloudinary upload failed"

**Causa:** Credenciales incorrectas o servicio no accesible.

**Soluciones:**

1. Verificar credenciales en el panel de Cloudinary

2. Verificar variables de entorno:
```bash
echo $CLOUDINARY_CLOUD_NAME
echo $CLOUDINARY_API_KEY
echo $CLOUDINARY_API_SECRET
```

3. Probar una subida manual:
```bash
curl -X POST https://api.cloudinary.com/v1_1/tu_cloud/image/upload \
  -F "file=@test.jpg" \
  -F "api_key=tu_api_key" \
  -F "api_secret=tu_api_secret" \
  -F "upload_preset=tu_upload_preset"
```

---

## Costos Estimados (Tiers Gratuitos)

| Servicio | Plan | Límites aproximados | Costo Mensual |
|----------|------|---------------------|---------------|
| **Railway** | Developer | 500 horas/mes, crédito inicial | $0 |
| **Render** | Free | 750 horas/mes | $0 |
| **Vercel** | Hobby | 100 GB bandwidth, builds ilimitados | $0 |
| **Cloudinary** | Free | 25 créditos/mes, ~25 GB almacenamiento | $0 |
| **PostgreSQL** | Incluido | Compartido con backend (Railway, etc.) | $0 |

**Total estimado:** $0 ARS / $0 USD por mes en tiers gratuitos (para cargas de uso académico o de baja escala).

---

**Última actualización:** Diciembre 2025  
**Versión:** 1.0
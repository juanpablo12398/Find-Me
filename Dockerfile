FROM nginx:alpine

# Copiar todos los archivos estáticos
COPY src/main/resources/static/ /usr/share/nginx/html/

# Copiar configuración de nginx
COPY nginx.conf /etc/nginx/templates/default.conf.template

# Exponer puerto dinámico de Railway
EXPOSE $PORT

# Iniciar nginx con sustitución de variable PORT
CMD sh -c "envsubst '\$PORT' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"
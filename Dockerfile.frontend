FROM nginx:alpine

# Copiar archivos estáticos
COPY src/main/resources/static/ /usr/share/nginx/html/

# Copiar configuración
COPY nginx.conf /etc/nginx/templates/default.conf.template

EXPOSE $PORT

# Script para reemplazar variables de entorno
CMD sh -c "envsubst '\$PORT \$BACKEND_URL' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"
#!/bin/bash

# Script de deployment para MediApp Backend en VPS
# Autor: MediApp Team
# Fecha: 23 de octubre de 2025

set -e  # Salir si hay algún error

echo "======================================"
echo "🚀 Iniciando deployment de MediApp Backend"
echo "======================================"

# Variables de configuración
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"
VPS_KEY="~/downloads/mediapp-key.pem"
VPS_PATH="/opt/citas"
JAR_NAME="app.jar"
SERVICE_NAME="citas"

echo ""
echo "📦 Paso 1: Compilando el proyecto con Maven..."
./mvnw clean package -DskipTests

if [ ! -f "target/${JAR_NAME}" ]; then
    echo "❌ Error: No se encontró el archivo JAR compilado"
    exit 1
fi

echo "✅ Compilación exitosa!"
echo ""
echo "📤 Paso 2: Subiendo JAR a VPS..."

# Crear directorio en VPS si no existe
ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} "sudo mkdir -p ${VPS_PATH}"

# Copiar JAR al VPS (renombrando a app.jar)
scp -i ${VPS_KEY} target/citas-backend-0.0.1-SNAPSHOT.jar ${VPS_USER}@${VPS_HOST}:${VPS_PATH}/${JAR_NAME}

echo "✅ Archivo subido exitosamente!"
echo ""
echo "🔄 Paso 3: Reiniciando servicio en VPS..."

# Ejecutar comandos en VPS para reiniciar el servicio
ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} << 'ENDSSH'
cd /opt/citas

# Detener el servicio si está corriendo
if systemctl is-active --quiet citas; then
    echo "Deteniendo servicio..."
    sudo systemctl stop citas
fi

# Reiniciar el servicio
echo "Iniciando servicio..."
sudo systemctl start citas
sudo systemctl enable citas

# Verificar estado
sleep 3
if systemctl is-active --quiet citas; then
    echo "✅ Servicio iniciado correctamente"
    sudo systemctl status citas --no-pager
else
    echo "❌ Error: El servicio no pudo iniciarse"
    sudo journalctl -u citas -n 50 --no-pager
    exit 1
fi
ENDSSH

echo ""
echo "======================================"
echo "✅ Deployment completado exitosamente!"
echo "======================================"
echo ""
echo "📊 Información del deployment:"
echo "   - VPS: ${VPS_HOST}"
echo "   - Ruta: ${VPS_PATH}"
echo "   - Servicio: ${SERVICE_NAME}"
echo ""
echo "🌐 URLs disponibles:"
echo "   - API: http://${VPS_HOST}:8080"
echo "   - Swagger UI: http://${VPS_HOST}:8080/swagger-ui.html"
echo "   - Health Check: http://${VPS_HOST}:8080/actuator/health"
echo ""
echo "📝 Comandos útiles:"
echo "   - Ver logs: ssh ${VPS_USER}@${VPS_HOST} 'journalctl -u ${SERVICE_NAME} -f'"
echo "   - Estado: ssh ${VPS_USER}@${VPS_HOST} 'systemctl status ${SERVICE_NAME}'"
echo "   - Reiniciar: ssh ${VPS_USER}@${VPS_HOST} 'systemctl restart ${SERVICE_NAME}'"
echo ""

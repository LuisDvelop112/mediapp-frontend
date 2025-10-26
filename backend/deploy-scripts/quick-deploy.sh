#!/bin/bash

# Script rápido de deployment - Solo copia y reinicia
# Para usar cuando ya tienes todo configurado en el VPS

set -e

# Variables
VPS_KEY="~/downloads/mediapp-key.pem"
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"
VPS_PATH="/opt/citas"

echo "🚀 Deployment rápido de MediApp Backend"
echo ""

# Compilar
echo "📦 Compilando..."
./mvnw clean package -DskipTests

# Subir
echo "📤 Subiendo a VPS..."
# Primero copiar al home del usuario
scp -i ${VPS_KEY} target/citas-backend-0.0.1-SNAPSHOT.jar ${VPS_USER}@${VPS_HOST}:~/app.jar

# Mover al directorio final y reiniciar
echo "🔄 Moviendo archivo y reiniciando servicio..."
ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} << 'ENDSSH'
# Detener el servicio
sudo systemctl stop citas

# Mover el archivo
sudo mv ~/app.jar /opt/citas/app.jar
sudo chown citasuser:citasuser /opt/citas/app.jar
sudo chmod 644 /opt/citas/app.jar

# Reiniciar el servicio
sudo systemctl start citas

# Mostrar estado
sudo systemctl status citas --no-pager
ENDSSH

echo ""
echo "✅ Deployment completado!"
echo "🌐 Swagger UI: http://56.125.172.86:8080/swagger-ui.html"

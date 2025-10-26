#!/bin/bash

# Deployment Manual - Guía paso a paso
# Usa este script para deployment guiado

VPS_KEY="~/downloads/mediapp-key.pem"
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"
VPS_PATH="/opt/citas"

echo "======================================"
echo "📋 GUÍA DE DEPLOYMENT MANUAL"
echo "======================================"
echo ""
echo "El JAR ha sido compilado exitosamente en:"
echo "   target/citas-backend-0.0.1-SNAPSHOT.jar"
echo ""
echo "======================================"
echo "PASOS A SEGUIR:"
echo "======================================"
echo ""
echo "1️⃣  Copia el JAR a tu VPS (renombrando a app.jar):"
echo "    scp -i ${VPS_KEY} target/citas-backend-0.0.1-SNAPSHOT.jar ${VPS_USER}@${VPS_HOST}:${VPS_PATH}/app.jar"
echo ""
echo "2️⃣  Conéctate a tu VPS:"
echo "    ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST}"
echo ""
echo "3️⃣  Una vez conectado, ejecuta estos comandos:"
echo "    cd ${VPS_PATH}"
echo "    sudo systemctl restart citas"
echo "    sudo systemctl status citas"
echo ""
echo "4️⃣  Verifica que esté funcionando:"
echo "    curl http://localhost:8080/actuator/health"
echo ""
echo "5️⃣  Sal de la VPS:"
echo "    exit"
echo ""
echo "======================================"
echo "URLs para verificar (desde tu navegador):"
echo "======================================"
echo ""
echo "   🌐 Health Check: http://56.125.172.86:8080/actuator/health"
echo "   📚 Swagger UI: http://56.125.172.86:8080/swagger-ui.html"
echo "   📄 API Docs: http://56.125.172.86:8080/v3/api-docs"
echo ""
echo "======================================"
echo "¿Quieres continuar con el deployment automático? (y/n)"
read -r response

if [[ "$response" =~ ^[Yy]$ ]]; then
    echo ""
    echo "📤 Copiando JAR a VPS..."
    echo ""
    # Copiar al home del usuario primero
    scp -i ${VPS_KEY} target/citas-backend-0.0.1-SNAPSHOT.jar ${VPS_USER}@${VPS_HOST}:~/app.jar
    
    echo ""
    echo "✅ Archivo copiado!"
    echo ""
    echo "🔄 Moviendo archivo y reiniciando servicio en VPS..."
    echo ""
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
    echo "======================================"
    echo "✅ Deployment completado!"
    echo "======================================"
    echo ""
else
    echo ""
    echo "❌ Deployment cancelado"
    echo "   El JAR compilado está disponible en: target/citas-backend-0.0.1-SNAPSHOT.jar"
    echo ""
fi

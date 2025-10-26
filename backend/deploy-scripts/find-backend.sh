#!/bin/bash

# Script para encontrar la ubicación del backend en la VPS

VPS_KEY="~/downloads/mediapp-key.pem"
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"

echo "🔍 Buscando ubicación del backend en VPS..."
echo ""

ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} << 'ENDSSH'
echo "1️⃣  Buscando archivos JAR del backend..."
find / -name "*citas-backend*.jar" -o -name "*mediapp*.jar" 2>/dev/null
echo ""

echo "2️⃣  Verificando servicios systemd..."
sudo systemctl list-units --type=service | grep -i "media\|cita\|backend" || echo "No se encontraron servicios relacionados"
echo ""

echo "3️⃣  Buscando procesos Java..."
ps aux | grep java | grep -v grep || echo "No hay procesos Java corriendo"
echo ""

echo "4️⃣  Verificando directorios comunes..."
echo "Directorio /opt:"
ls -la /opt/ 2>/dev/null || echo "No existe /opt o no hay permisos"
echo ""
echo "Directorio /home/ubuntu:"
ls -la /home/ubuntu/ 2>/dev/null || echo "No existe /home/ubuntu o no hay permisos"
echo ""
echo "Directorio /var/www:"
ls -la /var/www/ 2>/dev/null || echo "No existe /var/www o no hay permisos"
echo ""

echo "5️⃣  Verificando archivo de servicio systemd..."
sudo cat /etc/systemd/system/mediapp-backend.service 2>/dev/null || echo "No se encontró el archivo de servicio"
ENDSSH

echo ""
echo "======================================"
echo "✅ Búsqueda completada"
echo "======================================"

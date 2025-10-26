#!/bin/bash

# Script para verificar el estado del servicio en VPS

VPS_KEY="~/downloads/mediapp-key.pem"
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"

echo "🔍 Verificando estado de MediApp Backend en VPS..."
echo ""

ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} << 'ENDSSH'
echo "📊 Estado del servicio:"
sudo systemctl status citas --no-pager
echo ""
echo "💾 Uso de memoria:"
ps aux | grep java | grep -v grep
echo ""
echo "🌐 Puerto 8080:"
sudo netstat -tulpn | grep :8080 || sudo ss -tulpn | grep :8080
echo ""
echo "📝 Últimas líneas del log:"
sudo journalctl -u citas -n 20 --no-pager
ENDSSH

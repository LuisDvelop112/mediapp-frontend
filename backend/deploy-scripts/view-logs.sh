#!/bin/bash

# Script para ver logs en tiempo real del backend en VPS

VPS_KEY="~/downloads/mediapp-key.pem"
VPS_USER="ubuntu"
VPS_HOST="56.125.172.86"

echo "📋 Conectando a logs de MediApp Backend..."
echo "   Presiona Ctrl+C para salir"
echo ""

ssh -i ${VPS_KEY} ${VPS_USER}@${VPS_HOST} 'sudo journalctl -u citas -f'

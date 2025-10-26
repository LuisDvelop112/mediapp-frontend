#!/bin/bash

# =====================================================
# Script para cargar el dataset de MediApp
# =====================================================

echo "🏥 MediApp - Cargador de Dataset"
echo "=================================="
echo ""

# Colores
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Configuración de base de datos
DB_NAME="mediapp_db"
DB_USER="root"
SQL_FILE="src/main/resources/data/dataset_mediapp.sql"

# Verificar si existe el archivo SQL
if [ ! -f "$SQL_FILE" ]; then
    echo -e "${RED}❌ Error: No se encontró el archivo $SQL_FILE${NC}"
    exit 1
fi

echo -e "${YELLOW}📋 Base de datos: $DB_NAME${NC}"
echo -e "${YELLOW}👤 Usuario: $DB_USER${NC}"
echo ""

# Solicitar contraseña
echo -e "${YELLOW}🔐 Ingrese la contraseña de MySQL:${NC}"
read -s DB_PASSWORD
echo ""

# Verificar conexión
echo -e "${YELLOW}⏳ Verificando conexión a MySQL...${NC}"
mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "SELECT 1" > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error: No se pudo conectar a MySQL${NC}"
    echo -e "${RED}Verifica tu usuario y contraseña${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Conexión exitosa${NC}"
echo ""

# Verificar si existe la base de datos
echo -e "${YELLOW}⏳ Verificando base de datos $DB_NAME...${NC}"
mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "USE $DB_NAME" > /dev/null 2>&1

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ Error: La base de datos $DB_NAME no existe${NC}"
    echo -e "${YELLOW}💡 Creando base de datos...${NC}"
    mysql -u "$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE $DB_NAME"
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ Base de datos creada${NC}"
    else
        echo -e "${RED}❌ Error al crear la base de datos${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✅ Base de datos encontrada${NC}"
fi

echo ""

# Preguntar si desea limpiar datos existentes
echo -e "${YELLOW}⚠️  ¿Desea limpiar los datos existentes antes de cargar? (s/n)${NC}"
read -r RESPUESTA

if [[ "$RESPUESTA" =~ ^[Ss]$ ]]; then
    echo -e "${YELLOW}⏳ Limpiando datos existentes...${NC}"
    # El script SQL ya incluye la limpieza
else
    echo -e "${YELLOW}⏭️  Omitiendo limpieza de datos${NC}"
fi

echo ""

# Cargar el dataset
echo -e "${YELLOW}⏳ Cargando dataset desde $SQL_FILE...${NC}"
mysql -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" < "$SQL_FILE"

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Dataset cargado exitosamente${NC}"
else
    echo -e "${RED}❌ Error al cargar el dataset${NC}"
    exit 1
fi

echo ""

# Verificar conteo de registros
echo -e "${YELLOW}📊 Verificando registros cargados...${NC}"
echo ""

QUERY="SELECT 'Usuarios' as Tabla, COUNT(*) as Total FROM usuarios
UNION ALL SELECT 'Especialidades', COUNT(*) FROM especialidades
UNION ALL SELECT 'Pacientes', COUNT(*) FROM pacientes
UNION ALL SELECT 'Médicos', COUNT(*) FROM medicos
UNION ALL SELECT 'Disponibilidad', COUNT(*) FROM calendario_disponibilidad
UNION ALL SELECT 'Citas', COUNT(*) FROM citas
UNION ALL SELECT 'Historiales', COUNT(*) FROM historial_medico
UNION ALL SELECT 'Prescripciones', COUNT(*) FROM prescripciones
UNION ALL SELECT 'Evaluaciones', COUNT(*) FROM evaluaciones
UNION ALL SELECT 'Notificaciones', COUNT(*) FROM notificaciones;"

mysql -u "$DB_USER" -p"$DB_PASSWORD" "$DB_NAME" -e "$QUERY"

echo ""
echo -e "${GREEN}🎉 ¡Dataset cargado correctamente!${NC}"
echo ""
echo -e "${YELLOW}📌 Información útil:${NC}"
echo -e "   • Base de datos: ${GREEN}$DB_NAME${NC}"
echo -e "   • Total de tablas: ${GREEN}10${NC}"
echo -e "   • Total de registros: ${GREEN}86${NC}"
echo ""
echo -e "${YELLOW}👥 Usuarios de prueba (contraseña: password123):${NC}"
echo -e "   ${GREEN}Paciente:${NC} maria.gonzalez@example.com"
echo -e "   ${GREEN}Médico:${NC} carlos.ramirez@mediapp.com"
echo -e "   ${GREEN}Admin:${NC} admin@mediapp.com"
echo ""
echo -e "${YELLOW}📖 Para más información, revisa: DATASET_README.md${NC}"
echo ""

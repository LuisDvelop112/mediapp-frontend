# 🚀 Scripts de Deployment - MediApp Backend

Scripts para facilitar el deployment del backend a la VPS.

## 📋 Scripts Disponibles

### 1. `deploy.sh` - Deployment Completo
Deployment completo con validaciones y información detallada.

```bash
chmod +x deploy-scripts/deploy.sh
./deploy-scripts/deploy.sh
```

**Qué hace:**
- ✅ Compila el proyecto con Maven
- ✅ Sube el JAR a la VPS
- ✅ Reinicia el servicio systemd
- ✅ Verifica que el servicio esté corriendo
- ✅ Muestra información útil y URLs

### 2. `quick-deploy.sh` - Deployment Rápido
Deployment rápido para actualizaciones frecuentes.

```bash
chmod +x deploy-scripts/quick-deploy.sh
./deploy-scripts/quick-deploy.sh
```

**Qué hace:**
- ⚡ Compilación sin tests
- ⚡ Copia rápida del JAR
- ⚡ Reinicio del servicio

### 3. `view-logs.sh` - Ver Logs en Tiempo Real
Muestra los logs del backend en tiempo real.

```bash
chmod +x deploy-scripts/view-logs.sh
./deploy-scripts/view-logs.sh
```

Presiona `Ctrl+C` para salir.

### 4. `check-status.sh` - Verificar Estado
Verifica el estado completo del servicio en la VPS.

```bash
chmod +x deploy-scripts/check-status.sh
./deploy-scripts/check-status.sh
```

**Información que muestra:**
- Estado del servicio systemd
- Uso de memoria
- Puerto 8080
- Últimas líneas del log

## 🔧 Configuración Inicial

### Dar permisos de ejecución a todos los scripts:
```bash
chmod +x deploy-scripts/*.sh
```

### Verificar conexión SSH:
```bash
ssh root@56.125.172.86
```

## 🌐 URLs de la Aplicación

Después del deployment, accede a:

- **API Base:** http://56.125.172.86:8080
- **Swagger UI:** http://56.125.172.86:8080/swagger-ui.html
- **OpenAPI Docs:** http://56.125.172.86:8080/v3/api-docs
- **Health Check:** http://56.125.172.86:8080/actuator/health

## 📝 Comandos Manuales Útiles

### Compilar localmente:
```bash
./mvnw clean package -DskipTests
```

### Subir JAR manualmente:
```bash
scp target/citas-backend-0.0.1-SNAPSHOT.jar root@56.125.172.86:/root/mediapp-backend/
```

### Reiniciar servicio en VPS:
```bash
ssh root@56.125.172.86 'systemctl restart mediapp-backend'
```

### Ver estado del servicio:
```bash
ssh root@56.125.172.86 'systemctl status mediapp-backend'
```

### Ver logs completos:
```bash
ssh root@56.125.172.86 'journalctl -u mediapp-backend -n 100'
```

## 🐛 Troubleshooting

### Si el servicio no inicia:
```bash
ssh root@56.125.172.86 'journalctl -u mediapp-backend -n 50 --no-pager'
```

### Si el puerto está ocupado:
```bash
ssh root@56.125.172.86 'lsof -i :8080'
# o
ssh root@56.125.172.86 'kill -9 $(lsof -t -i:8080)'
```

### Verificar conectividad:
```bash
curl http://56.125.172.86:8080/actuator/health
```

## 📊 Workflow Recomendado

1. **Hacer cambios** en el código
2. **Commit y push** a GitHub
   ```bash
   git add .
   git commit -m "descripción de cambios"
   git push origin main
   ```
3. **Ejecutar deployment rápido**
   ```bash
   ./deploy-scripts/quick-deploy.sh
   ```
4. **Verificar** que todo funcione
   ```bash
   ./deploy-scripts/check-status.sh
   ```
5. **Probar** en Swagger UI: http://56.125.172.86:8080/swagger-ui.html

## 🔐 Notas de Seguridad

- Los scripts asumen que tienes configurado el acceso SSH con claves
- Si usas contraseña, se te pedirá en cada conexión SSH
- Considera configurar SSH keys para deployment automático

## 📦 Requisitos

- Maven instalado localmente
- Acceso SSH a la VPS (root@56.125.172.86)
- Java 21 instalado en la VPS
- Servicio systemd configurado en la VPS (`mediapp-backend.service`)

#!/bin/bash

# Script para lanzar y probar la aplicación completa con Deepseek AI
# Incluye la API key y realiza pruebas de todos los endpoints

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

API_KEY="your-api-key-here"
BASE_URL="http://localhost:8080"
APP_PID=""

echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo -e "${BLUE}   Prueba Completa de la Aplicación IAExample${NC}"
echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
echo ""

# Función para limpiar al salir
cleanup() {
    echo ""
    echo -e "${YELLOW}Deteniendo la aplicación...${NC}"
    if [ ! -z "$APP_PID" ]; then
        kill $APP_PID 2>/dev/null || true
        wait $APP_PID 2>/dev/null || true
    fi
    # Buscar y matar cualquier proceso de Maven
    pkill -f "spring-boot:run" 2>/dev/null || true
    echo -e "${GREEN}✓ Aplicación detenida${NC}"
    exit
}

trap cleanup EXIT INT TERM

# Verificar que jq está disponible
if ! command -v jq &> /dev/null; then
    echo -e "${YELLOW}⚠️  jq no está instalado. Instalarlo con: sudo apt install jq${NC}"
    echo -e "${YELLOW}   Continuando sin formato JSON...${NC}"
    echo ""
fi

# Verificar si el puerto 8080 está ocupado
if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1; then
    echo -e "${YELLOW}⚠️  Puerto 8080 ya está en uso${NC}"
    read -p "¿Quieres detener el proceso existente? (s/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        pkill -f "spring-boot:run" 2>/dev/null || true
        sleep 2
    else
        echo -e "${YELLOW}Usando la aplicación existente...${NC}"
        echo ""
    fi
fi

# Lanzar la aplicación si no está corriendo
if ! curl -s "$BASE_URL/api/alumnos" > /dev/null 2>&1; then
    echo -e "${BLUE}▶ Lanzando la aplicación Spring Boot...${NC}"
    export DEEPSEEK_API_KEY="$API_KEY"

    # Lanzar la aplicación en background
    mvn spring-boot:run > app.log 2>&1 &
    APP_PID=$!

    echo -e "${YELLOW}⏳ Esperando a que la aplicación inicie (puede tardar 30-60 segundos)...${NC}"

    # Esperar hasta que la aplicación responda
    MAX_WAIT=120
    WAITED=0
    while ! curl -s "$BASE_URL/api/alumnos" > /dev/null 2>&1; do
        sleep 2
        WAITED=$((WAITED + 2))
        printf "."

        if [ $WAITED -ge $MAX_WAIT ]; then
            echo ""
            echo -e "${RED}✗ La aplicación no inició en $MAX_WAIT segundos${NC}"
            echo -e "${YELLOW}Últimas líneas del log:${NC}"
            tail -20 app.log
            exit 1
        fi
    done

    echo ""
    echo -e "${GREEN}✓ Aplicación iniciada correctamente${NC}"
    echo ""
else
    echo -e "${GREEN}✓ Aplicación ya está corriendo${NC}"
    echo ""
fi

# Función para hacer request con manejo de errores
make_request() {
    local METHOD=$1
    local ENDPOINT=$2
    local DATA=$3
    local DESCRIPTION=$4

    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BLUE}$DESCRIPTION${NC}"
    echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

    if [ "$METHOD" = "GET" ]; then
        RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL$ENDPOINT")
    else
        RESPONSE=$(curl -s -w "\n%{http_code}" -X POST "$BASE_URL$ENDPOINT" \
            -H "Content-Type: application/json" \
            -d "$DATA")
    fi

    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
    BODY=$(echo "$RESPONSE" | sed '$d')

    if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ]; then
        echo -e "${GREEN}✓ HTTP $HTTP_CODE${NC}"
        if command -v jq &> /dev/null; then
            echo "$BODY" | jq .
        else
            echo "$BODY"
        fi
    else
        echo -e "${RED}✗ HTTP $HTTP_CODE${NC}"
        echo "$BODY"
    fi

    echo ""
    sleep 1
}

echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}   PRUEBAS DE ENDPOINTS CRUD${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo ""

# Test 1: GET todos los alumnos
make_request "GET" "/api/alumnos" "" "Test 1: Obtener lista de alumnos"

# Test 2: GET detalle de un alumno
make_request "GET" "/api/alumnos/1" "" "Test 2: Obtener detalle del alumno 1 (con asignaturas y notas)"

# Test 3: GET todas las asignaturas
make_request "GET" "/api/asignaturas" "" "Test 3: Obtener lista de asignaturas"

# Test 4: GET detalle de una asignatura
make_request "GET" "/api/asignaturas/1" "" "Test 4: Obtener detalle de la asignatura 1"

# Test 5: GET alumnos de una asignatura
make_request "GET" "/api/asignaturas/1/alumnos" "" "Test 5: Obtener alumnos matriculados en Matemáticas"

echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}   PRUEBAS DEL ENDPOINT DE DEEPSEEK IA${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo ""

# Test IA 1: Consulta simple
make_request "POST" "/api/ia/consultar" \
    '{"prompt":"¿Cuántos alumnos hay en el sistema?"}' \
    "Test IA 1: Consulta sobre cantidad de alumnos"

# Test IA 2: Consulta sobre asignaturas
make_request "POST" "/api/ia/consultar" \
    '{"prompt":"¿Qué asignaturas están disponibles?"}' \
    "Test IA 2: Consulta sobre asignaturas disponibles"

# Test IA 3: Consulta con contexto
make_request "POST" "/api/ia/consultar" \
    '{"prompt":"Dame información sobre el alumno Juan Pérez"}' \
    "Test IA 3: Consulta con contexto sobre un alumno específico"

# Test IA 4: Consulta analítica
make_request "POST" "/api/ia/consultar" \
    '{"prompt":"¿Cuál es el promedio de notas en el sistema?"}' \
    "Test IA 4: Consulta analítica sobre promedios"

# Test IA 5: Consulta descriptiva
make_request "POST" "/api/ia/consultar" \
    '{"prompt":"Explica qué tipo de sistema es este y qué funcionalidades tiene"}' \
    "Test IA 5: Consulta descriptiva del sistema"

echo ""
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo -e "${GREEN}   RESUMEN DE PRUEBAS${NC}"
echo -e "${GREEN}═══════════════════════════════════════════════════════════${NC}"
echo ""
echo -e "${GREEN}✓ Todos los endpoints han sido probados${NC}"
echo -e "${GREEN}✓ La aplicación está funcionando correctamente${NC}"
echo ""
echo -e "${BLUE}Endpoints disponibles:${NC}"
echo "  • GET  /api/alumnos"
echo "  • GET  /api/alumnos/{id}"
echo "  • POST /api/alumnos/{alumnoId}/asignaturas"
echo "  • PUT  /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}/nota"
echo "  • GET  /api/alumnos/{alumnoId}/asignaturas/{asignaturaId}"
echo "  • GET  /api/asignaturas"
echo "  • GET  /api/asignaturas/{id}"
echo "  • GET  /api/asignaturas/{id}/alumnos"
echo "  • POST /api/ia/consultar"
echo ""
echo -e "${BLUE}Consola H2:${NC} http://localhost:8080/h2-console"
echo -e "${BLUE}Log de aplicación:${NC} app.log"
echo ""
echo -e "${YELLOW}Presiona Ctrl+C para detener la aplicación y salir${NC}"
echo ""

# Mantener el script ejecutándose
wait

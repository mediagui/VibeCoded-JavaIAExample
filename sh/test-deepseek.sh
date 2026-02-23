#!/bin/bash

# Script para probar el endpoint de Deepseek
# Asegúrate de que la aplicación está corriendo en http://localhost:8080
# y que DEEPSEEK_API_KEY está configurada
# Uso: ./sh/test-deepseek.sh o cd sh && ./test-deepseek.sh

# Change to project root directory
cd "$(dirname "$0")/.." || exit 1

BASE_URL="http://localhost:8080"
IA_ENDPOINT="$BASE_URL/api/ia/consultar"

echo "═══════════════════════════════════════════════════════════"
echo "Test del Endpoint de Deepseek IA"
echo "═══════════════════════════════════════════════════════════"
echo ""

# Verificar que jq está instalado (para formatear JSON)
if ! command -v jq &> /dev/null; then
    echo "⚠️  jq no está instalado. Instalarlo con: sudo apt install jq"
    echo "Por ahora usaremos curl sin formatos bonitos"
    echo ""
fi

# Test 1: Consulta simple
echo "Test 1: Consulta simple sobre alumnos"
echo "───────────────────────────────────────────────────────────"
curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuántos alumnos hay en el sistema?"}' | jq . 2>/dev/null || curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuántos alumnos hay en el sistema?"}'
echo ""
echo ""

# Test 2: Consulta sobre asignaturas
echo "Test 2: Consulta sobre asignaturas"
echo "───────────────────────────────────────────────────────────"
curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Qué asignaturas están disponibles en el sistema?"}' | jq . 2>/dev/null || curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Qué asignaturas están disponibles en el sistema?"}'
echo ""
echo ""

# Test 3: Consulta descriptiva
echo "Test 3: Consulta descriptiva"
echo "───────────────────────────────────────────────────────────"
curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Dame un resumen de los datos del sistema escolar"}' | jq . 2>/dev/null || curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Dame un resumen de los datos del sistema escolar"}'
echo ""
echo ""

# Test 4: Consulta con JSON personalizado (sin espacios extras)
echo "Test 4: Consulta personalizada"
echo "───────────────────────────────────────────────────────────"
curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuál es el total de relaciones alumno-asignatura en el sistema?"}' | jq . 2>/dev/null || curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuál es el total de relaciones alumno-asignatura en el sistema?"}'
echo ""
echo ""

# Test 5: Error - prompt vacío
echo "Test 5: Manejo de error (prompt vacío)"
echo "───────────────────────────────────────────────────────────"
echo "Esperado: Bad Request (400)"
curl -s -X POST "$IA_ENDPOINT" \
  -H "Content-Type: application/json" \
  -d '{"prompt":""}' -w "\nStatus: %{http_code}\n"
echo ""
echo ""

echo "═══════════════════════════════════════════════════════════"
echo "Tests completados"
echo "═══════════════════════════════════════════════════════════"

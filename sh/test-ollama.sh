#!/bin/bash

# Script para probar la integración con Ollama
# Uso: ./sh/test-ollama.sh o cd sh && ./test-ollama.sh

# Change to project root directory
cd "$(dirname "$0")/.." || exit 1

echo "======================================"
echo "Test de Integración con Ollama"
echo "======================================"
echo ""

# Verificar que Ollama está corriendo
echo "1. Verificando Ollama..."
if ! curl -s http://localhost:11434/api/tags > /dev/null; then
    echo "❌ ERROR: Ollama no está corriendo"
    echo "   Ejecuta: ollama serve"
    exit 1
fi
echo "✅ Ollama está corriendo"
echo ""

# Verificar que el modelo está instalado
echo "2. Verificando modelo qwen2..."
if ! curl -s http://localhost:11434/api/tags | grep -q "qwen2"; then
    echo "❌ ERROR: Modelo qwen2 no encontrado"
    echo "   Ejecuta: ollama pull qwen2"
    exit 1
fi
echo "✅ Modelo qwen2 instalado"
echo ""

# Verificar que la aplicación está corriendo
echo "3. Verificando aplicación Spring Boot..."
if ! curl -s http://localhost:8080/actuator/health 2>/dev/null > /dev/null; then
    echo "⚠️  Aplicación no está corriendo"
    echo "   Iniciando aplicación..."
    mvn spring-boot:run > /dev/null 2>&1 &
    echo "   Esperando 10 segundos..."
    sleep 10
fi
echo "✅ Aplicación corriendo"
echo ""

# Test 1: Consulta simple
echo "4. Test 1: Consulta simple"
echo "   Prompt: '¿Cuáles son las asignaturas disponibles?'"
RESPONSE=$(curl -s -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt": "¿Cuáles son las asignaturas disponibles?"}' \
  | jq -r '.respuesta' 2>/dev/null)

if [ $? -eq 0 ] && [ ! -z "$RESPONSE" ]; then
    echo "✅ Respuesta recibida:"
    echo ""
    echo "$RESPONSE" | fold -w 70 -s | sed 's/^/   /'
    echo ""
else
    echo "❌ Error al obtener respuesta"
    exit 1
fi
echo ""

# Test 2: Consulta sobre alumnos
echo "5. Test 2: Consulta sobre alumnos"
echo "   Prompt: '¿Cuántos alumnos hay?'"
RESPONSE=$(curl -s -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt": "¿Cuántos alumnos hay?"}' \
  | jq -r '.respuesta' 2>/dev/null)

if [ $? -eq 0 ] && [ ! -z "$RESPONSE" ]; then
    echo "✅ Respuesta recibida:"
    echo ""
    echo "$RESPONSE" | fold -w 70 -s | sed 's/^/   /'
    echo ""
else
    echo "❌ Error al obtener respuesta"
    exit 1
fi
echo ""

# Test 3: Consulta compleja
echo "6. Test 3: Consulta compleja"
echo "   Prompt: 'Lista los alumnos con su email'"
RESPONSE=$(curl -s -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Lista los alumnos con su email"}' \
  | jq -r '.respuesta' 2>/dev/null)

if [ $? -eq 0 ] && [ ! -z "$RESPONSE" ]; then
    echo "✅ Respuesta recibida:"
    echo ""
    echo "$RESPONSE" | fold -w 70 -s | sed 's/^/   /'
    echo ""
else
    echo "❌ Error al obtener respuesta"
    exit 1
fi
echo ""

echo "======================================"
echo "✅ Todos los tests completados"
echo "======================================"
echo ""
echo "Modelo usado: qwen2"
echo "Para cambiar de modelo, edita src/main/resources/application.properties"
echo ""

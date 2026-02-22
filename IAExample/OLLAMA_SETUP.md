# Integración con Ollama - Modelos IA Locales

## ¿Qué es Ollama?

Ollama es una plataforma para ejecutar modelos de IA de forma local en tu equipo. Permite gestionar, descargar y ejecutar múltiples modelos LLM sin necesidad de conexión a Internet ni APIs externas.

## Instalación en Linux (Ubuntu 24.04)

### 1. Instalar Ollama

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

Esto instalará Ollama y lo configurará como servicio systemd.

### 2. Verificar la instalación

```bash
ollama --version
```

### 3. Iniciar el servicio

El servicio se inicia automáticamente, pero puedes verificarlo:

```bash
systemctl status ollama
```

Si no está corriendo:

```bash
ollama serve
```

## Descarga de Modelos

### Modelos Recomendados para MacBook Pro 2015 (16GB RAM)

#### Qwen2 (1.5B) - Ultra Ligero ⭐
```bash
ollama pull qwen2:1.5b
```
- **Tamaño**: ~900MB
- **Rendimiento**: Excelente para recursos limitados
- **Uso**: General, multilingüe

#### Qwen2 (7B) - Default
```bash
ollama pull qwen2
```
- **Tamaño**: ~4.4GB
- **Rendimiento**: Balance óptimo calidad/recursos
- **Uso**: Recomendado para este proyecto

#### Phi-3 Mini (3.8B)
```bash
ollama pull phi3
```
- **Tamaño**: ~2.3GB
- **Rendimiento**: Muy eficiente
- **Uso**: Microsoft, excelente para razonamiento

#### Gemma 2B
```bash
ollama pull gemma:2b
```
- **Tamaño**: ~1.4GB
- **Rendimiento**: Ligero y rápido
- **Uso**: Google, respuestas concisas

#### Llama 3.2 (3B)
```bash
ollama pull llama3.2
```
- **Tamaño**: ~2GB
- **Rendimiento**: Muy bueno
- **Uso**: Meta, versátil

### Listar modelos instalados

```bash
ollama list
```

### Eliminar un modelo

```bash
ollama rm nombre-modelo
```

## Prueba desde Terminal

Para probar un modelo directamente:

```bash
ollama run qwen2 "Explica qué es la programación orientada a objetos"
```

Modo interactivo:
```bash
ollama run qwen2
>>> Hola, ¿cómo estás?
```

Salir del modo interactivo: `/bye`

## Configuración del Proyecto Java

### 1. Configuración en application.properties

El proyecto ya está configurado para usar Ollama. Verifica en `src/main/resources/application.properties`:

```properties
# Ollama Local API Configuration
deepseek.api.url=http://localhost:11434/api/chat
deepseek.api.key=not-required
deepseek.api.model=qwen2
deepseek.api.temperature=0.7
deepseek.api.max-tokens=2000
deepseek.api.timeout=60000
deepseek.api.stream=false
```

### 2. Cambiar de Modelo

Para usar un modelo diferente, modifica la propiedad:

```properties
deepseek.api.model=phi3      # Para usar Phi-3
deepseek.api.model=gemma:2b  # Para usar Gemma 2B
deepseek.api.model=llama3.2  # Para usar Llama 3.2
```

### 3. Ajustar Parámetros

**Temperature** (0.0 - 1.0):
- `0.0`: Respuestas deterministas y precisas
- `0.7`: Balance creatividad/coherencia (recomendado)
- `1.0`: Respuestas más creativas y variadas

```properties
deepseek.api.temperature=0.5
```

**Max Tokens**:
```properties
deepseek.api.max-tokens=1000  # Respuestas más cortas
```

**Timeout**:
```properties
deepseek.api.timeout=120000  # 2 minutos para modelos más lentos
```

## Uso desde la Aplicación

### 1. Asegurarse que Ollama está corriendo

```bash
curl http://localhost:11434/api/tags
```

Si no responde, iniciar:
```bash
ollama serve
```

### 2. Levantar la aplicación Spring Boot

```bash
mvn spring-boot:run
```

### 3. Probar el endpoint

```bash
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{
    "prompt": "¿Cuáles son las asignaturas disponibles?"
  }'
```

## Rendimiento Esperado

### En MacBook Pro 2015 (Intel Iris, CPU)

| Modelo | Tamaño | Tokens/seg | Latencia Primera Respuesta |
|--------|--------|------------|---------------------------|
| qwen2:1.5b | 900MB | 15-25 | 1-2s |
| gemma:2b | 1.4GB | 12-20 | 1-3s |
| phi3 | 2.3GB | 8-15 | 2-4s |
| llama3.2 | 2GB | 10-18 | 2-3s |
| qwen2 (7B) | 4.4GB | 5-10 | 3-8s |

> **Nota**: Los tiempos varían según la longitud del prompt y el contexto cargado.

## Arquitectura de la Integración

```
Cliente HTTP
    ↓
IAController
    ↓
DeepseekService (ahora conecta a Ollama)
    ↓
RestTemplate → http://localhost:11434/api/chat
    ↓
Ollama (localhost)
    ↓
Modelo IA Local (qwen2)
```

## Troubleshooting

### Ollama no responde

```bash
# Verificar si está corriendo
ps aux | grep ollama

# Reiniciar el servicio
systemctl restart ollama

# Ver logs
journalctl -u ollama -f
```

### Error "connection refused"

Verifica que Ollama esté escuchando en el puerto correcto:
```bash
netstat -tlnp | grep 11434
```

### Modelo no encontrado

Asegúrate de haber descargado el modelo:
```bash
ollama list
ollama pull qwen2
```

### Respuestas muy lentas

- Usa un modelo más pequeño (qwen2:1.5b, gemma:2b)
- Reduce `max-tokens` en application.properties
- Cierra otras aplicaciones que consuman RAM

### Consumo alto de memoria

```bash
# Ver uso de memoria
ollama ps

# Descargar modelo no usado de RAM
ollama stop nombre-modelo
```

## API de Ollama

### Endpoints disponibles

- `POST /api/chat` - Chat con el modelo
- `POST /api/generate` - Generación de texto
- `GET /api/tags` - Listar modelos instalados
- `POST /api/pull` - Descargar modelo
- `DELETE /api/delete` - Eliminar modelo

### Ejemplo de request directo

```bash
curl http://localhost:11434/api/chat -d '{
  "model": "qwen2",
  "messages": [
    {
      "role": "user",
      "content": "¿Por qué el cielo es azul?"
    }
  ],
  "stream": false
}'
```

## Ventajas de Usar Ollama

✅ **Privacidad**: Datos procesados localmente
✅ **Sin costos**: No hay límites de API ni cobros
✅ **Sin Internet**: Funciona offline
✅ **Rápido**: Latencia mínima (localhost)
✅ **Flexible**: Múltiples modelos disponibles
✅ **Compatible**: API compatible con OpenAI

## Desventajas

❌ Requiere recursos locales (RAM, CPU)
❌ Calidad inferior a modelos cloud premium (GPT-4, Claude)
❌ Velocidad limitada por hardware

## Próximos Pasos

1. **Experimentar con diferentes modelos** para encontrar el mejor balance
2. **Ajustar temperatura** según el tipo de respuestas deseadas
3. **Implementar streaming** para respuestas en tiempo real
4. **Añadir caché** para consultas frecuentes
5. **Configurar system prompts** personalizados

## Referencias

- [Ollama Documentation](https://github.com/ollama/ollama)
- [Ollama Models Library](https://ollama.com/library)
- [API Reference](https://github.com/ollama/ollama/blob/main/docs/api.md)

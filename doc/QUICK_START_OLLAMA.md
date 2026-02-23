# Guía Rápida: Ollama Local en IAExample

## ✅ Configuración Completada

El proyecto ha sido configurado para usar **Ollama** con el modelo **qwen2** de forma local.

## 📋 Qué Se Ha Modificado

### Archivos Modificados:
1. **src/main/resources/application.properties**
   - URL cambiada a `http://localhost:11434/api/chat`
   - Modelo configurado como `qwen2`
   - Timeout aumentado a 60 segundos
   - Eliminada la validación de API key

2. **src/main/java/com/example/iaexample/service/DeepseekService.java**
   - Removida validación de API key (no necesaria para Ollama)
   - Removido header Authorization (no necesario para Ollama)
   - Mensajes de error actualizados

3. **src/main/java/com/example/iaexample/config/DeepseekProperties.java**
   - Añadida propiedad `stream` para soporte futuro

### Archivos Creados:
1. **OLLAMA_SETUP.md** - Documentación completa de Ollama
2. **application.properties.example** - Configuraciones de ejemplo
3. **sh/test-ollama.sh** - Script de testing automatizado

### Archivos Actualizados:
1. **README.md** - Añadida sección de Ollama
2. **DEEPSEEK.md** - Añadida nota sobre Ollama como alternativa

## 🚀 Inicio Rápido

### 1. Verificar Ollama
```bash
ollama list
```
Deberías ver `qwen2` en la lista.

### 2. Asegurar que Ollama está corriendo
```bash
systemctl status ollama
# o simplemente
curl http://localhost:11434/api/tags
```

### 3. Iniciar la aplicación
```bash
mvn spring-boot:run
```

### 4. Probar la integración
```bash
# Opción 1: Script automático
./sh/test-ollama.sh

# Opción 2: Manualmente
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt": "¿Cuáles son las asignaturas disponibles?"}'
```

## 🔧 Cambiar de Modelo

Para usar otro modelo de Ollama:

1. Descargar el modelo:
```bash
ollama pull phi3
```

2. Editar `application.properties`:
```properties
deepseek.api.model=phi3
```

3. Reiniciar la aplicación

### Modelos Disponibles:
- `qwen2:1.5b` - Ultra ligero (~900MB) ⚡
- `phi3` - Eficiente (~2.3GB) 🎯
- `gemma:2b` - Ligero (~1.4GB) 💎
- `llama3.2` - Versátil (~2GB) 🦙
- `qwen2` - Default (~4.4GB) ⭐

## 📊 Rendimiento Esperado

En tu MacBook Pro 2015:
- **Latencia primera respuesta**: 3-8 segundos
- **Velocidad generación**: 5-10 tokens/segundo
- **Uso RAM**: 5-8GB (modelo + contexto)

## 🐛 Troubleshooting

### Error "Connection refused"
```bash
# Iniciar Ollama
ollama serve
```

### Respuestas muy lentas
- Prueba con `qwen2:1.5b` o `gemma:2b`
- Reduce `max-tokens` en application.properties

### Modelo no encontrado
```bash
ollama pull qwen2
```

## 📚 Documentación Completa

- **[OLLAMA_SETUP.md](OLLAMA_SETUP.md)** - Guía detallada de instalación, configuración y optimización
- **[README.md](../README.md)** - Documentación general del proyecto
- **[DEEPSEEK.md](DEEPSEEK.md)** - Alternativa con Deepseek API

## 🎯 Próximos Pasos

1. **Experimentar con diferentes modelos** para encontrar el mejor balance
2. **Ajustar temperatura** según tus necesidades (0.0 = preciso, 1.0 = creativo)
3. **Optimizar max-tokens** para respuestas más rápidas
4. **Implementar streaming** para respuestas en tiempo real
5. **Añadir system prompts** personalizados para tu dominio

## ⚡ Ventajas de Ollama

✅ **Gratis** - Sin costos de API
✅ **Privado** - Datos procesados localmente
✅ **Sin límites** - Requests ilimitados
✅ **Offline** - No requiere Internet
✅ **Rápido** - Latencia mínima (localhost)
✅ **Compatible** - API tipo OpenAI

¡Disfruta de tu IA local! 🚀

# Ejemplos de Uso del Endpoint de Deepseek

## 1️⃣ Con cURL

### Consulta básica
```bash
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"¿Cuántos alumnos hay en el sistema?"}'
```

### Consulta con salida formateada (requiere jq)
```bash
curl -s -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d '{"prompt":"Información de los alumnos"}' | jq .
```

### Consulta guardando en variable
```bash
PROMPT="¿Cuáles son las asignaturas disponibles?"
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d "{\"prompt\":\"$PROMPT\"}"
```

### Consulta desde archivo
```bash
# Crear archivo prompt.txt con la pregunta
echo '¿Cuántas relaciones alumno-asignatura hay en total?' > prompt.txt

# Usar desde archivo
curl -X POST http://localhost:8080/api/ia/consultar \
  -H "Content-Type: application/json" \
  -d @prompt.txt
```

---

## 2️⃣ Con JavaScript/Fetch API

### Ejemplo básico
```javascript
const prompt = "¿Cuántos alumnos hay en el sistema?";

fetch('http://localhost:8080/api/ia/consultar', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({ prompt: prompt })
})
.then(response => response.json())
.then(data => {
  console.log('Respuesta de IA:', data.respuesta);
  console.log('Tokens utilizados:', data.tokens_utilizados);
  console.log('Modelo:', data.modelo_ia);
})
.catch(error => console.error('Error:', error));
```

### Con async/await
```javascript
async function consultarDeepseek(prompt) {
  try {
    const response = await fetch('http://localhost:8080/api/ia/consultar', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ prompt })
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error al consultar:', error);
  }
}

// Usar
consultarDeepseek("¿Qué asignaturas están disponibles?")
  .then(resultado => console.log(resultado));
```

### Con Axios (requiere instalar: npm install axios)
```javascript
const axios = require('axios');

const consultarDeepseek = async (prompt) => {
  try {
    const response = await axios.post('http://localhost:8080/api/ia/consultar', {
      prompt: prompt
    });
    return response.data;
  } catch (error) {
    console.error('Error:', error.message);
  }
};

// Usar
consultarDeepseek("Dame un resumen del sistema")
  .then(data => console.log(data.respuesta));
```

---

## 3️⃣ Con Python

### Con requests (pip install requests)
```python
import requests
import json

url = 'http://localhost:8080/api/ia/consultar'
headers = {'Content-Type': 'application/json'}
data = {'prompt': '¿Cuántos alumnos hay en el sistema?'}

response = requests.post(url, headers=headers, json=data)

if response.status_code == 200:
    resultado = response.json()
    print(f"Pregunta: {resultado['prompt']}")
    print(f"Respuesta: {resultado['respuesta']}")
    print(f"Tokens: {resultado['tokens_utilizados']}")
    print(f"Modelo: {resultado['modelo_ia']}")
else:
    print(f"Error: {response.status_code}")
    print(response.text)
```

### Script completo con múltiples consultas
```python
import requests
import json

BASE_URL = 'http://localhost:8080'

def consultar_ia(prompt):
    """Consultar el endpoint de IA"""
    try:
        response = requests.post(
            f'{BASE_URL}/api/ia/consultar',
            headers={'Content-Type': 'application/json'},
            json={'prompt': prompt},
            timeout=35  # Más que el timeout del servidor
        )
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error en la consulta: {e}")
        return None

# Ejemplos de consultas
consultas = [
    "¿Cuántos alumnos hay en el sistema?",
    "¿Qué asignaturas están disponibles?",
    "Dame un resumen del sistema escolar"
]

for pregunta in consultas:
    print(f"\n{'='*60}")
    print(f"Pregunta: {pregunta}")
    print('='*60)
    resultado = consultar_ia(pregunta)
    if resultado:
        print(f"Respuesta: {resultado['respuesta']}")
        print(f"Tokens: {resultado['tokens_utilizados']}")
    else:
        print("No se pudo obtener respuesta")
```

---

## 4️⃣ Con PHP

### Básico con cURL
```php
<?php
$prompt = "¿Cuántos alumnos hay?";
$url = 'http://localhost:8080/api/ia/consultar';
$data = json_encode(['prompt' => $prompt]);

$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, $url);
curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
curl_setopt($ch, CURLOPT_POST, true);
curl_setopt($ch, CURLOPT_POSTFIELDS, $data);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_TIMEOUT, 35);

$response = curl_exec($ch);
$httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
curl_close($ch);

if ($httpCode === 200) {
    $resultado = json_decode($response, true);
    echo "Respuesta: " . $resultado['respuesta'] . "\n";
    echo "Tokens: " . $resultado['tokens_utilizados'] . "\n";
} else {
    echo "Error: HTTP $httpCode\n";
}
?>
```

---

## 5️⃣ Con Postman

### Importar colección JSON

Crear archivo `Deepseek-API.postman_collection.json`:

```json
{
  "info": {
    "name": "Deepseek IA API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Consulta Simple",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"prompt\":\"¿Cuántos alumnos hay en el sistema?\"}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ia/consultar",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ia", "consultar"]
        }
      }
    },
    {
      "name": "Consulta Asignaturas",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"prompt\":\"¿Qué asignaturas están disponibles?\"}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ia/consultar",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ia", "consultar"]
        }
      }
    },
    {
      "name": "Consulta Resumen",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"prompt\":\"Dame un resumen del sistema escolar\"}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ia/consultar",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ia", "consultar"]
        }
      }
    }
  ]
}
```

**Pasos para importar:**
1. Abrir Postman
2. Click en "Import"
3. Pegar el JSON o cargar como archivo
4. Seleccionar el workspace
5. Click "Import"

---

## 6️⃣ Con Bash Script

### Script general
```bash
#!/bin/bash

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="${1:-http://localhost:8080}"
PROMPT="${2:-¿Cuántos alumnos hay en el sistema?}"

echo -e "${BLUE}Consultando: ${PROMPT}${NC}"

response=$(curl -s -X POST "$BASE_URL/api/ia/consultar" \
  -H "Content-Type: application/json" \
  -d "{\"prompt\":\"$PROMPT\"}" \
  -w "\n%{http_code}")

# Separar respuesta del status code
http_code=$(echo "$response" | tail -n1)
body=$(echo "$response" | head -n-1)

if [ "$http_code" = "200" ]; then
  echo -e "${GREEN}✓ Éxito${NC}"
  echo "$body" | jq .
else
  echo -e "${RED}✗ Error HTTP $http_code${NC}"
  echo "$body"
fi
```

**Usar:**
```bash
chmod +x consultar.sh
./consultar.sh http://localhost:8080 "¿Qué asignaturas hay?"
```

---

## 7️⃣ Con winget/PowerShell (Windows)

```powershell
$prompt = "¿Cuántos alumnos hay?"
$url = "http://localhost:8080/api/ia/consultar"
$headers = @{"Content-Type" = "application/json"}
$body = @{prompt = $prompt} | ConvertTo-Json

$response = Invoke-WebRequest -Uri $url -Method POST -Headers $headers -Body $body

if ($response.StatusCode -eq 200) {
    $resultado = $response.Content | ConvertFrom-Json
    Write-Host "Respuesta: $($resultado.respuesta)"
    Write-Host "Tokens: $($resultado.tokens_utilizados)"
} else {
    Write-Host "Error: $($response.StatusCode)"
}
```

---

## 8️⃣ Con Java

### Usando RestTemplate
```java
RestTemplate restTemplate = new RestTemplate();
PromptRequest request = new PromptRequest("¿Cuántos alumnos hay?");

ConsultaIAResponse response = restTemplate.postForObject(
    "http://localhost:8080/api/ia/consultar",
    request,
    ConsultaIAResponse.class
);

System.out.println("Respuesta: " + response.getRespuesta());
System.out.println("Tokens: " + response.getTokensUtilizados());
```

---

## 📊 Tabla de Ejemplos Rápidos

| Lenguaje | Herramienta | Comando |
|----------|-------------|---------|
| Shell | curl | `curl -X POST http://localhost:8080/api/ia/consultar -H "Content-Type: application/json" -d '{"prompt":"tu pregunta"}'` |
| JavaScript | Fetch | Ver Sección 2️⃣ |
| Python | requests | Ver Sección 3️⃣ |
| PHP | cURL | Ver Sección 4️⃣ |
| Postman | GUI | Ver Sección 5️⃣ |
| Shell | Script | Ver Sección 6️⃣ |
| PowerShell | WebRequest | Ver Sección 7️⃣ |
| Java | RestTemplate | Ver Sección 8️⃣ |

---

## ⚠️ Notas Importantes

1. **Reemplazar prompts** - Los ejemplos usan prompts fijos, personaliza según tus necesidades
2. **URL base** - Cambiar `http://localhost:8080` por tu URL si está en otro host
3. **API Key** - Asegurar que `DEEPSEEK_API_KEY` esté configurada
4. **Timeout** - Aumentar a 35+ segundos para prompts complejos
5. **Error handling** - Los ejemplos básicos pueden ampliarse con mejor control de errores

---

## ✨ Tips Útiles

- Usar `jq` en Linux/Mac para formatear JSON: `... | jq .`
- En Windows, usar `ConvertFrom-Json` en PowerShell
- Para debugging, activar verbose: `curl -v ...`
- Guardar respuestas: `curl ... > respuesta.json`
- Monitorear uso con: `curl ... | jq '.tokens_utilizados'`

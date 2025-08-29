# Documentación Swagger API - CrediYa Solicitudes

## 📚 Acceso a la documentación

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación Swagger en las siguientes URLs:

### 🌐 URLs de Swagger

- **Swagger UI (Interfaz Web)**: http://localhost:8081/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8081/v3/api-docs
- **API Docs (YAML)**: http://localhost:8081/v3/api-docs.yaml

## 🚀 Endpoints disponibles

### POST /api/v1/solicitudes
Registra una nueva solicitud de préstamo

#### Request Body:
```json
{
  "documentoIdentidad": "12345678",
  "monto": 100000.50,
  "plazo": 12,
  "idTipoPrestamo": 1
}
```

#### Responses:

**200 OK - Éxito:**
```json
{
  "status": 200,
  "mensaje": "Pendiente de revisión"
}
```

**400 Bad Request - Error de validación:**
```json
{
  "status": 400,
  "mensaje": "Usuario no encontrado: 12345678"
}
```

**500 Internal Server Error - Error interno:**
```json
{
  "status": 500,
  "mensaje": "Error interno del servidor"
}
```

## 🔧 Configuración

La configuración de Swagger se encuentra en:

- **SwaggerConfig.java**: Configuración principal de OpenAPI
- **application.yaml**: Configuración de SpringDoc
  ```yaml
  springdoc:
    api-docs:
      path: /v3/api-docs
    swagger-ui:
      path: /swagger-ui.html
      enabled: true
    show-actuator: true
  ```

## 📋 Validaciones incluidas

Los endpoints incluyen las siguientes validaciones documentadas:

- **documentoIdentidad**: Requerido, 6-15 caracteres
- **monto**: Requerido, mayor a 0, máximo 10 enteros y 2 decimales
- **plazo**: Requerido, entre 1 y 60 meses
- **idTipoPrestamo**: Requerido, mayor a 0

## 🎯 Características de la documentación

- ✅ Descripciones detalladas de endpoints
- ✅ Ejemplos de request/response
- ✅ Validaciones de campos documentadas
- ✅ Códigos de estado HTTP
- ✅ Esquemas de datos
- ✅ Interfaz interactiva para probar endpoints
- ✅ Compatible con OpenAPI 3.0

## 🧪 Probando endpoints

1. Abre http://localhost:8081/swagger-ui.html
2. Expande el endpoint POST /api/v1/solicitudes
3. Haz clic en "Try it out"
4. Modifica el JSON de ejemplo
5. Haz clic en "Execute"
6. Revisa la respuesta

¡Disfruta de la documentación interactiva! 🎉
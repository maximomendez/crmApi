# CRM API

API RESTful para la gestión de contactos y etiquetas (tags), desarrollada con Spring Boot y JPA.

## Características

- CRUD de contactos
- CRUD de etiquetas (tags)
- Asociación y desasociación de tags a contactos
- Búsqueda de contactos por tags
- Validaciones y manejo de errores centralizado
- Control de concurrencia optimista (ETag)
- Seeder de datos de ejemplo
- Documentación OpenAPI (Swagger)

## Requisitos

- Java 17+
- Maven 3.8+
- (Por defecto usa H2 en memoria, configurable en `application.properties` o `application.yml`)

## Instalación y ejecución

```bash
git clone https://github.com/tuusuario/tu-repo.git
cd crmapi
./mvnw spring-boot:run
```

La API estará disponible en:  
`http://localhost:8080/`

## Endpoints principales

- `POST   /contacts`           - Crear contacto
- `GET    /contacts`           - Listar contactos
- `GET    /contacts/{id}`      - Obtener contacto por ID
- `PUT    /contacts/{id}`      - Actualizar contacto (requiere ETag)
- `DELETE /contacts/{id}`      - Eliminar contacto
- `POST   /contacts/{id}/tags` - Asociar tag a contacto
- `DELETE /contacts/{id}/tags/{tagId}` - Eliminar tag de contacto


## Swagger

Accede a la documentación Swagger/OpenAPI en:  
`http://localhost:8080/swagger-ui.html`  
o  
`http://localhost:8080/swagger-ui/index.html`

## Configuración

Puedes modificar la configuración de la base de datos y otros parámetros en:
- `src/main/resources/application.properties`
- `src/main/resources/application.yml`

## Semillas de datos

Al iniciar la aplicación, se insertarán los datos automáticamente mediante la clase `DataSeeder`.

## Estructura del proyecto

```
src/
  main/
    java/
      com.pathmonk.crmapi/
        controllers/
        model/
        repo/
        service/
        web/
    resources/
      application.properties
      application.yml
  test/
    java/
      ...
```
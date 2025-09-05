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

# Contact & Quick Add Company (Java + Thymeleaf + Cypress)

Este proyecto incluye un formulario de contacto y un panel para añadir empresas rápidamente, usando Java (Spring Boot), Thymeleaf, jQuery y pruebas automáticas con Cypress.

## 📝 Características nuevas

- **Panel Quick Add Company**: Permite añadir una empresa desde el formulario de contacto sin salir de la página.
- **Validación en tiempo real**: Comprueba si el nombre de la empresa está disponible y valida el formato de la web.
- **Mensajes de éxito/error**: Se muestran alertas al crear una empresa.
- **Pruebas automáticas Cypress**: Tests end-to-end para el panel y la lógica de añadir empresa.

## 🚀 Cómo usar

### 1. Ejecuta la aplicación Java

Asegúrate de tener la app corriendo en local (por ejemplo, en `http://localhost:8080`):

```bash
mvn spring-boot:run
```
o desde tu IDE.

### 2. Instala Cypress para pruebas

Desde la raíz del proyecto, ejecuta:

```bash
npm init -y
npm install cypress --save-dev
```

### 3. Añade los tests de Cypress

Coloca el archivo de pruebas en:

```
crmapi/cypress/e2e/quick-add-company.cy.js
```

### 4. Ejecuta Cypress

Abre la interfaz de Cypress:

```bash
npx cypress open
```

O ejecuta los tests en modo headless:

```bash
npx cypress run
```

### 5. Usa el panel Quick Add Company

- Haz clic en **+ Quick Add Company** en el formulario de contacto.
- Completa los datos y guarda la empresa.
- El sistema valida el nombre y la web, y selecciona la empresa creada en el formulario.

## 📁 Estructura relevante

- `crmapi/src/main/resources/templates/contact.html` — Formulario y panel.
- `crmapi/src/main/resources/static/js/quick-add-company.js` — Lógica JS.
- `crmapi/src/main/resources/static/css/styles.css` — Estilos.
- `crmapi/cypress/e2e/quick-add-company.cy.js` — Tests Cypress.
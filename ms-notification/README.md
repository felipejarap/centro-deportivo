#  Microservicio ms-entrenadores

Este microservicio gestiona el ciclo de vida completa de las notificaciones. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation

##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Entrenadores (`/notifications`)

| Método     | Endpoint | Descripción                          |
|:-----------| :--- |:-------------------------------------|
| **GET**    | `/api/v1/notifications` | Listar todas las notifications       |
| **GET**    | `/api/v1/notifications/{id}` | Obtener detalle de una notifications |
| **POST**   | `/api/v1/notifications` | Crear un nueva notificacion          |



##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).
- **`model`**: Entidad de negocio que mapea a la base de datos (`Notifications`).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

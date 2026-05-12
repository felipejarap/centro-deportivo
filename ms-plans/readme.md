#  Microservicio ms-plans

Este microservicio gestiona el ciclo de vida completo de los planes que ofrece el centro deportivo. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation

##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Clases (`/plans`)

| Método     | Endpoint | Descripción                |
|:-----------| :--- |:---------------------------|
| **GET**    | `/api/v1/plans` | Listar todos los planes    |
| **GET**    | `/api/v1/plans/{id}` | Obtener detalle de un plan |
| **POST**   | `/api/v1/plans` | Crear un nuevo plan        |
| **PUT**    | `/api/v1/plans/{id}` | Modifica un plan           |
| **DELETE** | `/api/v1/plans/{id}` | Eliminar un plan           |


##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

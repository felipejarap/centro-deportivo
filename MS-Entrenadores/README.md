#  Microservicio ms-entrenadores

Este microservicio gestiona el ciclo de vida completo de los entrenadores. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation

##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Entrenadores (`/entrenadores`)

| Método     | Endpoint | Descripción                              |
|:-----------| :--- |:-----------------------------------------|
| **GET**    | `/api/v1/entrenadores` | Listar todos los entrenadores            |
| **GET**    | `/api/v1/entrenadores/{id}` | Obtener detalle de un entrenador         |
| **POST**   | `/api/v1/entrenadores` | Crear un nuevo entrenador                |
| **PUT**    | `/api/v1/entrenadores/{id}` | Modifica un entrenador                   |
| **DELETE** | `/api/v1/entrenadores/{id}` | Eliminar un entrenador                        |


##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).
- **`model`**: Entidad de negocio que mapea a la base de datos (`Entrenador`).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

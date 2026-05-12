#  Microservicio ms-classes

Este microservicio gestiona el ciclo de vida completo de las clases deportivas y sus categorías. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation

##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Clases (`/classes`)

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| **GET** | `/api/v1/classes` | Listar todas las clases |
| **GET** | `/api/v1/classes/{id}` | Obtener detalle de una clase |
| **POST** | `/api/v1/classes` | Crear una nueva clase |
| **GET** | `/api/v1/classes/{id}` | Obtener detalle de una clase |
| **DELETE** | `/api/v1/classes/{id}` | Eliminar una clase |

###  Tipos de Clases (`/type-classes`)

| Método | Endpoint                    | Descripción |
| :--- |:----------------------------| :--- |
| **GET** | `/api/v1/type-classes`      | Listar todas las categorías |
| **GET** | `/api/v1/type-classes/{id}` | Obtener detalle de una clase |
| **POST** | `/api/v1/type-classes`      | Crear nueva categoría |
| **GET** | `/api/v1/type-classes/{id}` | Obtener detalle de una clase |
| **DELETE** | `/api/v1/type-classes/{id}` | Eliminar una categoría |

##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

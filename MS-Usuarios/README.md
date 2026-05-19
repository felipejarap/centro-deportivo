#  Microservicio ms-usuarios

Este microservicio gestiona el ciclo de vida completo de los usuarios y sus tipos. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation

##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Usuarios (`/Users`)

| Método     | Endpoint | Descripción                    |
|:-----------| :--- |:-------------------------------|
| **GET**    | `/api/v1/Users` | Listar todos los usuarios      |
| **GET**    | `/api/v1/Users/{id}` | Obtener detalle de un usuario  |
| **POST**   | `/api/v1/Users` | Crear un nuevo usuario         |
| **PUT**    | `/api/v1/Users/{id}` | Modifica un usuario              |
| **DELETE** | `/api/v1/Users/{id}` | Eliminar un usuario             |

###  Tipos de Usuarios (`/type-users`)

| Método     | Endpoint                    | Descripción                              |
|:-----------|:----------------------------|:-----------------------------------------|
| **GET**    | `/api/v1/type-users`      | Listar todas los tipos de usuarios       |
| **GET**    | `/api/v1/type-users/{id}` | Obtener detalle de un tipo de usuario    |
| **POST**   | `/api/v1/type-users`      | Crear nuevo tipo de usuario              |
| **PUT**    | `/api/v1/type-users/{id}` | Modifica un tipo de usuario              |
| **DELETE** | `/api/v1/type-users/{id}` | Eliminar un tipo de usuario                   |

##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).
- **`model`**: Entidad de negocio que mapea a la base de datos (`User` y `TypeUser`).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

#  Microservicio ms-asistencia

Este microservicio gestiona el ciclo de vida completo de las asistencias de los deportistas. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation


## Comunicación Inter-Servicios

Este microservicio no trabaja aislado; se comunica de forma sincrónica con los siguientes componentes del ecosistema:
**Microservicio de Usuarios (`ms-Usuarios`)**: Utiliza `UsuarioClient` para validar la existencia del usuario  y enriquecer la información del historial con los datos devueltos en `UserResponseDto`.
**Microservicio de Usuarios (`ms-classes`)**: Utiliza `ClasseClient` para validar la existencia del usuario  y enriquecer la información del historial con los datos devueltos en `ClasseResponseDto`.



##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Entrenadores (`/asistencias`)

| Método     | Endpoint | Descripción                                        |
|:-----------| :--- |:---------------------------------------------------|
| **GET**    | `/api/v1/asistencias` | Listar todas las asistencias                       |
| **GET**    | `/api/v1/asistencias/{id}` | Obtener detalle de una asistencia                  |
| **POST**   | `/api/v1/asistencias` | Crear una asistencia                               |
| **PUT**    | `/api/v1/asistencias/{id}` | Modifica una asistencia                            |
| **DELETE** | `/api/v1/asistencias/{id}` | Eliminar una asistencia                            |


##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).
- **`model`**: Entidad de negocio que mapea a la base de datos (`Reservas`).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

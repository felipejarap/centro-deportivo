#  Microservicio ms-medicalRecord

Este microservicio gestiona el ciclo de vida completo de los historiales medicos de los deportistas. Implementa un **CRUD completo** y sigue una arquitectura de capas profesional.

##  Stack Tecnológico
*   **Lenguaje:** Java 21 (LTS)
*   **Framework:** Spring Boot 4.0.6
*   **Base de Datos:** MySQL con **Flyway**
*   **Comunicación:** Spring Cloud OpenFeign
*   **Librerías:** Lombok, Jakarta Validation


## Comunicación Inter-Servicios

Este microservicio no trabaja aislado; se comunica de forma sincrónica con los siguientes componentes del ecosistema:

**Microservicio de Usuarios (`ms_user`)**: Utiliza `UserClient` para validar la existencia del usuario  y enriquecer la información del historial con los datos devueltos en `UserResponseDto`.


##  API Endpoints (CRUD Completo)

La API está versionada bajo el path `/api/v1/`.

###  Gestión de Entrenadores (`/medical-records`)

| Método     | Endpoint | Descripción                            |
|:-----------| :--- |:---------------------------------------|
| **GET**    | `/api/v1/medical-records` | Listar todos los historiales medicos   |
| **GET**    | `/api/v1/medical-records/{id}` | Obtener detalle de un historial medico |
| **POST**   | `/api/v1/medical-records` | Crear un nuevo historial medico            |
| **PUT**    | `/api/v1/medical-records/{id}` | Modifica un historial medico                 |
| **DELETE** | `/api/v1/entrenadores/{id}` | Eliminar un historial medico                 |


##  Organización del Código
Basado en arquitectura limpia:
- **`controller`**: Controladores REST y manejo de excepciones.
- **`service`**: Lógica de negocio e implementaciones.
- **`repository`**: Abstracción de base de datos con JPA.
- **`dto`**: Transferencia de datos segura (Request/Response).
- **`model`**: Entidad de negocio que mapea a la base de datos (`MedicalRecord`).


##  Robustez
- **Validaciones:** Uso de `@Valid` para asegurar datos correctos.
- **Errores:** `ApiExceptionExporter` centraliza todas las respuestas de error.

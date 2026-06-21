package cl.duoc.MS_Usuarios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos públicos y detallados de un usuario")
public class UserResponseDto {

    @NotNull
    @Schema(description = "Identificador único del usuario registrado en la base de datos", example = "45")
    private Long id;
    @NotNull
    @Schema(description = "Nombre de usuario para el inicio de sesión", example = "c.perez")
    private String username;
    @Schema(description = "Apellido paterno del usuario", example = "Perez")
    private String paternalSurname;
    @Schema(description = "Apellido materno del usuario", example = "Gomez")
    private String maternalSurname;
    @Email
    @Schema(description = "Dirección de correo electrónico registrada", example = "cristian.perez@duocuc.cl")
    private String email;
    @Schema(description = "Número telefónico de contacto", example = "+56912345678")
    private String phone;
    @Schema(description = "Detalles de la categoría, rol o tipo de usuario asignado")
    private TypeUserResponseDto typeUser;

}

package cl.duoc.MS_Usuarios.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar un usuario en el sistema")

public class UserRequestDto {

    @NotBlank(message = "El username no puede estar vacio")
    @Size(min = 4,max = 20, message = "el username debe tener entre 4 y 20 caracteres")
    @Schema(description = "Nombre de usuario único para credenciales de acceso", example = "c.perez")
    private String username;
    @NotBlank(message = "El apellido paterno es requerido")
    @Schema(description = "Apellido paterno del usuario", example = "Perez")
    private String paternalSurname;
    @NotBlank(message = "El apellido materno es requerido")
    @Schema(description = "Apellido materno del usuario", example = "Gomez")
    private String maternalSurname;
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "Debe proporcionar un formato de email valido")
    @Schema(description = "Dirección de correo electrónico válida", example = "cristian.perez@duocuc.cl")
    private String email;
    @Schema(description = "Número telefónico de contacto (Opcional)", example = "+56912345678")
    private String phone;
    @NotNull(message = "El tipo de usuario es requerido")
    @Schema(description = "Identificador único de la categoría o rol asignado (Asociado a TypeUser)", example = "1")
    private Long typeUserId;



}

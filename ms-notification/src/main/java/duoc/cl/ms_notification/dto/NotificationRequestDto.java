package duoc.cl.ms_notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para solicitar el envío de una nueva notificación")

public class NotificationRequestDto {
    @NotBlank(message = "El to no debe de estar en blanco")
    @Email(message = "Debe proporcionar un formato de correo electrónico válido")
    @Schema(description = "Dirección de correo electrónico del destinatario", example = "cristian.perez@duocuc.cl")
    String to;
    @NotBlank(message = "El subject no debe de estar en blanco")
    @Schema(description = "Asunto o título principal del mensaje institucional", example = "Confirmación de Reserva de Cancha")
    String subject;
    @NotBlank(message = "El body no debe de estar en blanco")
    @Schema(description = "Contenido completo o cuerpo del mensaje de la notificación", example = "Estimado alumno, su clase de Crossfit ha sido programada con éxito.")
    String body;
}

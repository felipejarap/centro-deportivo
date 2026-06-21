package duoc.cl.ms_notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa el registro público de una notificación enviada")

public class NotificationResponseDto {

     @NotNull
     @Schema(description = "Identificador único de la notificación registrado en la base de datos", example = "1")
     Long id;
     @NotBlank
     @Schema(description = "Dirección de correo electrónico del destinatario", example = "cristian.perez@duocuc.cl")
     String to;
     @NotBlank
     @Schema(description = "Asunto o título oficial del mensaje enviado", example = "Confirmación de Reserva de Cancha")
     String subject;
     @NotBlank
     @Schema(description = "Contenido completo o cuerpo del mensaje que se despachó", example = "Estimado alumno, su clase de Crossfit ha sido programada con éxito.")
     String body;
     @Schema(description = "Fecha y hora exacta en la que el sistema procesó y envió la notificación", example = "2026-06-20T20:30:00")
     LocalDateTime sendDate;
}

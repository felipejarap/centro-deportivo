package cl.duoc.ms_subscription.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de respuesta que representa los datos públicos e informativos de una suscripción")
public class SubscriptionResponseDto {
    @NotNull
    @Schema(description = "Identificador único de la suscripción registrado en la base de datos", example = "1")
    Long id;
    @Schema(description = "Identificador único del usuario dueño de la suscripción", example = "45")
    Long userId;
    @Schema(description = "Identificador único del plan contratado", example = "2")
    Long plansId;
    @Schema(description = "Fecha y hora exacta de la expiración de la membresía", example = "2026-07-20T23:59:59")
    LocalDateTime endDate;
    @Schema(description = "Estado de vigencia de la suscripción (true = Activa / false = Expirada)", example = "true")
    boolean state;

}

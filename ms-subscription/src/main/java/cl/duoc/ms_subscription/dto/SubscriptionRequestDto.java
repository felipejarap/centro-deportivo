package cl.duoc.ms_subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
@Schema(description = "Modelo de entrada requerido para registrar o actualizar una suscripción de usuario")
public class SubscriptionRequestDto {

    @Schema(description = "Identificador único de la suscripción (Solo requerido para actualizaciones)", example = "1")
    Long id;
    @NotNull
    @Schema(description = "Identificador único del usuario que adquiere la suscripción (Proveniente de ms-users)", example = "45")
    Long userId;
    @NotNull
    @Schema(description = "Identificador único del plan que se va a asociar (Proveniente de ms-plans)", example = "2")
    Long plansId;
    @Schema(description = "Fecha y hora en que finalizará la suscripción", example = "2026-07-20T23:59:59")
    LocalDateTime endDate;
    @Schema(description = "Estado actual de la suscripción (true = Activa, false = Inactiva)", example = "true")
    boolean state;
}

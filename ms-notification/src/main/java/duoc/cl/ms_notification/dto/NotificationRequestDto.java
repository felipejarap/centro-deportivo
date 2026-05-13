package duoc.cl.ms_notification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class NotificationRequestDto {
    @NotBlank(message = "El to no debe de estar en blanco")
    @Email(message = "Debe proporcionar un formato de correo electrónico válido")
     String to;
    @NotBlank(message = "El subject no debe de estar en blanco")
     String subject;
    @NotBlank(message = "El body no debe de estar en blanco")
     String body;
}

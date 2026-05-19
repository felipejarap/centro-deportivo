package cl.duoc.ms_reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservaRequestDto {

    @NotNull(message = "El id de usuario es requerido")
    private Long idUsuario;

    @NotNull(message = "El id de clase es requerido")
    private Long idClase;

    @NotNull(message = "El id de entrenador es requerido")
    private Long idEntrenador;

    @NotBlank(message = "La fecha de reserva no puede estar vacía")
    private String fechaReserva;

    @NotBlank(message = "El estado de reserva no puede estar vacío")
    private String estadoReserva;
}

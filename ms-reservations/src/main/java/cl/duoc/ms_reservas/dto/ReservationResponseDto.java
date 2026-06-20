package cl.duoc.ms_reservas.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long idReservation;
    private Long idUser;
    private Long idClasse;
    private Long idCoach;
    private String reservationDate;
    private String reservationStatus;

    // Datos enriquecidos desde otros microservicios
    private UserResponseDto user;
    private ClasseResponseDto classe;
    private CoachResponseDto coach;
}

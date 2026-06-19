package cl.duoc.ms.asistencia.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssistanceResponseDto {

    private Long idAssistance;
    private Long idUser;
    private Long idClasse;
    private String arrivalTime;
    private Boolean assist;

    private UserResponseDto user;
    private ClasseResponseDto classe;
}

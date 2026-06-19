package cl.Duoc.MS_Entrenadores.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoachResponseDto {


    private Long idCoach;
    private String name;
    private String paternalSurname;
    private String maternalSurname;
    private String specialty;
    private String certification;

}

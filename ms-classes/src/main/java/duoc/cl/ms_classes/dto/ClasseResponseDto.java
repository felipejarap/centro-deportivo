package duoc.cl.ms_classes.dto;

import duoc.cl.ms_classes.model.TypeClasses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ClasseResponseDto {

    @NotNull
    Long id;

    LocalDateTime startDate;

    LocalDateTime endDate;
    @PositiveOrZero
    Integer maximumCapacity;
    @PositiveOrZero
    Integer spotsAvailable;
    @NotNull
    TypeClasses typeClasse;
}

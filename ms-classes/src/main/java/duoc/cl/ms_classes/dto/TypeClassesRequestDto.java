package duoc.cl.ms_classes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor

public class TypeClassesRequestDto {

    Long id;
    @NotBlank(message = "El name no puede estar en blanco")
    String name;
}

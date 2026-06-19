package cl.Duoc.MS_Entrenadores.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coach {
//agregar la comunicacion de id_usuario y rol cuando haga la comunicacion de microservicios
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coach")
    private Long idCoach;


    @Column(name= "name",nullable = false)
    private String name;

    @NotBlank(message = "El apellido no debe estar en blanco")
    @Column(name= "paternal_surname",nullable = false)
    private String paternalSurname;

    @Column(name= "maternal_surname")
    private String maternalSurname;

    @NotBlank(message = "La especialidad no debe estar en blanco")
    @Column(name= "specialty",nullable = false)
    private String specialty;

    @NotBlank(message = "La certificacion no debe estar en blanco")
    @Column(name= "certification",nullable = false)
    private String certification;



}

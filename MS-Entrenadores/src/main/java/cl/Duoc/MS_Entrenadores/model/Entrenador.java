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
public class Entrenador {
//agregar la comunicacion de id_usuario y rol cuando haga la comunicacion de microservicios
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Entrenador")
    private Long idEntrenador;


    @Column(name= "nombre",nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido no debe estar en blanco")
    @Column(name= "appaterno",nullable = false)
    private String appaterno;

    @Column(name= "apmaterno")
    private String apmaterno;

    @NotBlank(message = "La especialidad no debe estar en blanco")
    @Column(name= "especialidad",nullable = false)
    private String especialidad;

    @NotBlank(message = "La certificacion no debe estar en blanco")
    @Column(name= "certificacion",nullable = false)
    private String certificacion;



}

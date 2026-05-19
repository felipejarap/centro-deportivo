package cl.duoc.MS_Usuarios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id_user")
    private Long idUser;

    @NotBlank
    @Column(unique = true,nullable = false)
    private String username;

    @Column(name = "appaterno")
    private String appaterno;

    @Column(name ="apmaterno")
    private String apmaterno;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Column(name ="phone")
    private String phone;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type_user", referencedColumnName = "id")
    private TypeUser typeUser;



}

package duoc.cl.ms_medicalRecord.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class UserResponseDto {

    Long id;
    String username;
    String appaterno;
    String apmaterno;
    String email;
    String phone;
    //private TypeUserResponseDto typeUser;
}

package cronjob.example.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Data
@Getter
@Setter
public class UserRequestDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String information;

}

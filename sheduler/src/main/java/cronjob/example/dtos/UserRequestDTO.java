package cronjob.example.dtos;


import lombok.Data;

import java.util.UUID;
@Data
public class UserRequestDTO {

    private String fistName;
    private String lastName;
    private String email;
    private String information;

}

package cronjob.example.dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String info;
}

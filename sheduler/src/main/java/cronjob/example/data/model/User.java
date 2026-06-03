package cronjob.example.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class User {

    @Id
    private UUID id;

    private String firstName;
    private String lastName;
    private String email;
    private String information;


}

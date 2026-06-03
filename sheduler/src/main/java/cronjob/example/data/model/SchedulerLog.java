package cronjob.example.data.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "scheduler_log")
@Getter
@Setter
public class SchedulerLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String information;

    private String status;

    @Column(name = "retry_count")
    private int retryCount;

    @Column(name = "error_message")
    private String errorMessage;
}
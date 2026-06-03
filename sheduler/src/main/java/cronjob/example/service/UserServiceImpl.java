package cronjob.example.service;


import cronjob.example.data.model.SchedulerLog;
import cronjob.example.data.model.User;
import cronjob.example.data.repository.SchedulerLogRepository;
import cronjob.example.data.repository.UserRepository;
import cronjob.example.dtos.UserRequestDTO;
import cronjob.example.dtos.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchedulerLogRepository schedulerLogRepository;

    @Override
    public UserResponseDTO addInfo(UserRequestDTO userDTO) {

        if (userDTO.getInformation() == null || userDTO.getInformation().isBlank()) {
            throw new IllegalArgumentException("Information cannot be empty");
        }

        if (userDTO.getEmail() == null || userDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userDTO.getFirstName() == null || userDTO.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }

        if (userDTO.getLastName() == null || userDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        SchedulerLog log = new SchedulerLog();

        log.setFirstName(userDTO.getFirstName());
        log.setLastName(userDTO.getLastName());
        log.setEmail(userDTO.getEmail());
        log.setInformation(userDTO.getInformation());

        log.setStatus("PENDING");
        log.setRetryCount(0);

        schedulerLogRepository.save(log);

        UserResponseDTO response = new UserResponseDTO();

        response.setId(log.getId());
        response.setFirstName(log.getFirstName());
        response.setLastName(log.getLastName());
        response.setEmail(log.getEmail());
        response.setInfo(log.getInformation());

        return response;
    }

    @Override
    public void processPendingUsersCron() {

        List<SchedulerLog> pendingUsers =
                schedulerLogRepository.findPendingOrRetrying();

        if (pendingUsers.isEmpty()) {
            return;
        }

        for (SchedulerLog log : pendingUsers) {

            int currentAttempts = log.getRetryCount();

            try {

                System.out.println(
                        "Processing user: "
                                + log.getEmail()
                                + " | Attempt: "
                                + (currentAttempts + 1)
                );

                // Simulate external API
                simulateExternalApiCrash(500);

                // Save successful user
                User user = new User();

                user.setFirstName(log.getFirstName());
                user.setLastName(log.getLastName());
                user.setEmail(log.getEmail());
                user.setInformation(log.getInformation());

                userRepository.save(user);

                // Update log
                log.setStatus("COMPLETED");
                log.setErrorMessage("Success");

                schedulerLogRepository.save(log);

            } catch (RuntimeException ex) {

                currentAttempts++;

                String errorReason =
                        ex.getMessage() != null
                                ? ex.getMessage()
                                : "Internal Server Error";

                log.setRetryCount(currentAttempts);
                log.setErrorMessage(errorReason);

                if (currentAttempts >= 3) {

                    log.setStatus("FAILED");

                    sendFailureEmail(
                            log.getEmail(),
                            errorReason
                    );

                } else {

                    log.setStatus("RETRYING");
                }

                schedulerLogRepository.save(log);
            }
        }
    }

    private void simulateExternalApiCrash(int code) {
        if (code == 500) {
            throw new RuntimeException("Internal Server Error (Simulated 500)");
        }
    }

    private void sendFailureEmail(String email, String errorReason) {
        System.out.println(
                "EMAIL SENT TO ADMIN: Registration failed for "
                        + email
                        + ". Error: "
                        + errorReason
        );
    }
}
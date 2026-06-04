package cronjob.example.service;


import cronjob.example.data.model.SchedulerLog;
import cronjob.example.data.model.User;
import cronjob.example.data.repository.SchedulerLogRepository;
import cronjob.example.data.repository.UserRepository;
import cronjob.example.dtos.UserRequestDTO;
import cronjob.example.dtos.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserServiceImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchedulerLogRepository schedulerLogRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public UserResponseDTO addInfo(UserRequestDTO userDTO) {

        SchedulerLog log = new SchedulerLog();

        log.setFirstName(userDTO.getFirstName());
        log.setLastName(userDTO.getLastName());
        log.setEmail(userDTO.getEmail());
        log.setInformation(userDTO.getInformation());

        log.setRetryCount(0);
        log.setStatus("PENDING");
        log.setErrorMessage(null);

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

        List<SchedulerLog> pendingUsers = schedulerLogRepository.findPendingOrRetrying();

        if (pendingUsers.isEmpty()) {
            System.out.println("No pending users found.");
            return;
        }

        for (SchedulerLog log : pendingUsers) {

            try {

                System.out.println("Processing user: " + log.getEmail() + " | Attempt: " + (log.getRetryCount() + 1));

                validateRecord(log);
                if (userRepository.existsByEmail(log.getEmail())) {

                    log.setStatus("FAILED");
                    log.setErrorMessage("User already exists with email: " + log.getEmail());

                    schedulerLogRepository.save(log);

                    System.out.println("FAILED permanently (duplicate email): " + log.getEmail());

                    continue;
                }

                User user = new User();

                user.setFirstName(log.getFirstName());
                user.setLastName(log.getLastName());
                user.setEmail(log.getEmail());
                user.setInformation(log.getInformation());

                userRepository.saveAndFlush(user);

                log.setStatus("COMPLETED");
                log.setErrorMessage("Success");

                schedulerLogRepository.delete(log);

                System.out.println("Successfully processed: " + log.getEmail());

            } catch (Exception ex) {

                int currentAttempts = log.getRetryCount() + 1;
                log.setRetryCount(currentAttempts);
                String errorReason = ex.getMessage() != null ? ex.getMessage() : "Unknown error";
                log.setErrorMessage(errorReason);
                if (currentAttempts >= 3) {
                    log.setStatus("FAILED");
                    sendFailureEmail(log.getEmail(), errorReason);
                    System.out.println("FAILED permanently: " + log.getEmail());
                } else {
                    log.setStatus("RETRYING");
                    System.out.println("Retrying " + log.getEmail() + " | Retry Count: " + currentAttempts);
                }

                schedulerLogRepository.saveAndFlush(log);
            }
        }
    }

    private void validateRecord(SchedulerLog log) {

        if (log.getFirstName() == null || log.getFirstName().isBlank()) {
            throw new RuntimeException("First name is missing");
        }

        if (log.getLastName() == null ||
                log.getLastName().isBlank()) {
            throw new RuntimeException("Last name is missing");
        }

        if (log.getEmail() == null ||
                log.getEmail().isBlank()) {
            throw new RuntimeException("Email is missing");
        }

        if (log.getInformation() == null || log.getInformation().isBlank()) throw new RuntimeException("Information is missing");

    }

    private void sendFailureEmail(String email, String errorReason) {
        System.out.println("EMAIL SENT TO ADMIN: Registration failed for " + email + ". Error: " + errorReason);
        try {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo("admin@yourcompany.com");
            message.setSubject("User Registration Failed");

            message.setText(
                    "Registration failed.\n\n" +
                            "Email: " + email + "\n" +
                            "Reason: " + errorReason
            );

            mailSender.send(message);

        } catch (Exception ex) {

            System.err.println("Failed to send notification email: " + ex.getMessage());
        }

    }
}
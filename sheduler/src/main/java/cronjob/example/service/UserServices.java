package cronjob.example.service;

import cronjob.example.dtos.UserRequestDTO;
import cronjob.example.dtos.UserResponseDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


public interface UserServices {

    UserResponseDTO addInfo(UserRequestDTO userDTO);

    void processPendingUsersCron();
}

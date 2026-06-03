package cronjob.example.service;

import cronjob.example.dtos.UserRequestDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;


public interface UserServices {

    UserRequestDTO addInfo(UserRequestDTO userDTO);
}

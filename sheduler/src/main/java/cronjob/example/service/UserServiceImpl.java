package cronjob.example.service;


import cronjob.example.data.model.User;
import cronjob.example.dtos.UserRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UserServiceImpl implements UserServices{


     @Override
     public UserRequestDTO addInfo(UserRequestDTO userDTO) {

         return null;
     }
 }

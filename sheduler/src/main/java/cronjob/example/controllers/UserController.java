package cronjob.example.controllers;

import cronjob.example.dtos.UserRequestDTO;
import cronjob.example.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserController {

   @Autowired
   private UserServices userServices;


    @GetMapping("/home")
    public String welcomeController(){
        return "HOMEPAGE FOR TEST SHEDULER";
    }

    @PostMapping("/addinfo")
    public ResponseEntity<?> addInfo(@RequestBody UserRequestDTO userDTO){
        try {
            UserRequestDTO Response = userServices.addInfo(userDTO);
            return new ResponseEntity<>(Response , HttpStatus.OK);
        }catch(Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}

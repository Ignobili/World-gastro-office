package attendance.system.wgo.Controller;

import attendance.system.wgo.DTO.Users;
import attendance.system.wgo.Repository.UserRepository;
import attendance.system.wgo.Service.UsersService;
import attendance.system.wgo.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
public class UsersController {

    @Autowired
    UsersService usersService;


    UserRepository userRepository;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String fullname, @RequestParam String password, @RequestParam String role) {
        LocalDate timestamp = LocalDate.now();
        return usersService.registerUser(username,fullname,password, Role.valueOf(role));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        return usersService.loginUser(username, password);
    }

    @PostMapping("/logout")
    public  ResponseEntity<String> logoutUser(@RequestParam String username){
        return  usersService.logoutUser(username);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String deleteUsername, @RequestParam String loggedUsername) {

        return  usersService.deleteUser(deleteUsername,loggedUsername);
    }
    @GetMapping("/all")
    public List<Users> getAll(){
        return usersService.getAll();
    }
}

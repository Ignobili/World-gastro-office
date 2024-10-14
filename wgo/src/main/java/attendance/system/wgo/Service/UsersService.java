package attendance.system.wgo.Service;


import attendance.system.wgo.DTO.Users;
import attendance.system.wgo.enums.Role;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UsersService {

    ResponseEntity<String> registerUser(String username, String fullname, String password, Role role);
    ResponseEntity<String> loginUser(String username, String password);
    ResponseEntity<String> logoutUser(String username);
    ResponseEntity<String> deleteUser(String username,String loggedUsername);
    Role getRole(String username);

    List<Users> getAll();
}

package attendance.system.wgo.Service.Implementation;

import attendance.system.wgo.DTO.Users;
import attendance.system.wgo.Repository.UserRepository;
import attendance.system.wgo.Security.JwtUtil;
import attendance.system.wgo.Service.UsersService;
import attendance.system.wgo.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static attendance.system.wgo.enums.Role.*;

@Service
public class UsersServiceImp implements UsersService {

    String token;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;


    private Map<String, String> tokenStore = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private Authentication authentication;

    @Override
    public ResponseEntity<String> registerUser(String username, String fullname, String password, Role role) {

        String encodedPassword = passwordEncoder.encode(password); // corrected variable name
        LocalDate timestamp = LocalDate.now();

        try {
            Users existingUser = userRepository.findByUsername(username);

            if (existingUser != null) {
                return ResponseEntity.internalServerError().body(String.format("Používateľ %s už existuje", username.formatted())); // formatted with username
            } else {
                Users newUser = new Users(username, fullname, encodedPassword, role, timestamp);
                userRepository.save(newUser);
                return ResponseEntity.ok().body(String.format("Používateľ %s úspešne založený", username.formatted())); // formatted with username
            }

        } catch (Exception exception) {
            // Optionally log the exception here
            return ResponseEntity.internalServerError().body("Chyba pri vytváraní používateľa");
        }
    }

    @Override
    public ResponseEntity<String> loginUser(String username, String password) {

        try {
            // Nájdeme používateľa podľa jeho mena
            Users user = userRepository.findByUsername(username);

            // Skontrolujeme, či používateľ existuje
            if (user == null) {
                return ResponseEntity.internalServerError().body("Použivateľ: %s neexistuje".formatted(username));
            }

            // Overíme, či sa zadané heslo zhoduje s uloženým heslom
            if (passwordEncoder.matches(password, (user.getPassword()))) {
                token = jwtUtil.generateToken(username);
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
                setAuthentication(new UsernamePasswordAuthenticationToken(username, password, authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                tokenStore.put(username, token);
                return ResponseEntity.ok().body("Úspešne prihlásený používateľ: %s".formatted(jwtUtil.extractUsername(token)));
            } else {
                return ResponseEntity.internalServerError().body("Nesprávane heslo");
            }
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }

    }

    @Override
    public ResponseEntity<String> deleteUser(String username, String loggedUsername) {

        try {
            if(!username.equals(loggedUsername)){
            // Check if the user exists
            Users user = userRepository.findByUsername(username);
            if (user == null) {
                return ResponseEntity.internalServerError().body("Použivateľ: %s neexistuje".formatted(username));
            }
            // Delete the user

            Users users = userRepository.findByUsername(loggedUsername);
            if (tokenStore.containsKey(users.getUsername())) {
                if (users.getRole() != EMPLOYEE) {
                    userRepository.deleteById(user.getUserId());
                    return ResponseEntity.ok().body("Používateľ %s úspešne vymazaný!".formatted(username));
                } else {
                    return ResponseEntity.internalServerError().body("Nemáte oprávnenie na vymazanie používateľa!");
                }
            } else {
                return ResponseEntity.internalServerError().body("Používateľ nie je prihlásený!");
            }
        } else {
                return ResponseEntity.internalServerError().body("Nemôžete seba vymazať!");
            }
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("Chyba pri vymazaní používateľa!");
        }

    }

    @Override
    public List<Users> getAll() {
        System.out.println(tokenStore);
        return userRepository.findAll();
    }

    public ResponseEntity<String> logoutUser(String username) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                if (jwtUtil.validateToken(tokenStore.get(username), username)) {
                    if (tokenStore.containsKey(username)) {
                        tokenStore.remove(username);                 // Clear the authentication from the security context
                    }
                }
                return ResponseEntity.ok().body("Používateľ %s bol úspešne odhlásený".formatted(username));
            } else {
                return ResponseEntity.badRequest().body("Používateľ %s nie je prihlásený.".formatted(username));
            }
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body("Vyskytla sa chyba počas odhlasovania.");
        }
    }


    private Role getRole(String username) {
        Users users = userRepository.findByUsername(username);
        return users.getRole();
    }
}

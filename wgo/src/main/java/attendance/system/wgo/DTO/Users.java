package attendance.system.wgo.DTO;

import attendance.system.wgo.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Setter
    @Getter
    private String username;

    @Getter
    private String fullname;

    private String password;

    @Setter
    @Column(name = "role")
    private Role role;

    @Setter
    @Column(name = "created_at")
    private LocalDate timestamp;

    public Users(String username, String fullname, String password, Role role, LocalDate timestamp) {

        this.username = username;
        this.fullname = fullname;
        this.password = password;
        this.role = role;
        this.timestamp = timestamp;
    }
}

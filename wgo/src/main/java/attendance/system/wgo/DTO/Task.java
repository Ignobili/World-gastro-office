package attendance.system.wgo.DTO;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    @Getter
    @Setter
    private String taskName;

    @Getter
    @Setter
    private String description;

    @Setter
    @Column(name = "created_at")
    LocalDate timestamp;

    @Setter
    @Getter
    private String username;
}

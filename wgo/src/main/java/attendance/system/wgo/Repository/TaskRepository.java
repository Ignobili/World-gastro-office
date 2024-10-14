package attendance.system.wgo.Repository;

import attendance.system.wgo.DTO.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}

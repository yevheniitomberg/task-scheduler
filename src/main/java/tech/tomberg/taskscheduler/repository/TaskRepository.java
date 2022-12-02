package tech.tomberg.taskscheduler.repository;

import tech.tomberg.taskscheduler.entity.Task;
import tech.tomberg.taskscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUserAndDone(User user, boolean done);
}
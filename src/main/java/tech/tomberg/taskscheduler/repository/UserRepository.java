package tech.tomberg.taskscheduler.repository;

import tech.tomberg.taskscheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    User findByUsername(String username);
}
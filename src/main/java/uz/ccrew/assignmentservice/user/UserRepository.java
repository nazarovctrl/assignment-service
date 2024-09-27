package uz.ccrew.assignmentservice.user;

import org.springframework.data.jpa.repository.Query;
import uz.ccrew.assignmentservice.base.BasicRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BasicRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query("""
            select w.login
              from User w
             where w.role = 'EMPLOYEE'
            """)
    List<String> findEmployees();
}
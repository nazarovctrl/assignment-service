package uz.ccrew.assignmentservice.user;

import uz.ccrew.assignmentservice.base.BasicRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BasicRepository<User, Long> {
    Optional<User> findByLogin(String login);

    @Query("""
            select w.login
              from User w
             where w.role in ('EMPLOYEE','MANAGER')
            """)
    List<String> findEmployeesAndManager();

    Page<User> findByRole(UserRole role, Pageable pageable);
}
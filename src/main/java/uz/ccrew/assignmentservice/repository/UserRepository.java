package uz.ccrew.assignmentservice.repository;

import uz.ccrew.assignmentservice.entity.User;

import java.util.Optional;

public interface UserRepository extends BasicRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
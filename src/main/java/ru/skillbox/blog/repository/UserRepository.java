package ru.skillbox.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.blog.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> getByEmailIgnoreCase(String email);

    Optional<User> getByCode(String code);
}

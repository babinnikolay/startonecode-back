package ru.hukola.startonecodeback.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByNameIgnoreCase(String name);
}

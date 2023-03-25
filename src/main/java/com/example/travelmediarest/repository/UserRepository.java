package com.example.travelmediarest.repository;

import com.example.travelmediarest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByMail(String mail);

    @Query("select case when count (u) > 0 then true else false end from User u where u.mail = ?1")
    boolean selectExistsEmail(String mail);

}

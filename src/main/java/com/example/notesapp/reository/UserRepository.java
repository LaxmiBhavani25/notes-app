
package com.example.notesapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.notesapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}

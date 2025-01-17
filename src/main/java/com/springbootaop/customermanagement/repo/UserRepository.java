package com.springbootaop.customermanagement.repo;

import com.springbootaop.customermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

//Specifies that the ID field in the User entity is a String.
public interface UserRepository extends JpaRepository<User, String> {
}

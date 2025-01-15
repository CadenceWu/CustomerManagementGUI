package com.springbootaop.customermanagement.repo;

import com.springbootaop.customermanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}

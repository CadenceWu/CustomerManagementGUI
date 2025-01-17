package com.springbootaop.customermanagement.service;

import com.springbootaop.customermanagement.model.User;
import com.springbootaop.customermanagement.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    /* A work factor of 10 means 2^10 = 1024 iterations.
     * Higher work factor → Slower hashing → More secure.
     * default is 10.*/
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public boolean authenticate(String username, String password) {
        System.out.println("Authenticating user: " + username);
        
        User user = userRepository.findById(username).orElse(null);
        if (user == null) {
            System.out.println("User not found");
            return false;
        }
        
        System.out.println("Found user: " + user); // Debug print
        System.out.println("Provided password: " + password);
        
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        System.out.println("Password match result: " + matches);
        
        return matches;
    }

    public void createDefaultUsers() {
        try {
            if (userRepository.count() == 0) {
                System.out.println("Creating default users...");
                
                // Clear existing users first
                userRepository.deleteAll();
                
                // Create admin user
                String adminPass = passwordEncoder.encode("admin123");
                System.out.println("Admin encoded password: " + adminPass);
                User admin = new User("admin", adminPass, "ADMIN");
                userRepository.save(admin);
                
                // Create regular user
                String userPass = passwordEncoder.encode("user123");
                System.out.println("User encoded password: " + userPass);
                User user = new User("user", userPass, "USER");
                userRepository.save(user);
                
                System.out.println("Default users created successfully");
                
                // Verify users were created
                userRepository.findAll().forEach(u -> 
                    System.out.println("Created user: " + u));
            }
        } catch (Exception e) {
            System.out.println("Error creating default users: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Add this method to recreate users for testing
    public void recreateDefaultUsers() {
        System.out.println("Recreating default users...");
        createDefaultUsers();
    }
}

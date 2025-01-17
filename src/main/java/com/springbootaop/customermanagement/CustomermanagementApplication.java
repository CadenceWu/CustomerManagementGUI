package com.springbootaop.customermanagement;

import javafx.application.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.springbootaop.customermanagement.controller.FXMLController;
import com.springbootaop.customermanagement.controller.LoginController;
import com.springbootaop.customermanagement.service.UserService;


@SpringBootApplication
@EnableAspectJAutoProxy //Enable AOP in Spring.
public class CustomermanagementApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext springContext = SpringApplication.run(CustomermanagementApplication.class, args);
        
        try {
            UserService userService = springContext.getBean(UserService.class);
            // Force recreate users for testing
            userService.recreateDefaultUsers();
        } catch (Exception e) {
            System.out.println("Error setting up users: " + e.getMessage());
            e.printStackTrace();
        }
        
        JavaFXApplication.setApplicationContext(springContext); // Passing the Spring application context to the JavaFXApplication.
        LoginController.setApplicationContext(springContext);
        FXMLController.setApplicationContext(springContext);  
        Application.launch(JavaFXApplication.class, args); //Launching a JavaFX application. Invoking the start() method.
    }
}

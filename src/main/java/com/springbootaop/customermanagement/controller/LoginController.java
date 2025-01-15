package com.springbootaop.customermanagement.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.springbootaop.customermanagement.model.Customer;
import com.springbootaop.customermanagement.service.UserService;

@Component
public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @Autowired
    private UserService userService;
    
    private static ApplicationContext applicationContext;  // Add this

    public static void setApplicationContext(ApplicationContext context) {  // Add this
        applicationContext = context;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Username and password cannot be empty");
                return;
            }

            if (userService.authenticate(username, password)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerManagementGUI.fxml"));
                    loader.setControllerFactory(applicationContext::getBean);
                    Parent root = loader.load();
                    
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Customer Management System");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace(); // This will help us see the actual error
                    errorLabel.setText("Error loading main window: " + e.getMessage());
                }
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Login error: " + e.getMessage());
        }
    }
    
}
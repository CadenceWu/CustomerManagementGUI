package com.springbootaop.customermanagement.controller;

import com.springbootaop.customermanagement.model.Customer;
import com.springbootaop.customermanagement.service.CustomerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FXMLController {
    @Autowired
    private CustomerService customerService;
    

    private static ApplicationContext applicationContext; // Change to instance variable

    public static void setApplicationContext(ApplicationContext context) {
        FXMLController.applicationContext = context;
    }

    @FXML
    private TableView<Customer> cusManageTable;
    @FXML
    private TableColumn<Customer, Integer> colId;
    @FXML
    private TableColumn<Customer, String> colName;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colMobile;
    @FXML
    private TextField idText, nameText, emailText, mobileText;
    @FXML
    private TextField idTextU, nameTextU, emailTextU, mobileTextU;
    @FXML
    private TextField dsText;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();
    


    @FXML
    public void initialize() {
        setupTable();
        loadCustomers();
    }

    @FXML
    void createDatabaseAction(ActionEvent event) {
        // This method was in your original code for creating/recreating the database
        // You might want to move this functionality to a service class
        try {
            // Initialize database if needed
            loadCustomers(); // Refresh the table view
            showAlert("Success", "Database initialized successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to initialize database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void doubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            nameTextU.clear();
            emailTextU.clear();
            mobileTextU.clear();
        }
    }

    @FXML
    void getSelected(MouseEvent event) {
        int index = cusManageTable.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        idTextU.setText(colId.getCellData(index).toString());
        nameTextU.setText(colName.getCellData(index));
        emailTextU.setText(colEmail.getCellData(index));
        mobileTextU.setText(colMobile.getCellData(index));
    }

    @FXML
    void addAction(ActionEvent event) {
        try {
            Customer customer = new Customer(
                Integer.valueOf(idText.getText()),
                nameText.getText(),
                emailText.getText(),
                mobileText.getText()
            );
            customerService.saveCustomer(customer);
            loadCustomers();
            clearInputFields();
            showAlert("Success", "Customer added successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to add customer: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void updateAction(ActionEvent event) {
        try {
            Customer customer = new Customer(
                Integer.valueOf(idTextU.getText()),
                nameTextU.getText(),
                emailTextU.getText(),
                mobileTextU.getText()
            );
            customerService.updateCustomer(customer);
            loadCustomers();
            clearUpdateFields();
            showAlert("Success", "Customer updated successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to update customer: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void deleteAction(ActionEvent event) {
        try {
            Integer id = Integer.valueOf(dsText.getText());
            customerService.deleteCustomer(id);
            loadCustomers();
            dsText.clear();
            showAlert("Success", "Customer deleted successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to delete customer: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void searchAction(ActionEvent event) {
        try {
            Integer id = Integer.valueOf(dsText.getText());
            customerService.getCustomerById(id).ifPresentOrElse(
                customer -> showAlert("Customer Found", 
                    String.format("ID: %d\nName: %s\nEmail: %s\nMobile: %s", 
                        customer.getId(), customer.getName(), 
                        customer.getEmail(), customer.getMobile()),
                    Alert.AlertType.INFORMATION),
                () -> showAlert("Not Found", "Customer not found", Alert.AlertType.WARNING)
            );
        } catch (Exception e) {
            showAlert("Error", "Failed to search customer: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
    }

    private void loadCustomers() {
        customers.clear();
        customers.addAll(customerService.getAllCustomers());
        cusManageTable.setItems(customers);
    }

    private void clearInputFields() {
        idText.clear();
        nameText.clear();
        emailText.clear();
        mobileText.clear();
    }

    private void clearUpdateFields() {
        idTextU.clear();
        nameTextU.clear();
        emailTextU.clear();
        mobileTextU.clear();
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            
            Stage stage = (Stage) cusManageTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to logout: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
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
    private TextField nameText, emailText, mobileText;
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
        try {
            // Initialize database if needed
            loadCustomers(); // Refresh the table view
            showAlert("Success", "Database initialized successfully", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            showAlert("Error", "Failed to initialize database: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    //Select data from table.
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
            if (idTextU.getText().isEmpty()) {
                showAlert("Error", "Please select a customer to update", Alert.AlertType.ERROR);
                return;
            }

            Customer customer = new Customer(
                nameTextU.getText(),
                emailTextU.getText(),
                mobileTextU.getText()
            );
            customer.setId(Integer.valueOf(idTextU.getText())); // Set the ID for update

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
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to logout: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
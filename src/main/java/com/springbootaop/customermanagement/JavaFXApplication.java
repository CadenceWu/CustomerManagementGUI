package com.springbootaop.customermanagement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class JavaFXApplication extends Application {
	private static ApplicationContext applicationContext;

//    @Override
//    public void start(Stage stage) throws Exception {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerManagementGUI.fxml"));
//            loader.setControllerFactory(applicationContext::getBean);
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setTitle("Customer Management System");
//            stage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Platform.exit();
//        }
//    }
	@Override
	public void start(Stage stage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
			loader.setControllerFactory(applicationContext::getBean);
			Parent root = loader.load();
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Login");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	@Override
	public void stop() {
		Platform.exit();
		System.exit(0);
	}

	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
}
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


	@Override
	public void start(Stage stage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));	
			loader.setControllerFactory(applicationContext::getBean); //Method reference.(object: :method). getBean() method of the application object.
			//=loader.setControllerFactory(beanName -> applicationContext.getBean(beanName));
			Parent root = loader.load();
			Scene scene = new Scene(root, 800, 600);
			stage.setScene(scene);
			stage.setTitle("Login");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	@Override
	//Called when the application is closed or stopped.
	public void stop() {
		Platform.exit();
		System.exit(0);
	}

	//Is used to inject the Spring ApplicationContext.
	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}
}
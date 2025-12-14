package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.LoginPage;

public class Main extends Application{
	
	private static Stage stage;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		stage = primaryStage;
        stage.setTitle("GoVlash Laundry System");
        
        // Start  Login Page
        setScene(new LoginPage().getScene());
        stage.show();	
	}
	
	// Helper method to switch scenes
    public static void setScene(javafx.scene.Scene newScene) {
        stage.setScene(newScene);
        stage.centerOnScreen();
    }

}

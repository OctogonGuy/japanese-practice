/**
 * 
 */
package tech.octopusdragon.japanesepractice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A program that lets you practice kanji, conjugation, counters, etc.
 * @author Alex Gill
 *
 */
public class JapanesePracticeApplication extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/resources/view/MainView.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/resources/styles/main.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("日本語の練習");
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

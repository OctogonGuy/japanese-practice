package tech.octopusdragon.japanesepractice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * A program that lets you practice kanji, conjugation, counters, etc.
 * @author Alex Gill
 *
 */
public class JapanesePracticeApplication extends Application {

	private static final double MIN_WIDTH = 475.0;	// Minimum stage width
	private static final double MIN_HEIGHT = 275.0;	// Minimum stage height

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainView.fxml")));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/main.css")).toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("日本語の練習");
		primaryStage.show();
		setMinWindowSize(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Prevents window from shrinking past certain point
	 * @param stage The stage
	 */
	private void setMinWindowSize(Stage stage) {
		double windowWidth = stage.getScene().getWindow().getWidth();
		double windowHeight = stage.getScene().getWindow().getHeight();
		double stageWidth = stage.getScene().getWidth();
		double stageHeight = stage.getScene().getHeight();

		// Calculate top, bottom, left, and right window insets
		double leftInsets = stage.getScene().getX();
		double topInsets = stage.getScene().getY();
		double rightInsets = windowWidth - stageWidth - leftInsets;
		double bottomInsets = windowHeight - stageHeight - topInsets;

		// Set stage's minimum width and height, accounting for the decorations
		stage.setMinWidth(MIN_WIDTH + leftInsets + rightInsets);
		stage.setMinHeight(MIN_HEIGHT + topInsets + bottomInsets);
	}

}

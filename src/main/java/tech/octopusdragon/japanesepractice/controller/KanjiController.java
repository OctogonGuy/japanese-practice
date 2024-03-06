package tech.octopusdragon.japanesepractice.controller;

import java.util.ArrayList;
import java.util.List;

import tech.octopusdragon.japanesepractice.model.Kanji;
import tech.octopusdragon.japanesepractice.model.KanjiPractice;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public abstract class KanjiController {
	private static final double BRUSH_SIZE = 2.0;

	@FXML private HBox joyoGradeBox;
	@FXML private Label joyoGradeLabel;
	@FXML private Label messageLabel;
	@FXML private GridPane infoPane;
	@FXML private Canvas primarySquare;
	@FXML private GridPane secondarySquares;
	@FXML private Text characterText;
	@FXML private Label meaningLabel;
	@FXML private Label readingLabel;
	@FXML private HBox buttonBar;
	@FXML private Button correctButton;
	@FXML private Button incorrectButton;
	@FXML private Button showButton;
	
	private Font defaultKanjiFont;

	protected KanjiPractice session;

	@FXML
	protected void initialize() {
		Platform.runLater(() -> {
			defaultKanjiFont = characterText.getFont();
		});
		
		List<Node> squares = new ArrayList<>();
		squares.add(primarySquare);
		for (Node secondarySquareContainer : secondarySquares.getChildren())
			squares.add(((Parent)secondarySquareContainer).getChildrenUnmodifiable().get(0));
		squares.add(characterText);
		for (Node square : squares) {
			square.disabledProperty().addListener((obs, oldVal, newVal) -> {
				if (newVal) {
					square.getParent().getStyleClass().add("disabled-square");
					square.getParent().getStyleClass().remove("enabled-square");
				}
				else {
					square.getParent().getStyleClass().add("enabled-square");
					square.getParent().getStyleClass().remove("disabled-square");
				}
			});
		}
		
		nextKanji();
	}

	/**
	 * Advances the next kanji
	 */
	public void nextKanji() {
		Kanji curKanji = session.next();
		
		if (curKanji == null) {
			joyoGradeBox.setVisible(false);
			infoPane.setVisible(false);
			buttonBar.setVisible(false);
			messageLabel.setText(noKanjiMessage());
			messageLabel.setVisible(true);
			return;
		}
		else if (messageLabel.isVisible()) {
			joyoGradeBox.setVisible(true);
			infoPane.setVisible(true);
			buttonBar.setVisible(true);
			messageLabel.setVisible(false);
		}

		characterText.setText(Character.toString(session.getCurKanji().getCharacter()));
		characterText.setVisible(false);
		joyoGradeLabel.setText(session.getCurKanji().getJoyoGrade());
		meaningLabel.setText(session.getCurKanji().getMeaning());
		readingLabel.setText(session.getCurKanji().getReading());
		correctButton.setVisible(false);
		correctButton.setManaged(false);
		incorrectButton.setVisible(false);
		incorrectButton.setManaged(false);
		showButton.setVisible(true);
		showButton.setManaged(true);
		
		primarySquare.getGraphicsContext2D().clearRect(0, 0, primarySquare.getWidth(), primarySquare.getHeight());
		primarySquare.setDisable(false);
		for (Node secondarySquareContainer : secondarySquares.getChildren()) {
			if (!((Parent)secondarySquareContainer).getChildrenUnmodifiable().get(0).getClass().equals(Canvas.class)) continue;
			Canvas secondarySquare = (Canvas) ((Parent)secondarySquareContainer).getChildrenUnmodifiable().get(0);
			(secondarySquare).getGraphicsContext2D().clearRect(0, 0, secondarySquare.getWidth(), secondarySquare.getHeight());
			secondarySquare.setDisable(true);
		}
		characterText.setDisable(true);
	}

	/**
	 * Shows the current kanji
	 */
	@FXML
	private void show(ActionEvent event) {
		characterText.setVisible(true);
		correctButton.setVisible(true);
		correctButton.setManaged(true);
		incorrectButton.setVisible(true);
		incorrectButton.setManaged(true);
		showButton.setVisible(false);
		showButton.setManaged(false);
		
		primarySquare.setDisable(true);
		for (Node secondarySquareContainer : secondarySquares.getChildren()) {
			if (!((Parent)secondarySquareContainer).getChildrenUnmodifiable().get(0).getClass().equals(Canvas.class)) continue;
			Canvas secondarySquare = (Canvas) ((Parent)secondarySquareContainer).getChildrenUnmodifiable().get(0);
			secondarySquare.setDisable(false);
		}
		characterText.setDisable(false);
	}

	@FXML
	private void draw(MouseEvent event) {
		GraphicsContext gc = ((Canvas) event.getSource()).getGraphicsContext2D();
		gc.fillRect(event.getX() - BRUSH_SIZE/2, event.getY() - BRUSH_SIZE/2, BRUSH_SIZE, BRUSH_SIZE);
	}

	@FXML
	private void correct(ActionEvent event) {
		session.correct();
		nextKanji();
	}

	@FXML
	private void incorrect(ActionEvent event) {
		session.incorrect();
		nextKanji();
	}
	
	@FXML
	private void toggleStrokeOrder(MouseEvent event) {
		if (session.isShowStrokeOrder()) {
			session.setShowStrokeOrder(false);
			characterText.setFont(defaultKanjiFont);
		}
		else {
			session.setShowStrokeOrder(true);
			characterText.setFont(Font.loadFont(getClass().getResourceAsStream("/fonts/KanjiStrokeOrders.ttf"), defaultKanjiFont.getSize()));
		}
	}
	
	/**
	 * @return A message indicating there are no kanji available
	 */
	protected abstract String noKanjiMessage();
}

package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.KanjiPractice;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class KanjiController {
	private static final double BRUSH_SIZE = 3.0;

	@FXML private Label joyoGradeLabel;
	@FXML private Canvas primarySquare;
	@FXML private GridPane secondarySquares;
	@FXML private Text characterText;
	@FXML private Label meaningLabel;
	@FXML private Label readingLabel;
	@FXML private Button correctButton;
	@FXML private Button incorrectButton;
	@FXML private Button showButton;

	private KanjiPractice session;

	@FXML
	private void initialize() {
		session = new KanjiPractice();
		nextKanji();
	}

	/**
	 * Advances the next kanji
	 */
	private void nextKanji() {
		session.next();

		characterText.setText(Character.toString(session.getCurKanji().getCharacter()));
		characterText.setVisible(false);
		joyoGradeLabel.setText(session.getCurKanji().getJoyoGrade());
		meaningLabel.setText(session.getCurKanji().getMeaning());
		readingLabel.setText(session.getCurKanji().getReading());
		primarySquare.getGraphicsContext2D().clearRect(0, 0, primarySquare.getWidth(), primarySquare.getHeight());
		correctButton.setVisible(false);
		correctButton.setManaged(false);
		incorrectButton.setVisible(false);
		incorrectButton.setManaged(false);
		showButton.setVisible(true);
		showButton.setManaged(true);
		for (Node secondarySquareNode : secondarySquares.getChildren()) {
			if (!secondarySquareNode.getClass().equals(Canvas.class)) continue;
			Canvas secondarySquare = (Canvas) secondarySquareNode;
			(secondarySquare).getGraphicsContext2D().clearRect(0, 0, secondarySquare.getWidth(), secondarySquare.getHeight());
		}
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
}

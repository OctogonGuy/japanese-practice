package tech.octopusdragon.japanesepractice.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.octopusdragon.japanesepractice.model.Kanji;
import tech.octopusdragon.japanesepractice.model.KanjiPractice;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
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
	@FXML private Hyperlink wanikaniLink;
	@FXML private Hyperlink jishoLink;
	@FXML private Hyperlink kanjipediaLink;
	
	private Font defaultKanjiFont;
	private double lastX, lastY;

	protected KanjiPractice session;

	@FXML
	protected void initialize() {
		lastX = lastY = Double.NaN;
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
		
		Map<Hyperlink, String> linkMap = new HashMap<>();
		linkMap.put(wanikaniLink, "https://wanikani.com/kanji/%s");
		linkMap.put(jishoLink, "https://jisho.org/search/%s%%23kanji");
		linkMap.put(kanjipediaLink, "https://dictionary.goo.ne.jp/word/kanji/%s/");
		for (Hyperlink hyperlink : new Hyperlink[] {wanikaniLink, jishoLink, kanjipediaLink}) {
			hyperlink.setOnAction(event -> {
				try {
					Desktop.getDesktop().browse(new URI(String.format(linkMap.get(hyperlink), session.getCurKanji())));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
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
		wanikaniLink.setVisible(false);
		jishoLink.setVisible(false);
		kanjipediaLink.setVisible(false);
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
		wanikaniLink.setVisible(true);
		jishoLink.setVisible(true);
		kanjipediaLink.setVisible(true);
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
	
	private void drawStart(Event event, double x, double y) {
		GraphicsContext gc = ((Canvas) event.getSource()).getGraphicsContext2D();
		double curX = x - BRUSH_SIZE/2;
		double curY = y - BRUSH_SIZE/2;
		gc.fillRect(curX, curY, BRUSH_SIZE, BRUSH_SIZE);
		lastX = x;
		lastY = y;
	}
	
	private void draw(Event event, double x, double y) {
		GraphicsContext gc = ((Canvas) event.getSource()).getGraphicsContext2D();
		double curX = x;
		double curY = y;
		gc.strokeLine(lastX, lastY, curX, curY);
		lastX = curX;
		lastY = curY;
	}
	
	private void drawRelease() {
		lastX = lastY = Double.NaN;
	}

	@FXML
	private void drawMouseStart(MouseEvent event) {
		drawStart(event, event.getX(), event.getY());
	}

	@FXML
	private void drawMouse(MouseEvent event) {
		draw(event, event.getX(), event.getY());
	}

	@FXML
	private void drawMouseRelease(MouseEvent event) {
		drawRelease();
	}

	@FXML
	private void drawTouchStart(TouchEvent event) {
		drawStart(event, event.getTouchPoint().getX(), event.getTouchPoint().getY());
	}

	@FXML
	private void drawTouch(TouchEvent event) {
		draw(event, event.getTouchPoint().getX(), event.getTouchPoint().getY());
	}

	@FXML
	private void drawTouchRelease(TouchEvent event) {
		drawRelease();
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

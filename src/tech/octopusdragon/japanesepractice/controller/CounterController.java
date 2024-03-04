package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.CounterPractice;
import tech.octopusdragon.japanesepractice.model.Userdata;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CounterController {
	@FXML private Label curStreakLabel;
	@FXML private Label highestStreakLabel;
	@FXML private Label kanjiRepresentationLabel;
	@FXML private Label correctAnswerLabel;
	@FXML private TextField answerTextField;
	@FXML private Button submitButton;
	@FXML private Button nextButton;
	
	private CounterPractice session;
	
	@FXML
	private void initialize() {
		session = new CounterPractice();
		
		curStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getHighestStreak()));
		correctAnswerLabel.setVisible(false);
		submitButton.defaultButtonProperty().bind(submitButton.disabledProperty().not());
		nextButton.defaultButtonProperty().bind(submitButton.disabledProperty());
		
		next();
	}
	
	@FXML
	private void submit(ActionEvent event) {
		submit(answerTextField.getText());
	}
	
	private void submit(String guess) {
		session.submit(guess);
		
		curStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getHighestStreak()));
		correctAnswerLabel.setText(session.correctAnswersStr());
		correctAnswerLabel.setVisible(true);
		answerTextField.setDisable(true);
		submitButton.setDisable(true);
		nextButton.setDisable(false);
	}
	
	@FXML
	private void next(ActionEvent event) {
		next();
	}
	
	private void next() {
		session.next();
		
		kanjiRepresentationLabel.setText(session.getKanjiRepresentation());
		correctAnswerLabel.setVisible(false);
		answerTextField.clear();
		answerTextField.setDisable(false);
		submitButton.setDisable(false);
		nextButton.setDisable(true);
	}
}

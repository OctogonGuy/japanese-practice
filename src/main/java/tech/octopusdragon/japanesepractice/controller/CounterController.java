package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.CounterPractice;
import tech.octopusdragon.japanesepractice.model.PromptLanguage;
import tech.octopusdragon.japanesepractice.model.Userdata;
import tech.octopusdragon.japanesepractice.model.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CounterController {
	@FXML private Label curStreakLabel;
	@FXML private Label highestStreakLabel;
	@FXML private RadioButton japaneseRadioButton;
	@FXML private RadioButton englishRadioButton;
	@FXML private RadioButton randomRadioButton;
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
		submitButton.defaultButtonProperty().bind(submitButton.disabledProperty().not().and(answerTextField.focusedProperty()));
		nextButton.defaultButtonProperty().bind(submitButton.disabledProperty().and(nextButton.focusedProperty()));
		
		switch (session.getPromptLanguage()) {
		case JAPANESE:
			japaneseRadioButton.setSelected(true);
			session.setPromptLanguage(PromptLanguage.JAPANESE);
			break;
		case ENGLISH:
			englishRadioButton.setSelected(true);
			session.setPromptLanguage(PromptLanguage.ENGLISH);
			break;
		case RANDOM:
			randomRadioButton.setSelected(true);
			session.setPromptLanguage(PromptLanguage.RANDOM);
			break;
		}

		next();
	}

	@FXML
	private void submit(ActionEvent event) {
		// If the last character is 'n', change it to 'ん'
		if (!answerTextField.getText().isEmpty() && answerTextField.getText().charAt(answerTextField.getText().length() - 1) == 'n')
			answerTextField.replaceText(answerTextField.getText().length() - 1, answerTextField.getText().length(), "ん");
		
		submit(answerTextField.getText());
	}

	private void submit(String guess) {
		boolean correct = session.submit(guess);

		curStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getCounterPracticeData().getHighestStreak()));
		correctAnswerLabel.setText(session.correctAnswersStr());
		if (correct) {
			correctAnswerLabel.setText("⭕ " + correctAnswerLabel.getText());
			correctAnswerLabel.getStyleClass().add("correct");
		}
		else {
			correctAnswerLabel.setText("❌ " + correctAnswerLabel.getText());
			correctAnswerLabel.getStyleClass().add("incorrect");
		}
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

		kanjiRepresentationLabel.setText(session.prompt());
		correctAnswerLabel.setVisible(false);
		correctAnswerLabel.getStyleClass().remove("correct");
		correctAnswerLabel.getStyleClass().remove("incorrect");
		answerTextField.clear();
		answerTextField.setDisable(false);
		submitButton.setDisable(false);
		nextButton.setDisable(true);
	}
	
	@FXML
	private void changePromptLanguageToJapanese() {
		session.setPromptLanguage(PromptLanguage.JAPANESE);
		kanjiRepresentationLabel.setText(session.prompt());
	}
	
	@FXML
	private void changePromptLanguageToEnglish() {
		session.setPromptLanguage(PromptLanguage.ENGLISH);
		kanjiRepresentationLabel.setText(session.prompt());
	}
	
	@FXML
	private void changePromptLanguageToRandom() {
		session.setPromptLanguage(PromptLanguage.RANDOM);
		kanjiRepresentationLabel.setText(session.prompt());
	}
	
	@FXML
	private void convertRomajiToHiragana(KeyEvent event) {
		Util.convertRomajiToHiragana(answerTextField);
	}
}

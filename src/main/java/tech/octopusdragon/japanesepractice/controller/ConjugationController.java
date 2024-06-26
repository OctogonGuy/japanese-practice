package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.ConjugationPractice;
import tech.octopusdragon.japanesepractice.model.Userdata;
import tech.octopusdragon.japanesepractice.model.Util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class ConjugationController {
	@FXML private Label curStreakLabel;
	@FXML private Label highestStreakLabel;
	@FXML private RadioButton japaneseRadioButton;
	@FXML private RadioButton englishRadioButton;
	@FXML private RadioButton randomRadioButton;
	@FXML private Label dictionaryFormLabel;
	@FXML private Label conjugationLabel;
	@FXML private Label correctAnswerLabel;
	@FXML private TextField answerTextField;
	@FXML private Button submitButton;
	@FXML private Button nextButton;

	private ConjugationPractice session;

	@FXML
	private void initialize() {
		session = new ConjugationPractice();

		curStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getHighestStreak()));
		correctAnswerLabel.setVisible(false);
		submitButton.defaultButtonProperty().bind(submitButton.disabledProperty().not().and(answerTextField.focusedProperty()));
		nextButton.defaultButtonProperty().bind(submitButton.disabledProperty().and(nextButton.focusedProperty()));

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

		curStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getHighestStreak()));
		correctAnswerLabel.setText(StringUtils.strip(Arrays.asList(session.correctAnswers()).stream().map(str -> str + "・").reduce("", String::concat), "・"));
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

		dictionaryFormLabel.setText(session.prompt());
		conjugationLabel.setText(session.getCurConjugation().toString());
		correctAnswerLabel.setVisible(false);
		correctAnswerLabel.getStyleClass().remove("correct");
		correctAnswerLabel.getStyleClass().remove("incorrect");
		answerTextField.clear();
		answerTextField.setDisable(false);
		submitButton.setDisable(false);
		nextButton.setDisable(true);
	}
	
	@FXML
	private void convertRomajiToHiragana(KeyEvent event) {
		Util.convertRomajiToHiragana(answerTextField);
	}
}

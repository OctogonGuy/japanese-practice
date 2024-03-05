package tech.octopusdragon.japanesepractice.controller;

import org.apache.commons.lang3.StringUtils;

import com.moji4j.MojiConverter;
import com.moji4j.MojiDetector;

import tech.octopusdragon.japanesepractice.model.ConjugationPractice;
import tech.octopusdragon.japanesepractice.model.PromptLanguage;
import tech.octopusdragon.japanesepractice.model.Userdata;

import javafx.application.Platform;
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
		
		switch (session.getPromptLanguage()) {
		case JAPANESE:
			japaneseRadioButton.setSelected(true);
			break;
		case ENGLISH:
			englishRadioButton.setSelected(true);
			break;
		case RANDOM:
			randomRadioButton.setSelected(true);
			break;
		}

		next();
	}

	@FXML
	private void submit(ActionEvent event) {
		submit(answerTextField.getText());
	}

	private void submit(String guess) {
		boolean correct = session.submit(guess);

		curStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getCurStreak()));
		highestStreakLabel.setText(String.valueOf(Userdata.getConjugationPracticeData().getHighestStreak()));
		correctAnswerLabel.setText(session.correctAnswer());
		if (correct) correctAnswerLabel.getStyleClass().addAll("correct");
		else correctAnswerLabel.getStyleClass().add("incorrect");
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

		dictionaryFormLabel.setText(session.getCurVerb().getDictionaryForm());
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
	private void changePromptLanguageToJapanese() {
		session.setPromptLanguage(PromptLanguage.JAPANESE);
		dictionaryFormLabel.setText(session.prompt());
	}
	
	@FXML
	private void changePromptLanguageToEnglish() {
		session.setPromptLanguage(PromptLanguage.ENGLISH);
		dictionaryFormLabel.setText(session.prompt());
	}
	
	@FXML
	private void changePromptLanguageToRandom() {
		session.setPromptLanguage(PromptLanguage.RANDOM);
		dictionaryFormLabel.setText(session.prompt());
	}
	
	@FXML
	private void convertRomajiToHiragana(KeyEvent event) {
		// Convert typed romaji characters to hiragana
		Platform.runLater(() -> {
			String original = answerTextField.getText();
			String converted = new MojiConverter().convertRomajiToHiragana(original);
			if (original.equals(converted)) return;                  
			String romajiDiff = StringUtils.difference(converted, original);
			String hiraganaDiff = StringUtils.difference(original, converted);
			int diffIndex = StringUtils.indexOfDifference(original, converted);
			// Do nothing if just small っ
			if (hiraganaDiff.startsWith("っ") && new MojiDetector().hasLatin(hiraganaDiff)) return;
			// Do nothing if ん without double n
			if (hiraganaDiff.startsWith("ん") && (romajiDiff.equals("n") || romajiDiff.equals("m") || romajiDiff.equals("nm"))) return;
			// Replace double n with ん
			hiraganaDiff = hiraganaDiff.replace("っん", "ん");
			int caretDistanceFromEnd = answerTextField.getText().length() - answerTextField.getCaretPosition();
			answerTextField.replaceText(diffIndex, diffIndex + romajiDiff.length(), hiraganaDiff);
			answerTextField.positionCaret(answerTextField.getText().length() - caretDistanceFromEnd);
		});
	}
}

package tech.octopusdragon.japanesepractice.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import tech.octopusdragon.japanesepractice.model.GrammarPractice;

public class GrammarController {
	@FXML private Label grammarLabel;
	@FXML private Button nextButton;
	@FXML private CheckBox jlptN5CheckBox;
	@FXML private CheckBox jlptN4CheckBox;
	@FXML private CheckBox jlptN3CheckBox;
	@FXML private CheckBox jlptN2CheckBox;
	@FXML private CheckBox jlptN1CheckBox;
	
	private ObservableList<CheckBox> jlptCheckBoxes;
	private ObservableList<ReadOnlyBooleanProperty> jlptCheckBoxesSelectedProperties;

	private GrammarPractice session;

	@FXML
	private void initialize() {
		session = new GrammarPractice();
		
		// Change JLPT range on selection.
		// Also prevent less than 1 check box from being selected.
		jlptCheckBoxes = FXCollections.observableArrayList(
				jlptN1CheckBox, jlptN2CheckBox,
				jlptN3CheckBox, jlptN4CheckBox, jlptN5CheckBox);
		jlptCheckBoxesSelectedProperties = FXCollections.observableArrayList(
				jlptN1CheckBox.selectedProperty(), jlptN2CheckBox.selectedProperty(),
				jlptN3CheckBox.selectedProperty(), jlptN4CheckBox.selectedProperty(), jlptN5CheckBox.selectedProperty());
		for (int i = 0; i < jlptCheckBoxes.size(); i++) {
			CheckBox checkBox = jlptCheckBoxes.get(i);
			checkBox.setSelected(session.getJlptLevels().contains(i));
			checkBox.disableProperty().bind(Bindings.createBooleanBinding(() -> {
				int selectedCount = 0;
				for (ReadOnlyBooleanProperty booleanProperty : jlptCheckBoxesSelectedProperties) {
					if (booleanProperty.get())
						selectedCount++;
				}
				if (selectedCount == 1 && checkBox.isSelected())
					return true;
				else
					return false;
			}, jlptN1CheckBox.selectedProperty(), jlptN2CheckBox.selectedProperty(),
					jlptN3CheckBox.selectedProperty(), jlptN4CheckBox.selectedProperty(), jlptN5CheckBox.selectedProperty()));
		}
		
		next();
	}
	
	@FXML
	private void start() {
	}

	@FXML
	private void next(ActionEvent event) {
		next();
	}

	@FXML
	private void toggleJLPTN5(ActionEvent event) {
		session.toggleJlptLevel(5);
	}

	@FXML
	private void toggleJLPTN4(ActionEvent event) {
		session.toggleJlptLevel(4);
	}

	@FXML
	private void toggleJLPTN3(ActionEvent event) {
		session.toggleJlptLevel(3);
	}

	@FXML
	private void toggleJLPTN2(ActionEvent event) {
		session.toggleJlptLevel(2);
	}

	@FXML
	private void toggleJLPTN1(ActionEvent event) {
		session.toggleJlptLevel(1);
	}

	private void next() {
		session.next();
		
		grammarLabel.setText(session.getCurGrammar().getGrammar());
	}
}

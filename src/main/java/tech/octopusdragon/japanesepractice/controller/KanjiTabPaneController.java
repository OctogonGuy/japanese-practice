package tech.octopusdragon.japanesepractice.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class KanjiTabPaneController {
	
	@FXML private AnchorPane learnKanjiContainer;
	@FXML private AnchorPane reviewKanjiContainer;
	@FXML private Tab learnTab;
	@FXML private Tab reviewTab;
	
	@FXML
	public void initialize() {
		// Load learn kanji tab
		FXMLLoader learnViewLoader = new FXMLLoader(getClass().getResource("/view/KanjiView.fxml"));
		KanjiController learnKanjiController = new LearnKanjiController();
		learnTab.setOnSelectionChanged(event -> {
			if (((Tab)event.getSource()).isSelected())
				learnKanjiController.nextKanji();
		});
		learnViewLoader.setController(learnKanjiController);
		try {
			Node learnView = learnViewLoader.load();
			learnKanjiContainer.getChildren().add(learnView);
			AnchorPane.setTopAnchor(learnView, 0.0);
			AnchorPane.setBottomAnchor(learnView, 0.0);
			AnchorPane.setLeftAnchor(learnView, 0.0);
			AnchorPane.setRightAnchor(learnView, 0.0);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// Load review kanji tab
		FXMLLoader reviewViewLoader = new FXMLLoader(getClass().getResource("/view/KanjiView.fxml"));
		KanjiController reviewKanjiController = new ReviewKanjiController();
		reviewTab.setOnSelectionChanged(event -> {
			if (((Tab)event.getSource()).isSelected())
				reviewKanjiController.nextKanji();
		});
		reviewViewLoader.setController(reviewKanjiController);
		try {
			Node reviewView = reviewViewLoader.load();
			reviewKanjiContainer.getChildren().add(reviewView);
			AnchorPane.setTopAnchor(reviewView, 0.0);
			AnchorPane.setBottomAnchor(reviewView, 0.0);
			AnchorPane.setLeftAnchor(reviewView, 0.0);
			AnchorPane.setRightAnchor(reviewView, 0.0);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
}

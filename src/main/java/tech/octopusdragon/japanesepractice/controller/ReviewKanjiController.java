package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.KanjiPracticeReview;

public class ReviewKanjiController extends KanjiController {
	public ReviewKanjiController() {
		session = new KanjiPracticeReview();
	}
}

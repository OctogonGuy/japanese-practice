package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.KanjiPracticeReview;

public class ReviewKanjiController extends KanjiController {
	public ReviewKanjiController() {
		session = new KanjiPracticeReview();
	}

	@Override
	protected String noKanjiMessage() {
		return "漢字を復習し始めるために「学習」タブで表示された新しい漢字を解答してください。";
	}
}

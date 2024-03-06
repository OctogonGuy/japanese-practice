package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.KanjiPracticeLearn;

public class LearnKanjiController extends KanjiController {
	public LearnKanjiController() {
		session = new KanjiPracticeLearn();
	}

	@Override
	protected String noKanjiMessage() {
		return "おめでとうございます！漢字を全部覚えました！";
	}
}

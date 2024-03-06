package tech.octopusdragon.japanesepractice.controller;

import tech.octopusdragon.japanesepractice.model.KanjiPracticeLearn;

public class LearnKanjiController extends KanjiController {
	public LearnKanjiController() {
		session = new KanjiPracticeLearn();
	}
}

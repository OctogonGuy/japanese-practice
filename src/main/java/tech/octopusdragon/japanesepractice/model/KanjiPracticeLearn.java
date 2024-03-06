package tech.octopusdragon.japanesepractice.model;

/**
 * Practice for learning new kanji
 * @author Alex Gill
 *
 */
public class KanjiPracticeLearn extends KanjiPractice {
	@Override
	public Kanji next() {
		curKanji = Scheduler.nextLearnKanji();
		return curKanji;
	}
}

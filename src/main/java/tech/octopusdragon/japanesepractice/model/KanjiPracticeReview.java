package tech.octopusdragon.japanesepractice.model;

/**
 * Practice for reviewing already learned kanji
 * @author Alex Gill
 *
 */
public class KanjiPracticeReview extends KanjiPractice {
	@Override
	public Kanji next() {
		curKanji = Scheduler.nextReviewKanji();
		return curKanji;
	}
}

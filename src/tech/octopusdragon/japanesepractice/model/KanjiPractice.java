package tech.octopusdragon.japanesepractice.model;

/**
 * Contains logic of kanji practice session
 * @author Alex Gill
 *
 */
public class KanjiPractice {
	
	private Kanji curKanji;
	
	/**
	 * Moves on to the next kanji
	 */
	public void next() {
		curKanji = Scheduler.nextKanji();
	}
	
	/**
	 * Records that the user correctly wrote the kanji and updates spaced
	 * repetition accordingly
	 */
	public void correct() {
		Userdata.getKanjiPracticeData().advance(curKanji);
		Userdata.save();
	}

	/**
	 * Records that the user did not correctly write the kanji and updates
	 * spaced repetition accordingly
	 */
	public void incorrect() {
		Userdata.getKanjiPracticeData().reset(curKanji);
		Userdata.save();
	}

	/**
	 * @return the current kanji
	 */
	public Kanji getCurKanji() {
		return curKanji;
	}

}

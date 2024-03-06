package tech.octopusdragon.japanesepractice.model;

/**
 * Contains logic of kanji practice session
 * @author Alex Gill
 *
 */
public abstract class KanjiPractice {
	
	protected Kanji curKanji;
	private boolean showStrokeOrder;
	
	/**
	 * Moves on to the next kanji
	 */
	public abstract void next();
	
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

	/**
	 * @return whether stroke order is showing
	 */
	public boolean isShowStrokeOrder() {
		return showStrokeOrder;
	}

	/**
	 * @param showStrokeOrder whether to show stroke order
	 */
	public void setShowStrokeOrder(boolean showStrokeOrder) {
		this.showStrokeOrder = showStrokeOrder;
	}

}

package tech.octopusdragon.japanesepractice.model;

import java.util.List;

/**
 * Contains logic of grammar practice session
 * @author Alex Gill
 *
 */
public class GrammarPractice {

	private Grammar curGrammar;
	private List<Integer> jlptLevels;
	
	public GrammarPractice() {
		jlptLevels = Userdata.getGrammarPracticeData().getJlptLevels();
	}
	
	/**
	 * Moves on to the next piece of grammar
	 */
	public void next() {
		curGrammar = Scheduler.nextGrammar(jlptLevels);
	}

	/**
	 * @return the current grammar
	 */
	public Grammar getCurGrammar() {
		return curGrammar;
	}

	/**
	 * @return the active JLPT levels
	 */
	public List<Integer> getJlptLevels() {
		return jlptLevels;
	}
	
	public void toggleJlptLevel(int level) {
		if (jlptLevels.contains(level)) {
			jlptLevels.remove(new Integer(level));
		}
		else {
			jlptLevels.add(level);
		}
		Userdata.getGrammarPracticeData().setJlptIntervals(jlptLevels);
		Userdata.save();
	}
}

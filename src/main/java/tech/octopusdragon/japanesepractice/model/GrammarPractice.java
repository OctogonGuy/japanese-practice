package tech.octopusdragon.japanesepractice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Contains logic of grammar practice session
 * @author Alex Gill
 *
 */
public class GrammarPractice {

	private Grammar curGrammar;
	private List<Integer> jlptLevels;
	
	public GrammarPractice() {
		jlptLevels = new ArrayList<>(IntStream.range(0, 5 + 1).boxed().collect(Collectors.toList()));
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
	}
	
	public void toggleMisc() {
		if (jlptLevels.contains(0)) {
			jlptLevels.remove(new Integer(0));
		}
		else {
			jlptLevels.add(0);
		}
	}

}

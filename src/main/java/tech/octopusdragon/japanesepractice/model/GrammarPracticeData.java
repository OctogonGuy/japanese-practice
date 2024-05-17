package tech.octopusdragon.japanesepractice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Holds information about the user's grammar practice
 * @author Alex Gill
 *
 */
public class GrammarPracticeData {
	private List<Integer> jlptLevels;
	
	/**
	 * Initializes grammar data
	 */
	public GrammarPracticeData() {
		jlptLevels = new ArrayList<>(IntStream.range(0, 5 + 1).boxed().collect(Collectors.toList()));
	}
	
	/**
	 * @return the jlpt levels
	 */
	public List<Integer> getJlptLevels() {
		return jlptLevels;
	}

	/**
	 * @param jlptLevels the jlpt levels
	 */
	public void setJlptIntervals(List<Integer> jlptLevels) {
		this.jlptLevels = jlptLevels;
	}
}

package tech.octopusdragon.japanesepractice.model;

import java.util.HashMap;
import java.util.Map;

public class KanjiPracticeData {
	
	private static final int STAGES = 5;		// Number of stages
	private static final int INIT_STAGE = 3;	// Initial stage for all kanji

	private Map<Character, Integer> srsMap;	// Maps kanji to SRS stage

	/**
	 * Initializes kanji practice with no progress
	 */
	public KanjiPracticeData() {
		srsMap = new HashMap<>();
	}
	
	/**
	 * Advances kanji to next, less frequent stage
	 * @param kanji The kanji
	 */
	public void advance(Kanji kanji) {
		char kanjiChar = kanji.getCharacter();
		if (!srsMap.containsKey(kanjiChar))
			srsMap.put(kanjiChar, INIT_STAGE);
		srsMap.put(kanjiChar, Math.min(srsMap.get(kanjiChar) + 1, STAGES));
	}
	
	/**
	 * Returns kanji to first, most repetitive stage
	 * @param kanji The kanji
	 */
	public void reset(Kanji kanji) {
		char kanjiChar = kanji.getCharacter();
		if (!srsMap.containsKey(kanjiChar))
			srsMap.put(kanjiChar, INIT_STAGE - 1);
		else
			srsMap.put(kanjiChar, 1);
	}
	
}

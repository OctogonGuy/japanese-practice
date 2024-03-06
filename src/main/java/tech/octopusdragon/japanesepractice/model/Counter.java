package tech.octopusdragon.japanesepractice.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a counter
 * @author Alex Gill
 *
 */
public class Counter {
	private String counterStr;
	// All number + counter combination kanji
	private Map<Integer, String> kanjiRepresentations;
	// All number + counter combination readings
	private Map<Integer, List<String>> readings;
	// All English number + counter words
	private Map<Integer, String> meanings;
	
	public Counter(String counterStr) {
		this.counterStr = counterStr;
		kanjiRepresentations = new HashMap<Integer, String>();
		readings = new HashMap<Integer, List<String>>();
		meanings = new HashMap<Integer, String>();
	}

	/**
	 * @return the kanji representation
	 */
	public String getKanjiRepresentation(int number) {
		return kanjiRepresentations.get(number);
	}

	/**
	 * @return the readings
	 */
	public List<String> getReadings(int number) {
		return readings.get(number);
	}

	/**
	 * @return the meanings
	 */
	public String getMeaning(int number) {
		return meanings.get(number);
	}
	
	/**
	 * Adds a new counter compound with a kanji representation, kana reading,
	 * and English meaning.
	 * @param number The number
	 * @param kanji The kanji representation
	 * @param kana The kana readings
	 * @param meaning The English meaning
	 */
	public void putCompound(int number, String kanji, List<String> kana, String meaning) {
		kanjiRepresentations.put(number, kanji);
		readings.put(number, kana);
		meanings.put(number, meaning);
	}
	
	@Override
	public String toString() {
		return counterStr;
	}
}

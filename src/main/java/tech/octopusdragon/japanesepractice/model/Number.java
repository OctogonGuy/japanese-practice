package tech.octopusdragon.japanesepractice.model;

import java.util.List;

/**
 * Represents a number; has a kanji numeral, kana reading(s), and English
 * meaning
 * @author Alex Gill
 *
 */
public class Number {
	private int number;
	private String kanjiNumeral;
	private List<String> readings;
	private String meaning;
	
	public Number(int number, String kanjiNumeral, List<String> readings, String meaning) {
		this.number = number;
		this.kanjiNumeral = kanjiNumeral;
		this.readings = readings;
		this.meaning = meaning;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @return the kanji numeral
	 */
	public String getKanjiNumeral() {
		return kanjiNumeral;
	}

	/**
	 * @return the readings
	 */
	public List<String> getReadings() {
		return readings;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}
}

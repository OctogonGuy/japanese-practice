package tech.octopusdragon.japanesepractice.model;

/**
 * Represents a kanji character
 * @author Alex Gill
 *
 */
public class Kanji {
	private final char character;
	private final int numStrokes;
	private final String joyoGrade;
	private final String meaning;
	private final String reading;
	
	public Kanji(char character, int numStrokes, String joyoGrade, String meaning, String reading) {
		this.character = character;
		this.numStrokes = numStrokes;
		this.joyoGrade = joyoGrade;
		this.meaning = meaning;
		this.reading = reading;
	}

	/**
	 * @return the character
	 */
	public char getCharacter() {
		return character;
	}

	/**
	 * @return the number of strokes
	 */
	public int getNumStrokes() {
		return numStrokes;
	}

	/**
	 * @return the grade level taught
	 */
	public String getJoyoGrade() {
		return joyoGrade;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * @return the reading
	 */
	public String getReading() {
		return reading;
	}
	
	@Override
	public String toString() {
		return Character.toString(character);
	}
}

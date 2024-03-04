package tech.octopusdragon.japanesepractice.model;

/**
 * Holds information about the user's conjugation practice
 * @author Alex Gill
 *
 */
public class ConjugationPracticeData {
	private int curStreak;
	private int highestStreak;
	
	/**
	 * Initializes conjugation practice with no streaks
	 */
	public ConjugationPracticeData() {
		curStreak = 0;
		highestStreak = 0;
	}
	
	/**
	 * @return the current streak
	 */
	public int getCurStreak() {
		return curStreak;
	}
	
	/**
	 * @return the highest streak
	 */
	public int getHighestStreak() {
		return highestStreak;
	}
	
	/**
	 * Increments current streak by 1; increments highest streak by one if
	 * current streak is equal to highest streak
	 */
	public void incrementStreak() {
		if (curStreak == highestStreak) {
			curStreak++;
			highestStreak++;
		}
		else {
			curStreak++;
		}
	}
	
	/**
	 * Resets current streak to 0
	 */
	public void resetStreak() {
		curStreak = 0;
	}
}

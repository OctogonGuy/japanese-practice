package tech.octopusdragon.japanesepractice.model;

import java.util.List;
import java.util.Arrays;

/**
 * Contains logic of conjugation practice session
 * @author Alex Gill
 *
 */
public class ConjugationPractice {
	
	private Predicate curPredicate;
	private Conjugation curConjugation;
	
	/**
	 * Submits a guess of the conjugated form of the current predicate
	 * @param guess The user's guess
	 * @return whether the user guessed correctly
	 */
	public boolean submit(String guess) {
		boolean correct;
		List<String> correctAnswers = Arrays.asList(correctAnswers());
		if (correctAnswers.contains(guess)) {
			Userdata.getConjugationPracticeData().incrementStreak();
			correct = true;
		}
		else {
			Userdata.getConjugationPracticeData().resetStreak();
			correct = false;
		}
		Userdata.save();
		
		return correct;
	}
	
	/**
	 * Moves on to the next prompt
	 */
	public void next() {
		curPredicate = Scheduler.nextPredicate();
		curConjugation = Scheduler.nextConjugation(curPredicate);
	}
	
	/**
	 * @return the current representation of the current number + counter
	 */
	public String prompt() {
		return curPredicate.getDictionaryForm();
	}

	/**
	 * @return the current predicate
	 */
	public Predicate getCurPredicate() {
		return curPredicate;
	}

	/**
	 * @return the current conjugation
	 */
	public Conjugation getCurConjugation() {
		return curConjugation;
	}
	
	/**
	 * @return the conjugated form of the current predicate
	 */
	public String[] correctAnswers() {
		return curPredicate.conjugate(curConjugation);
	}

}

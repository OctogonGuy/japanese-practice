package tech.octopusdragon.japanesepractice.model;

/**
 * Contains logic of conjugation practice session
 * @author Alex Gill
 *
 */
public class ConjugationPractice {
	
	private Verb curVerb;
	private Conjugation curConjugation;
	
	/**
	 * Submits a guess of the conjugated form of the current verb
	 * @param guess The user's guess
	 */
	public void submit(String guess) {
		String correctAnswer = correctAnswer();
		if (correctAnswer.equals(guess))
			Userdata.getConjugationPracticeData().incrementStreak();
		else
			Userdata.getConjugationPracticeData().resetStreak();
		Userdata.save();
	}
	
	/**
	 * Moves on to the next prompt
	 */
	public void next() {
		curVerb = Scheduler.nextVerb();
		curConjugation = Scheduler.nextConjugation();
	}

	/**
	 * @return the current verb
	 */
	public Verb getCurVerb() {
		return curVerb;
	}

	/**
	 * @return the current conjugation
	 */
	public Conjugation getCurConjugation() {
		return curConjugation;
	}
	
	/**
	 * @return the conjugated form of the current verb
	 */
	public String correctAnswer() {
		return curVerb.conjugate(curConjugation);
	}

}

package tech.octopusdragon.japanesepractice.model;

import java.util.Random;

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
	 * @return whether the user guessed correctly
	 */
	public boolean submit(String guess) {
		boolean correct;
		String correctAnswer = correctAnswer();
		if (correctAnswer.equals(guess)) {
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
		curVerb = Scheduler.nextVerb();
		curConjugation = Scheduler.nextConjugation(curVerb);
	}
	
	/**
	 * @return the prompt language
	 */
	public PromptLanguage getPromptLanguage() {
		return Userdata.getConjugationPracticeData().getPromptLang();
	}
	
	/**
	 * Changes the prompt language
	 * @param lang The desired prompt language
	 */
	public void setPromptLanguage(PromptLanguage promptLang) {
		Userdata.getConjugationPracticeData().setPromptLang(promptLang);
		Userdata.save();
	}
	
	/**
	 * @return the current representation of the current number + counter
	 */
	public String prompt() {
		String prompt = "";
		switch (getPromptLanguage()) {
		case JAPANESE:
			prompt = curVerb.getDictionaryForm();
			break;
		case ENGLISH:
			prompt = curVerb.getMeaning();
			break;
		case RANDOM:
			Random rand = new Random();
			if (rand.nextBoolean())
				prompt = curVerb.getDictionaryForm();
			else
				prompt = curVerb.getMeaning();
			break;
		}
		return prompt;
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

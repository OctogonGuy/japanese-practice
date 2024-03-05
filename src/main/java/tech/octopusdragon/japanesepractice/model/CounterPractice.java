package tech.octopusdragon.japanesepractice.model;

import java.util.List;
import java.util.Random;

/**
 * Contains logic of counter practice session
 * @author Alex Gill
 *
 */
public class CounterPractice {

	private Counter curCounter;
	private int curNumber;
	
	/**
	 * Submits a guess of the reading of the current numeral + counter
	 * @param guess The user's guess
	 */
	public void submit(String guess) {
		List<String> correctAnswer = correctAnswers();
		if (correctAnswer.contains(guess))
			Userdata.getCounterPracticeData().incrementStreak();
		else
			Userdata.getCounterPracticeData().resetStreak();
		Userdata.save();
	}
	
	/**
	 * Moves on to the next prompt
	 */
	public void next() {
		curCounter = Scheduler.nextCounter();
		curNumber = Scheduler.nextNumber();
	}

	/**
	 * @return the current counter
	 */
	public Counter getCurCounter() {
		return curCounter;
	}

	/**
	 * @return the current number
	 */
	public int getCurNumber() {
		return curNumber;
	}
	
	/**
	 * @return the prompt language
	 */
	public PromptLanguage getPromptLanguage() {
		return Userdata.getCounterPracticeData().getPromptLang();
	}
	
	/**
	 * Changes the prompt language
	 * @param lang The desired prompt language
	 */
	public void setPromptLanguage(PromptLanguage promptLang) {
		Userdata.getCounterPracticeData().setPromptLang(promptLang);
	}
	
	/**
	 * @return the current representation of the current number + counter
	 */
	public String prompt() {
		String prompt = "";
		switch (Userdata.getCounterPracticeData().getPromptLang()) {
		case JAPANESE:
			prompt = curCounter.getKanjiRepresentation(curNumber);
			break;
		case ENGLISH:
			prompt = curCounter.getMeaning(curNumber);
			break;
		case RANDOM:
			Random rand = new Random();
			if (rand.nextBoolean())
				prompt = curCounter.getKanjiRepresentation(curNumber);
			else
				prompt = curCounter.getMeaning(curNumber);
			break;
		}
		return prompt;
	}
	
	/**
	 * @return the reading(s) of the current number + counter
	 */
	public List<String> correctAnswers() {
		return curCounter.getReadings(curNumber);
	}
	
	/**
	 * @return string form of the reading(s) of the current number + counter
	 */
	public String correctAnswersStr() {
		return curCounter.getReadings(curNumber).toString().replace("[", "").replace("]", "");
	}

}

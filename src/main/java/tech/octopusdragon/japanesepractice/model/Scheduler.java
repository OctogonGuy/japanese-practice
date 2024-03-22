package tech.octopusdragon.japanesepractice.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The Scheduler class reads data files and shows materials based on its
 * corresponding algorithm.
 * @author Alex Gill
 *
 */
public class Scheduler {
	private static final String LIST_DIRECTORY = "/lists/";
	private static final String KANJI_LIST_FILENAME = "kanji_list.csv";
	private static final String VERB_LIST_FILENAME = "verb_list.csv";
	private static final String COUNTER_LIST_FILENAME = "counter_list.csv";
	private static final String NUMBER_LIST_FILENAME = "number_list.csv";
	private static final String COUNTER_RULES_FILENAME = "counter_rules.csv";
	
	private static List<Kanji> kanjiList;
	private static List<Verb> verbList;
	private static List<Counter> counterList;
	
	
	static {
		kanjiList = readKanji();
		verbList = readVerbs();
		counterList = readCounters();
	}
	
	
	/**
	 * Reads kanji list file
	 * @return Kanji list
	 */
	private static List<Kanji> readKanji() {
		kanjiList = new ArrayList<Kanji>();
		// Read kanji list file
		try {
			InputStream kanjiListInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + KANJI_LIST_FILENAME);
			BufferedReader kanjiListFile = new BufferedReader(
					new InputStreamReader(kanjiListInputStream, "UTF-8"));
			String line;
			// Skip header
			kanjiListFile.readLine();
			while ((line = kanjiListFile.readLine()) != null) {
				String[] tokens = line.split(",");
				
				char character = tokens[0].charAt(0);
				int numStrokes = Integer.parseInt(tokens[1]);
				String joyoGrade = tokens[2];
				String meaning = tokens[3];
				String reading = tokens[4];
				Kanji kanji = new Kanji(character, numStrokes, joyoGrade,meaning, reading);
				
				// Add kanji to list
				kanjiList.add(kanji);
			}
			kanjiListInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return kanjiList;
	}
	
	
	/**
	 * Reads verb list file
	 * @return Verb list
	 */
	private static List<Verb> readVerbs() {
		verbList = new ArrayList<Verb>();
		// Read verb list file
		try {
			InputStream verbListInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + VERB_LIST_FILENAME);
			BufferedReader verbListFile = new BufferedReader(
					new InputStreamReader(verbListInputStream, "UTF-8"));
			String line;
			// Skip header
			verbListFile.readLine();
			while ((line = verbListFile.readLine()) != null) {
				
				String[] tokens = line.split(",", 4);
				String dictionaryForm = tokens[0];
				String reading = tokens[1];
				VerbGroup verbGroup = VerbGroup.valueOf(tokens[2]);
				String meaning = tokens[3].replaceAll("\"", "");
				Verb verb = new Verb(dictionaryForm, reading, verbGroup, meaning);
				
				// Add verb to list
				verbList.add(verb);
			}
			verbListInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verbList;
	}
	
	
	/**
	 * Reads counter list file
	 * @return Counter list
	 */
	private static List<Counter> readCounters() {
		
		// Read number list file
		Map<Integer, Number> numberList = new HashMap<Integer, Number>();
		try {
			InputStream numberListInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + NUMBER_LIST_FILENAME);
			BufferedReader numberListFile = new BufferedReader(
					new InputStreamReader(numberListInputStream, "UTF-8"));
			String line;
			// Skip header
			numberListFile.readLine();
			while ((line = numberListFile.readLine()) != null) {
				String[] tokens = line.split(",");
				
				int number = Integer.parseInt(tokens[0]);
				String kanjiNumeral = tokens[1];
				List<String> readings = Arrays.asList(tokens[2].split(";"));
				String meaning = tokens[3];
				
				// Add number to list
				numberList.put(number, new Number(number, kanjiNumeral, readings, meaning));
			}
			numberListInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Read counter rules file
		Map<CounterType, Map<String, Map<Integer, String>>> counterRuleMap = new HashMap<>();
		for (CounterType counterType : CounterType.values()) {
			counterRuleMap.put(counterType, new HashMap<String, Map<Integer, String>>());
		}
		try {
			InputStream counterRulesInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + COUNTER_RULES_FILENAME);
			BufferedReader counterRulesFile = new BufferedReader(
					new InputStreamReader(counterRulesInputStream, "UTF-8"));
			String line;
			// Skip header
			counterRulesFile.readLine();
			while ((line = counterRulesFile.readLine()) != null) {
				String[] tokens = line.split(",", -1);
				
				CounterType counterType = CounterType.valueOf(tokens[0]);
				String firstKana = tokens[1];
				
				Map<Integer, String> prefixMap = new HashMap<Integer, String>();
				counterRuleMap.get(counterType).put(firstKana, prefixMap);
				for (int i = 2; i < tokens.length; i++) {
					int number = i - 1;
					if (!tokens[i].isEmpty()) {
						prefixMap.put(number, tokens[i]);
					}
				}
			}
			counterRulesInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Read counter list file
		counterList = new ArrayList<Counter>();
		try {
			InputStream counterListInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + COUNTER_LIST_FILENAME);
			BufferedReader counterListFile = new BufferedReader(
					new InputStreamReader(counterListInputStream, "UTF-8"));
			String line;
			// Skip header
			counterListFile.readLine();
			while ((line = counterListFile.readLine()) != null) {
				String[] tokens = line.split(",");
				
				String suffix = tokens[0];
				String[] readings = tokens[1].split(";");
				CounterType type = CounterType.valueOf(tokens[2]);
				String meaningSingular = tokens[3];
				String meaningPlural = tokens[4];
				int maxNum;
				String[] irregularMeanings;
				String[][] irregularReadings = new String[10][];
				try {
					maxNum = Integer.parseInt(tokens[5]);
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					maxNum = -1;
				}
				try {
					irregularMeanings = Arrays.copyOfRange(tokens, 6, 16);
					for (int i = 0; i < irregularMeanings.length; i++)
						if (irregularMeanings[i] == null)
							irregularMeanings[i] = "";
				} catch (ArrayIndexOutOfBoundsException e) {
					irregularMeanings = new String[10];
					for (int i = 0; i < irregularMeanings.length; i++) irregularMeanings[i] = "";
				}
				try {
					String[] irregularReadingsTokens = Arrays.copyOfRange(tokens, 16, 26);
					for (int i = 0; i < irregularReadingsTokens.length; i++) {
						if (irregularReadingsTokens[i] == null)
							irregularReadings[i] = new String[] {""};
						else
							irregularReadings[i] = irregularReadingsTokens[i].split(";");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					irregularReadings = new String[10][1];
					for (int i = 0; i < irregularReadings.length; i++) irregularReadings[i][0] = "";
				}
				
				Counter counter = new Counter(suffix, maxNum);
				
				// Fill all numbers
				for (Number number : numberList.values()) {
					// Stop adding numbers if greater than max number
					if (maxNum != -1 && number.getNumber() > maxNum) break;
					
					String kanjiCompound = number.getKanjiNumeral() + suffix;
					
					// TODO - redo this
					List<String> kanaCompounds = new ArrayList<String>();
					// If there is no specified beginning kana for this counter rule
					if (counterRuleMap.get(type).containsKey("") && counterRuleMap.get(type).get("").containsKey(number.getNumber())) {
						String[] numberReadings = counterRuleMap.get(type).get("").get(number.getNumber()).split(";");
						for (int i = 0; i < numberReadings.length; i++) {
							if (!irregularReadings[number.getNumber() - 1][0].equals("")) {
								for (int j = 0; j < irregularReadings[i].length; j++) {
									kanaCompounds.add(numberReadings[i] + irregularReadings[i][j]);
								}
							}
							else {
								for (int j = 0; j < readings.length; j++) {
									kanaCompounds.add(numberReadings[i] + readings[j]);
								}
							}
						}
					}
					// If there is a specified special prefix for the beginning kana
					else if (!counterRuleMap.get(type).containsKey("") && counterRuleMap.get(type).get(readings[0].substring(0, 1)).containsKey(number.getNumber())) {
						String[] prefixReadings = counterRuleMap.get(type).get(readings[0].substring(0, 1)).get(number.getNumber()).split(";");
						for (int i = 0; i < prefixReadings.length; i++) {
							if (!irregularReadings[number.getNumber() - 1][0].equals("") && irregularReadings[number.getNumber() - 1][0].length() > 1) {
								for (int j = 0; j < irregularReadings[number.getNumber() - 1].length; j++) {
									kanaCompounds.add(irregularReadings[number.getNumber() - 1][j]);
								}
							}
							else if (readings[0].length() > 1) {
								for (int j = 0; j < readings.length; j++) {
									kanaCompounds.add(prefixReadings[i] + readings[j].substring(1));
								}
							}
							else {
								kanaCompounds.add(prefixReadings[i]);
							}
						}
					}
					// If there is not a specified special prefix for the beginning kana
					else if (type != CounterType.IRREGULAR) {
						for (int i = 0; i < number.getReadings().size(); i++) {
							if (!irregularReadings[number.getNumber() - 1][0].equals("")) {
								for (int j = 0; j < irregularReadings[number.getNumber() - 1].length; j++) {
									kanaCompounds.add(irregularReadings[number.getNumber() - 1][j]);
								}
							}
							else {
								for (int j = 0; j < readings.length; j++) {
									kanaCompounds.add(number.getReadings().get(i) + readings[j]);
								}
							}
						}
					}
					// If there is no counter rule (irregular counter)
					else {
						for (int j = 0; j < irregularReadings[number.getNumber() - 1].length; j++) {
							kanaCompounds.add(irregularReadings[number.getNumber() - 1][j]);
						}
					}
					
					String meaningCompound;
					if (!irregularMeanings[number.getNumber() - 1].equals("")) {
						meaningCompound = irregularMeanings[number.getNumber() - 1];
					}
					else if (number.getNumber() == 1) {
						meaningCompound = number.getMeaning() + " " + meaningSingular;
					}
					else {
						meaningCompound = number.getMeaning() + " " + meaningPlural;
					}
					
					counter.putCompound(number.getNumber(), kanjiCompound, kanaCompounds, meaningCompound);
				}
				
				// Add counter to list
				counterList.add(counter);
			}
			counterListInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return counterList;
	}
	
	
	/**
	 * Generates a random unlearned kanji from the lowest available joyo level
	 * @return Kanji The kanji to learn or null if all kanji have been learned
	 */
	public static Kanji nextLearnKanji() {
		// Make list of unlearned kanji
		List<Kanji> unlearnedKanjiList = new ArrayList<>(kanjiList);
		for (Kanji kanji : kanjiList) {
			if (Userdata.getKanjiPracticeData().kanjiMap().keySet().contains(kanji.getCharacter())) {
				unlearnedKanjiList.remove(kanji);
			}
		}
		
		// If list is empty, return null
		if (unlearnedKanjiList.isEmpty())
			return null;
		
		// Narrow list to unlearned kanji of lowest available joyo level
		int lowestJoyoLevel = Integer.MAX_VALUE;
		List<Kanji> lowestLevelKanjiList = new ArrayList<>();
		for (Kanji kanji : unlearnedKanjiList) {
			int joyoLevel = kanji.getJoyoGrade().equals("S")? 7 : Integer.parseInt(kanji.getJoyoGrade());
			if (joyoLevel < lowestJoyoLevel) {
				lowestJoyoLevel = joyoLevel;
				lowestLevelKanjiList.clear();
			}
			if (joyoLevel == lowestJoyoLevel) {
				lowestLevelKanjiList.add(kanji);
			}
		}
		
		// Choose and return next kanji of lowest joyo level
		return lowestLevelKanjiList.get(0);
	}
	
	
	/**
	 * Generates a random kanji
	 * @return Kanji The kanji to review or null if there is none to review
	 */
	public static Kanji nextReviewKanji() {
		// Make list of learned kanji
		List<Kanji> learnedKanjiList = new ArrayList<>(kanjiList);
		for (Kanji kanji : kanjiList) {
			if (!Userdata.getKanjiPracticeData().kanjiMap().keySet().contains(kanji.getCharacter())) {
				learnedKanjiList.remove(kanji);
			}
		}
		
		// If list is empty, return null
		if (learnedKanjiList.isEmpty())
			return null;
		
		// Compute weighted number based on SRS stages
		int totalWeight = 0;
		for (Kanji kanji : learnedKanjiList) {
			int srsStage = Userdata.getKanjiPracticeData().kanjiMap().get(kanji.getCharacter());
			totalWeight += KanjiPracticeData.STAGES - (srsStage - 1);
		}
		
		// Choose weighted random kanji
		int index = 0;
		Random rand = new Random();
		for (int r = rand.nextInt(totalWeight); index < learnedKanjiList.size(); index++) {
			int srsStage = Userdata.getKanjiPracticeData().kanjiMap().get(learnedKanjiList.get(index).getCharacter());
			int weight = KanjiPracticeData.STAGES - (srsStage - 1);
			r -= weight;
			if (r < 0) break;
		}
		return learnedKanjiList.get(index);
	}
	
	
	/**
	 * Saves the user's progress on a kanji to the userdata file depending on
	 * whether the user wrote it correctly or not
	 * @param kanji The kanji to recorc
	 * @param correct Whether the user wrote the kanji correctly
	 */
	public static void recordKanji(Kanji kanji, boolean correct) {
		
	}
	
	
	/**
	 * Generates a random verb
	 * @return Verb
	 */
	public static Verb nextVerb() {
		Random rand = new Random();
		// Incorporate verb group weighted chances
		VerbGroup verbGroup = VerbGroup.weightedSelection(rand.nextDouble());
		List<Verb> filteredVerbList = verbList.stream()
				.filter(verb -> verb.getVerbGroup() == verbGroup)
				.collect(Collectors.toList());
		Verb verb = filteredVerbList.get(rand.nextInt(filteredVerbList.size()));
		return verb;
	}
	
	
	/**
	 * Generates a random conjugation
	 * @param The verb to conjugate
	 * @return Conjugation
	 */
	public static Conjugation nextConjugation(Verb verb) {
		Random rand = new Random();
		// Remove conjugations that do not exist for the given verb
		List<Conjugation> conjugations = new ArrayList<>(Arrays.asList(Conjugation.values()));
		for (Conjugation conjugation : Conjugation.values()) {
			try {
				verb.conjugate(conjugation);
			}
			catch (InvalidConjugationException e) {
				conjugations.remove(conjugation);
			}
		}
		Conjugation conjugation = conjugations.get(rand.nextInt(conjugations.size()));
		return conjugation;
	}
	
	
	/**
	 * Generates a random counter
	 * @return Counter
	 */
	public static Counter nextCounter() {
		Random rand = new Random();
		Counter counter = counterList.get(rand.nextInt(counterList.size()));
		return counter;
	}
	
	
	/**
	 * Generates a random number
	 * @param counter The counter to attach the number to (necessary to determine max number)
	 * @return Number
	 */
	public static int nextNumber(Counter counter) {
		Random rand = new Random();
		int number;
		if (counter.getMaxNumber() == -1) {
			number = rand.nextInt(10) + 1;
		}
		else {
			number = Math.min(rand.nextInt(counter.getMaxNumber()), 9) + 1;
		}
		return number;
	}
}

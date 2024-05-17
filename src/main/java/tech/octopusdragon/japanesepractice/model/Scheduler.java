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
	private static final String PREDICATE_LIST_NAME = "predicate_list.csv";
	private static final String COUNTER_LIST_FILENAME = "counter_list.csv";
	private static final String NUMBER_LIST_FILENAME = "number_list.csv";
	private static final String COUNTER_RULES_FILENAME = "counter_rules.csv";
	private static final String GRAMMAR_LIST_FILENAME = "grammar.csv";
	
	public static List<Kanji> kanjiList;
	public static List<Predicate> predicateList;
	public static List<Counter> counterList;
	public static List<Grammar> grammarList;
	
	
	static {
		kanjiList = readKanji();
		predicateList = readPredicates();
		counterList = readCounters();
		grammarList = readGrammar();
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
	 * Reads grammar list file
	 * @return Grammar list
	 */
	private static List<Grammar> readGrammar() {
		grammarList = new ArrayList<Grammar>();
		// Read grammar list file
		try {
			InputStream grammarListInputSteam =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + GRAMMAR_LIST_FILENAME);
			BufferedReader grammarListFile = new BufferedReader(
					new InputStreamReader(grammarListInputSteam, "UTF-8"));
			String line;
			// Skip header
			grammarListFile.readLine();
			while ((line = grammarListFile.readLine()) != null) {
				String[] tokens = line.split(",");
				String grammar = tokens[0];
				int jlpt = Integer.parseInt(tokens[1]);
				grammarList.add(new Grammar(grammar, jlpt));
			}
			grammarListInputSteam.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return grammarList;
	}
	
	
	/**
	 * Reads predicate list file
	 * @return Predicate list
	 */
	private static List<Predicate> readPredicates() {
		predicateList = new ArrayList<Predicate>();
		// Read predicate list file
		try {
			InputStream predicateListInputStream =
					Scheduler.class.getResourceAsStream(LIST_DIRECTORY + PREDICATE_LIST_NAME);
			BufferedReader predicateListFile = new BufferedReader(
					new InputStreamReader(predicateListInputStream, "UTF-8"));
			String line;
			// Skip header
			predicateListFile.readLine();
			while ((line = predicateListFile.readLine()) != null) {
				String[] tokens = line.split(",", 4);
				String dictionaryForm = tokens[0];
				String reading = tokens[1];
				for (String predicateGroupString : tokens[2].split(";")) {
					// For predicate groups that don't exist yet
					try {
						PredicateGroup.valueOf(predicateGroupString);
					} catch (IllegalArgumentException e) {
						continue;
					}
					
					PredicateGroup predicateGroup = PredicateGroup.valueOf(predicateGroupString);
					String modifiedDictionaryForm = dictionaryForm;
					String modifiedReading = reading;
					if (predicateGroup == PredicateGroup.SURU && !dictionaryForm.endsWith("する")) {
						modifiedDictionaryForm = dictionaryForm + "する";
						modifiedReading = reading + "する";
					}
					Predicate predicate = new Predicate(modifiedDictionaryForm, modifiedReading, predicateGroup);
					// Add predicate to list
					predicateList.add(predicate);
				}
			}
			predicateListInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return predicateList;
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
					List<String> kanaCompounds = new ArrayList<String>();
					int numberIndex = number.getNumber() - 1;
					String[] numberReadings = null;
					String[] prefixReadings = null;
					
					// If there are special numeral reading(s), use that as the number reading(s)
					if (counterRuleMap.get(type).containsKey("") &&
							counterRuleMap.get(type).get("").containsKey(number.getNumber())) {
						numberReadings = counterRuleMap.get(type).get("").get(number.getNumber()).split(";");
					}
					// If there are special prefix reading(s) (number + beginning of counter), use those
					else if (!counterRuleMap.get(type).containsKey("") &&
							counterRuleMap.get(type).get(readings[0].substring(0, 1)).containsKey(number.getNumber())) {
						prefixReadings = counterRuleMap.get(type).get(readings[0].substring(0, 1)).get(number.getNumber()).split(";");
					}
					// Otherwise, use normal number reading(s)
					else {
						numberReadings = number.getReadings().toArray(new String[0]);
					}
					
					// If irregular reading, use that
					if (!irregularReadings[numberIndex][0].isEmpty()) {
						for (String reading : irregularReadings[numberIndex])
							kanaCompounds.add(reading);
					}
					// If beginning kana and special prefix, prefix + counter
					else if (prefixReadings != null) {
						for (int i = 0; i < prefixReadings.length; i++)
							for (int j = 0; j < readings.length; j++)
								kanaCompounds.add(prefixReadings[i] + readings[j].substring(1));
					}
					// Otherwise, number + counter
					else {
						for (int i = 0; i < numberReadings.length; i++)
							for (int j = 0; j < readings.length; j++)
								kanaCompounds.add(numberReadings[i] + readings[j]);
					}
					
					// Construct English translation
					String meaningCompound;
					if (!irregularMeanings[numberIndex].equals("")) {
						meaningCompound = irregularMeanings[numberIndex];
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
	 * Generates a random predicate
	 * @return Predicate
	 */
	public static Predicate nextPredicate() {
		Random rand = new Random();
		// Incorporate predicate group weighted chances
		PredicateGroup predicateGroup = PredicateGroup.weightedSelection(rand.nextDouble());
		List<Predicate> filteredPredicateList = predicateList.stream()
				.filter(predicate -> predicate.getPredicateGroup() == predicateGroup)
				.collect(Collectors.toList());
		Predicate predicate = filteredPredicateList.get(rand.nextInt(filteredPredicateList.size()));
		return predicate;
	}
	
	
	/**
	 * Generates a random conjugation
	 * @param The predicate to conjugate
	 * @return Conjugation
	 */
	public static Conjugation nextConjugation(Predicate predicate) {
		Random rand = new Random();
		// Remove conjugations that do not exist for the given predicate
		List<Conjugation> conjugations = new ArrayList<>(Arrays.asList(Conjugation.values()));
		for (Conjugation conjugation : Conjugation.values()) {
			try {
				predicate.conjugate(conjugation);
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
	 * Generates a random grammar
	 * @return Grammar
	 */
	public static Grammar nextGrammar(List<Integer> jlptLevels) {
		Random rand = new Random();
		List<Grammar> filteredGrammarList = grammarList.stream().filter(g -> jlptLevels.contains(g.getJlpt())).collect(Collectors.toList());
		Grammar grammar = filteredGrammarList.get(rand.nextInt(filteredGrammarList.size()));
		return grammar;
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

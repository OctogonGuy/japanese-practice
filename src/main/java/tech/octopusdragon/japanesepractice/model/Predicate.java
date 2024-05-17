package tech.octopusdragon.japanesepractice.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a predicate that can be conjugated
 * @author Alex Gill
 *
 */
public class Predicate {
	
	// --- Conjugation information ---

	private static final String LIST_DIRECTORY = "/lists/";
	private static final String CONJUGATION_RULES_FILENAME = "conjugation_rules.csv";
	
	private static Map<PredicateGroup, Map<Character, Map<Conjugation, String[]>>> conjugationMap;
	
	static {
		conjugationMap = new HashMap<>();
		try {
			InputStream conjugationRulesInputStream =
					Predicate.class.getResourceAsStream(LIST_DIRECTORY + CONJUGATION_RULES_FILENAME);
			BufferedReader conjugationRulesFile = new BufferedReader(
					new InputStreamReader(conjugationRulesInputStream, "UTF-8"));
			String line;
			String[] tokens = conjugationRulesFile.readLine().split(",");
			Conjugation[] conjugations = new Conjugation[tokens.length - 2];
			for (int i = 2; i < tokens.length; i++) {
				conjugations[i - 2] = Conjugation.valueOf(tokens[i]);
			}
			while ((line = conjugationRulesFile.readLine()) != null) {
				tokens = line.split(",");
				PredicateGroup predicateGroup = PredicateGroup.valueOf(tokens[0]);
				if (!conjugationMap.containsKey(predicateGroup))
					conjugationMap.put(predicateGroup, new HashMap<>());
				char endingKana = !tokens[1].isEmpty() ? tokens[1].charAt(0) : '*';
				conjugationMap.get(predicateGroup).put(endingKana, new HashMap<>());
				for (int i = 0; i < conjugations.length; i++) {
					String suffixToken = i + 2 < tokens.length ? tokens[i + 2] : "";
					String[] suffixes = suffixToken.split(";");
					conjugationMap.get(predicateGroup).get(endingKana).put(conjugations[i], suffixes);
				}
			}
			conjugationRulesInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private String dictionaryForm;
	private String reading;
	private PredicateGroup predicateGroup;
	
	public Predicate(String dictionaryForm, String reading, PredicateGroup predicateGroup) {
		this.dictionaryForm = dictionaryForm;
		this.reading = reading;
		this.predicateGroup = predicateGroup;
	}

	/**
	 * @return the dictionary form
	 */
	public String getDictionaryForm() {
		return dictionaryForm;
	}

	/**
	 * @return the hiragana reading
	 */
	public String getReading() {
		return reading;
	}

	/**
	 * @return the predicate group
	 */
	public PredicateGroup getPredicateGroup() {
		return predicateGroup;
	}
	
	/**
	 * Returns the predicate conjugated to the given form
	 * @param conjugation The conjugation
	 * @return The conjugated form
	 * @exception InvalidConjugationException When the given conjugation does
	 * not exist for this predicate.
	 */
	public String[] conjugate(Conjugation conjugation) throws InvalidConjugationException {
		String stem = reading.length() >= 1 ? reading.substring(0, reading.length() - 1) : null;
		String root = reading.length() >= 2 ? reading.substring(0, reading.length() - 2) : null;
		char lastKana = !conjugationMap.get(predicateGroup).containsKey('*') ? reading.charAt(reading.length() - 1) : '*';
		String[] suffixes = conjugationMap.get(predicateGroup).get(lastKana).get(conjugation).clone();
		for (int i = 0; i < suffixes.length; i++) {
			if (suffixes[i].contains("**")) {
				suffixes[i] = suffixes[i].replace("**", root);
			}
			else if (suffixes[i].contains("*")) {
				suffixes[i] = suffixes[i].replace("*", stem);
			}
			else if (suffixes[i].equals("")) {
				throw new InvalidConjugationException();
			}
			else {
				suffixes[i] = reading + suffixes[i];
			}
		}
		return suffixes;
	}
	
	@Override
	public String toString() {
		return dictionaryForm;
	}
}

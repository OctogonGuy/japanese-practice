package tech.octopusdragon.japanesepractice.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a verb that can be conjugated
 * @author Alex Gill
 *
 */
public class Verb {
	
	// --- Conjugation information ---

	private static final String LIST_DIRECTORY = "/lists/";
	private static final String CONJUGATION_RULES_FILENAME = "conjugation_rules.csv";
	
	private static Map<VerbGroup, Map<Character, Map<Conjugation, String>>> conjugationMap;
	
	static {
		conjugationMap = new HashMap<>();
		try {
			InputStream conjugationRulesInputStream =
					Verb.class.getResourceAsStream(LIST_DIRECTORY + CONJUGATION_RULES_FILENAME);
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
				VerbGroup verbGroup = VerbGroup.valueOf(tokens[0]);
				if (!conjugationMap.containsKey(verbGroup))
					conjugationMap.put(verbGroup, new HashMap<>());
				char endingKana = tokens[1].charAt(0);
				conjugationMap.get(verbGroup).put(endingKana, new HashMap<>());
				for (int i = 2; i < tokens.length; i++) {
					conjugationMap.get(verbGroup).get(endingKana).put(conjugations[i - 2], tokens[i]);
				}
			}
			conjugationRulesInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private String dictionaryForm;
	private String reading;
	private VerbGroup verbGroup;
	private String meaning;
	
	public Verb(String dictionaryForm, String reading, VerbGroup verbGroup, String meaning) {
		this.dictionaryForm = dictionaryForm;
		this.reading = reading;
		this.verbGroup = verbGroup;
		this.meaning = meaning;
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
	 * @return the verb group
	 */
	public VerbGroup getVerbGroup() {
		return verbGroup;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}
	
	/**
	 * Returns the verb conjugated to the given form
	 * @param conjugation The conjugation
	 * @return The conjugated form
	 */
	public String conjugate(Conjugation conjugation) {
		String stem = reading.substring(0, reading.length() - 1);
		String root = reading.substring(0, reading.length() - 2);
		char lastKana = reading.charAt(reading.length() - 1);
		String suffix = conjugationMap.get(verbGroup).get(lastKana).get(conjugation);
		if (suffix.contains("**")) {
			suffix = suffix.replace("**", root);
		}
		else if (suffix.contains("*")) {
			suffix = suffix.replace("*", stem);
		}
		else if (suffix.equals("")) {
			suffix = reading;
		}
		return suffix;
	}
	
	@Override
	public String toString() {
		return dictionaryForm;
	}
}

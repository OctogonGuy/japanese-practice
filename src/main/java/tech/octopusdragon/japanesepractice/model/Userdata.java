package tech.octopusdragon.japanesepractice.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

/**
 * Keeps track of user's progress. Has instance members so that data can be
 * serialized and static members so the class can be accessed from anywhere
 * in the program
 * @author Alex Gill
 *
 */
public class Userdata {
	
	// --- Static members ---
	
	private static final String USERDATA_DIRECTORY = "userdata/";
	private static final String USERDATA_FILENAME = "japanese_practice.json";
	
	private static Userdata userdata;
	private static Gson gson;
	
	static {
		// Instantiate using GsonBuilder to include newlines, whitespace, and
		// indentation
		gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Make sure userdata folder exists
		File folder = new File(USERDATA_DIRECTORY);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		// Get data
		try {
			userdata = gson.fromJson(new JsonReader(new InputStreamReader(new FileInputStream(
					USERDATA_DIRECTORY + USERDATA_FILENAME), "UTF-8")), Userdata.class);
		} catch (JsonParseException | IOException e) {
			userdata = new Userdata();
		}
	}
	
	/**
	 * Saves data to JSON
	 */
	public static void save() {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(
					USERDATA_DIRECTORY + USERDATA_FILENAME), "UTF-8");
			gson.toJson(userdata, writer);
			writer.flush();
			writer.close();
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the counter practice data
	 */
	public static CounterPracticeData getCounterPracticeData() {
		if (userdata.counterPracticeData == null)
			userdata.counterPracticeData = new CounterPracticeData();
		return userdata.counterPracticeData;
	}
	
	/**
	 * @return the conjugation practice data
	 */
	public static ConjugationPracticeData getConjugationPracticeData() {
		if (userdata.conjugationPracticeData == null)
			userdata.conjugationPracticeData = new ConjugationPracticeData();
		return userdata.conjugationPracticeData;
	}
	
	/**
	 * @return the kanji practice data
	 */
	public static KanjiPracticeData getKanjiPracticeData() {
		if (userdata.kanjiPracticeData == null)
			userdata.kanjiPracticeData = new KanjiPracticeData();
		return userdata.kanjiPracticeData;
	}
	
	/**
	 * @return the grammar practice data
	 */
	public static GrammarPracticeData getGrammarPracticeData() {
		if (userdata.grammarPracticeData == null)
			userdata.grammarPracticeData = new GrammarPracticeData();
		return userdata.grammarPracticeData;
	}
	
	
	
	// --- Instance members ---
	
	private CounterPracticeData counterPracticeData;
	private ConjugationPracticeData conjugationPracticeData;
	private KanjiPracticeData kanjiPracticeData;
	private GrammarPracticeData grammarPracticeData;
}

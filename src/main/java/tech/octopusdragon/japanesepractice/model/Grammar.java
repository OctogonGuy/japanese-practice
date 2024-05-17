package tech.octopusdragon.japanesepractice.model;

/**
 * Represents a grammar point
 * @author Alex Gill
 *
 */
public class Grammar {
	private String grammar;
	private int jlpt;
	
	public String getGrammar() {
		return grammar;
	}

	public int getJlpt() {
		return jlpt;
	}

	public Grammar(String grammar, int jlpt) {
		this.grammar = grammar;
		this.jlpt = jlpt;
	}
}

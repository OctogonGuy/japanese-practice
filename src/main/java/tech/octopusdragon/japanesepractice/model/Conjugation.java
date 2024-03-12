package tech.octopusdragon.japanesepractice.model;

/**
 * Conjugations of Japanese verbs
 * @author Alex Gill
 *
 */
public enum Conjugation {
	INF_NONPAST("常体"),
	INF_PAST("常体　過去形"),
	INF_NEG_NONPAST("常体　否定形"),
	INF_NEG_PAST("常体　過去・否定形"),
	FORMAL_NONPAST("敬体"),
	FORMAL_PAST("敬体　過去形"),
	FORMAL_NEG_NONPAST("敬体　否定形"),
	FORMAL_NEG_PAST("敬体　過去・否定形"),
	CONDITIONAL("仮定形"),
	VOLITIONAL("意志形"),
	TE("て形");
	
	
	
	private String term;
	
	/**
	 * @param term The Japanese term for the conjugation
	 */
	private Conjugation(String term) {
		this.term = term;
	}
	
	@Override
	public String toString() {
		return term;
	}
}

package tech.octopusdragon.japanesepractice.model;

/**
 * Groups of conjugation patterns for Japanese verbs
 * @author Alex Gill
 *
 */
public enum VerbGroup {
	ICHIDAN(30),	// 見る, 寝る, ...
	GODAN(150),	// 読む, 泳ぐ, 乗る, ...
	SURU(12),	// する
	KURU(2),	// 来る
	POLITE(5),	// いらっしゃる, ござる, ...
	ARU(1);		// ある
	
	private int weight;	// Relative chance of being selected
	
	/**
	 * Constructor
	 * @param weight Relative chance of being selected
	 */
	private VerbGroup(int weight) {
		this.weight = weight;
	}
	
	/**
	 * Returns a verb group based on their weighted probabilities
	 * @param randNum A random double between 0.0 and 1.0
	 * @return The corresponding verb group
	 */
	public static VerbGroup weightedSelection(double randNum) {
		int totalWeight = 0;
		for (VerbGroup verbGroup : VerbGroup.values()) {
			totalWeight += verbGroup.weight;
		}
		double weightedIndex = randNum * totalWeight;
		int cumulativeWeight = 0;
		for (VerbGroup verbGroup : VerbGroup.values()) {
			cumulativeWeight += verbGroup.weight;
			if (weightedIndex <= cumulativeWeight) {
				return verbGroup;
			}
		}
		return null;
	}
}

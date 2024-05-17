package tech.octopusdragon.japanesepractice.model;

/**
 * Groups of conjugation patterns for Japanese predicates
 * @author Alex Gill
 *
 */
public enum PredicateGroup {
	ICHIDAN(30),		// 見る, 寝る, ...
	GODAN(150),			// 読む, 泳ぐ, 行く, 乗る, ...
	SURU(12),			// する
	KURU(2),			// 来る
	POLITE(5),			// いらっしゃる, ござる, ...
	ARU(1),				// ある
	II_YOI(2),			// いい
	I_ADJECTIVE(40),
	NA_ADJECTIVE(40),
	NOUN(50);
	
	private int weight;	// Relative chance of being selected
	
	/**
	 * Constructor
	 * @param weight Relative chance of being selected
	 */
	private PredicateGroup(int weight) {
		this.weight = weight;
	}
	
	/**
	 * Returns a verb group based on their weighted probabilities
	 * @param randNum A random double between 0.0 and 1.0
	 * @return The corresponding verb group
	 */
	public static PredicateGroup weightedSelection(double randNum) {
		int totalWeight = 0;
		for (PredicateGroup verbGroup : PredicateGroup.values()) {
			totalWeight += verbGroup.weight;
		}
		double weightedIndex = randNum * totalWeight;
		int cumulativeWeight = 0;
		for (PredicateGroup verbGroup : PredicateGroup.values()) {
			cumulativeWeight += verbGroup.weight;
			if (weightedIndex <= cumulativeWeight) {
				return verbGroup;
			}
		}
		return null;
	}
}

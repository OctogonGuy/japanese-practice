package tech.octopusdragon.japanesepractice;

import tech.octopusdragon.japanesepractice.model.Conjugation;
import tech.octopusdragon.japanesepractice.model.InvalidConjugationException;
import tech.octopusdragon.japanesepractice.model.Predicate;
import tech.octopusdragon.japanesepractice.model.Scheduler;

/**
 * Makes sure that all valid conjugations work for all predicates
 */
public class ConjugationTest {

	public static void main(String[] args) {
		for (Predicate predicate : Scheduler.predicateList) {
			for (Conjugation conjugation : Conjugation.values()) {
				try {
					predicate.conjugate(conjugation);
				}
				catch (InvalidConjugationException e) {
					
				}
			}
		}
	}

}

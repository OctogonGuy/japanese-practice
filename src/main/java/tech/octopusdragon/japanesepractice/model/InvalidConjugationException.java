package tech.octopusdragon.japanesepractice.model;

/**
 * Should be thrown when trying to access a conjugated form that doesn't exist
 * for a certain predicate.
 * @author Alex Gill
 *
 */
public class InvalidConjugationException extends RuntimeException {
	private static final long serialVersionUID = 6219656561305344184L;
	public InvalidConjugationException() {
		super();
	}
	@Override
	public String getMessage() {
		return "No conjugated form of the specified type exists for this predicate.";
	}
}

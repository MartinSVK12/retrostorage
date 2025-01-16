package sunsetsatellite.retrostorage.util;

/**
 * Represents a function that accepts no arguments and produces no result besides side effects.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #run()}.
 */
@FunctionalInterface
public interface Procedure {

	/**
	 * Runs the procedure.
	 */
	void run();
}

package com.crossover.trial.journals.notify;

/**
 * MessageBuilder interface
 *
 */
@FunctionalInterface
public interface MessageBuilder {
	
	public String build(String message);
	
}

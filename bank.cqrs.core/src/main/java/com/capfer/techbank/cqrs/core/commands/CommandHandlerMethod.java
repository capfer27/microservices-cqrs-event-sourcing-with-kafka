package com.capfer.techbank.cqrs.core.commands;

/**
 * Represents a single command handler method that registers all of our
 * subsequent commands.
 * @param <T> the command to fire
 */
@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {

    void handle(T command);

}

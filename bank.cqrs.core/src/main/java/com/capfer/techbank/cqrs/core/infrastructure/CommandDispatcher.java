package com.capfer.techbank.cqrs.core.infrastructure;

import com.capfer.techbank.cqrs.core.commands.BaseCommand;
import com.capfer.techbank.cqrs.core.commands.CommandHandlerMethod;

/**
 * The main command dispatcher.
 * Used to register commands handlers, to send or dispatch.
 */
public interface CommandDispatcher {

    /**
     * Register commands handlers
     * @param type
     * @param handlerMethod
     * @param <T>
     */
    <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handlerMethod);

    /**
     * Sends or dispatches a command
     * @param baseCommand
     */
   void send(BaseCommand baseCommand);
}

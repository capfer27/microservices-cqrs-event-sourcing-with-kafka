package com.capfer.techbank.cmd.infrastructure;

import com.capfer.techbank.cqrs.core.commands.BaseCommand;
import com.capfer.techbank.cqrs.core.commands.CommandHandlerMethod;
import com.capfer.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AccountCommandDispatcher implements CommandDispatcher {

    private final Map<Class<? extends BaseCommand>, List<CommandHandlerMethod>> routes = new HashMap<>();

    @Override
    public <T extends BaseCommand> void registerHandler(Class<T> type, CommandHandlerMethod<T> handlerMethod) {
        List<CommandHandlerMethod> handlerMethods =
                routes.computeIfAbsent(type, c -> new LinkedList<>());

        handlerMethods.add(handlerMethod);
    }

    @Override
    public void send(BaseCommand baseCommand) {
        List<CommandHandlerMethod> handlerMethods = routes.get(baseCommand.getClass());

        if (handlerMethods == null || handlerMethods.isEmpty()) {
            throw new RuntimeException("No command handler was registered");
        }

        if (handlerMethods.size() > 1) {
            throw new RuntimeException("Cannot send command to more than one handler!");
        }

        handlerMethods.getFirst().handle(baseCommand);

    }
}

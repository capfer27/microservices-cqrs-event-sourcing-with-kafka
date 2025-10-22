package com.capfer.techbank.cmd.api.commands;

import com.capfer.techbank.cqrs.core.commands.BaseCommand;

//@EqualsAndHashCode(callSuper = true)
//@Data
public class CloseAccountCommand extends BaseCommand {
    public CloseAccountCommand(String id) {
        super(id);
    }
}

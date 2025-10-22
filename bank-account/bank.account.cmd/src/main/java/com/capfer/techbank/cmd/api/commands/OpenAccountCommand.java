package com.capfer.techbank.cmd.api.commands;

import com.capfer.techbank.common.dto.AccountType;
import com.capfer.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}

package com.capfer.techbank.cmd.api.commands;

import com.capfer.techbank.cqrs.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawFundsCommand extends BaseCommand {

    private double amount;
}

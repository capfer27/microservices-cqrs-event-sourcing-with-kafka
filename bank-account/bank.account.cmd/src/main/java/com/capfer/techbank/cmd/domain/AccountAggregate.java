package com.capfer.techbank.cmd.domain;

import com.capfer.techbank.cmd.api.commands.OpenAccountCommand;
import com.capfer.techbank.common.events.AccountClosedEvent;
import com.capfer.techbank.common.events.AccountOpenedEvent;
import com.capfer.techbank.common.events.FundsDepositedEvent;
import com.capfer.techbank.common.events.FundsWithdrawnEvent;
import com.capfer.techbank.cqrs.core.domain.AggregateRoot;
import com.capfer.techbank.cqrs.core.messages.Message;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    
    private Boolean active;
    private double balance;

    public AccountAggregate(OpenAccountCommand command) {
        AccountOpenedEvent openedEvent = createAccountOpenedEvent(command);
        raiseEvent(openedEvent);
    }

    private static AccountOpenedEvent createAccountOpenedEvent(OpenAccountCommand command) {
        AccountOpenedEvent openedEvent = AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .createdDate(new Date())
                .openingBalance(command.getOpeningBalance())
                .build();
        return openedEvent;
    }
    
    public void apply(AccountOpenedEvent openedEvent) {
        this.id = openedEvent.getId();
        this.active = true;
        this.balance = openedEvent.getOpeningBalance();
    }
    
    public void depositFunds(double amount) {
        if (!this.active) {
            throw new IllegalArgumentException("Funds cannot be deposited into a closed account.");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("The deposit amount must be greater that 0!");
        }

        var fundsDepositedEvent = FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();
        
        raiseEvent(fundsDepositedEvent);
    }
    
    public void apply(FundsDepositedEvent fundsDepositedEvent) {
        this.id = fundsDepositedEvent.getId();
        this.balance += fundsDepositedEvent.getAmount();
    }
    
    public void withdrawFunds(double amount) {
        if (!this.active) {
            throw new IllegalArgumentException("Funds cannot be withdrawn from a closed account.");
        }

        var fundsWithdrawnEvent = FundsWithdrawnEvent.builder()
                .id(this.id)
                .amount(amount)
                .build();

        raiseEvent(fundsWithdrawnEvent);
    }

    public void apply(FundsWithdrawnEvent fundsWithdrawnEvent) {
        this.id = fundsWithdrawnEvent.getId();
        this.balance -= fundsWithdrawnEvent.getAmount();
    }

    public void closeAccount() {
        if (!this.active) {
            throw new IllegalArgumentException("The bank account has already been closed!");
        }

        AccountClosedEvent closedEvent = AccountClosedEvent.builder()
                .id(this.id)
                .build();

        raiseEvent(closedEvent);
    }

    public void apply(AccountClosedEvent accountClosedEvent) {
        this.id = accountClosedEvent.getId();
        this.active = false;
    }
}

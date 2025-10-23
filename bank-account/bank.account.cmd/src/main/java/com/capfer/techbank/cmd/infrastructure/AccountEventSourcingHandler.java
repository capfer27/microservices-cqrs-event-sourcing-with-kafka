package com.capfer.techbank.cmd.infrastructure;

import com.capfer.techbank.cmd.domain.AccountAggregate;
import com.capfer.techbank.cqrs.core.domain.AggregateRoot;
import com.capfer.techbank.cqrs.core.events.BaseEvent;
import com.capfer.techbank.cqrs.core.handlers.EventSourcingHandler;
import com.capfer.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

@Service
public class AccountEventSourcingHandler implements EventSourcingHandler<AccountAggregate> {

    private final EventStore eventStore;

    public AccountEventSourcingHandler(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void save(AggregateRoot aggregate) {
        eventStore.saveEvents(aggregate.getId(), aggregate.getUncommittedChanges(), aggregate.getVersion());
        aggregate.markChangesAsCommitted();
    }

    @Override
    public AccountAggregate getById(String id) {
        // Recreate the latest state of the aggregate
        AccountAggregate accountAggregate = new AccountAggregate();
        List<BaseEvent> events = eventStore.getEvents(id);
        if (CollectionUtils.isEmpty(events)) {
            return null;
        }
        accountAggregate.replayEvents(events);
        var latestVersion = events.stream()
                .map(BaseEvent::getVersion)
                .max(Comparator.naturalOrder());

        latestVersion.ifPresent(accountAggregate::setVersion);
        return accountAggregate;
    }
}

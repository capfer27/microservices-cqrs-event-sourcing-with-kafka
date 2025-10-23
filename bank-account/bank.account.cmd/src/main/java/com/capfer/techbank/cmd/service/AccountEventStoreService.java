package com.capfer.techbank.cmd.service;

import com.capfer.techbank.cmd.domain.AccountAggregate;
import com.capfer.techbank.cmd.domain.EventStoreRepository;
import com.capfer.techbank.cqrs.core.events.BaseEvent;
import com.capfer.techbank.cqrs.core.events.EventModel;
import com.capfer.techbank.cqrs.core.exception.AggregateNotFoundException;
import com.capfer.techbank.cqrs.core.exception.ConcurrencyException;
import com.capfer.techbank.cqrs.core.infrastructure.EventStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
public class AccountEventStoreService implements EventStore {

    private final EventStoreRepository eventStoreRepository;

    public AccountEventStoreService(EventStoreRepository eventStoreRepository) {
        this.eventStoreRepository = eventStoreRepository;
    }

    @Override
    public void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion) {
        List<EventModel> eventStreamModels = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        // Implements optimistic concurrency checking if the aggregate is new
        EventModel model = eventStreamModels.getLast();
        if (expectedVersion != -1 && model.getVersion() != expectedVersion) {
            throw new ConcurrencyException();
        }
        int version = expectedVersion;
        for (BaseEvent event : events) {
            version++;
            event.setVersion(version);
            EventModel newEventModel = createEventModel(aggregateId, event, version);

            EventModel persistedEvent = eventStoreRepository.save(newEventModel);
            if (persistedEvent != null) {
                // TODO: produce event to kafka
            }
        }

    }

    private static EventModel createEventModel(String aggregateId, BaseEvent event, int version) {
        EventModel eventModel = EventModel.builder()
                .timestamp(new Date())
                .eventData(event)
                .aggregateIdentifier(aggregateId)
                .aggregateType(AccountAggregate.class.getTypeName())
                .version(version)
                .eventType(event.getClass().getTypeName())
                .eventData(event)
                .build();

        return eventModel;
    }

    /**
     *
     * @param aggregateId is the same as account id
     * @return List<BaseEvent>
     */
    @Override
    public List<BaseEvent> getEvents(String aggregateId) {
        List<EventModel> eventStreamModels = eventStoreRepository.findByAggregateIdentifier(aggregateId);
        if (CollectionUtils.isEmpty(eventStreamModels)) {
            throw new AggregateNotFoundException("Incorrect account ID provided");
        }
        List<BaseEvent> events = eventStreamModels.stream()
                .map(EventModel::getEventData)
                .toList();

        return events;
    }
}

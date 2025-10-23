package com.capfer.techbank.cqrs.core.infrastructure;

import com.capfer.techbank.cqrs.core.events.BaseEvent;

import java.util.List;

/**
 * Provides an abstraction for accessing the event store business logic.
 */
public interface EventStore {
    void saveEvents(String aggregateId, Iterable<BaseEvent> events, int expectedVersion);
    List<BaseEvent> getEvents(String aggregateId);
}

package com.capfer.techbank.cqrs.core.handlers;

import com.capfer.techbank.cqrs.core.domain.AggregateRoot;

public interface EventSourcingHandler<T> {

    void save(AggregateRoot aggregate);
    T getById(String id);
}

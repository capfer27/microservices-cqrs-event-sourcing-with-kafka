package com.capfer.techbank.cmd.domain;

import com.capfer.techbank.cqrs.core.events.EventModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventStoreRepository extends MongoRepository<EventModel, String> {

    List<EventModel> findByAggregateIdentifier(String aggregateIdentifier);

    @Override
    @Nullable
    <S extends EventModel> S save(S entity);
}

package ch.zuehlke.hatch.sailingserver.data;

import ch.zuehlke.hatch.sailingserver.domain.Position;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static ch.zuehlke.hatch.sailingserver.config.ApplicationConfig.COLLECTION_NAME_EVENTS;

@Repository
public class PositionRepository {

    private final MongoDatabase database;

    @Autowired
    public PositionRepository(MongoDatabase database) {
        this.database = database;
    }

    public Flux<Position> getPositions(LocalDateTime from, LocalDateTime to) {
        return Flux.from(
                this.database.getCollection(COLLECTION_NAME_EVENTS)
                        .find()
        ).map(this::toPosition);
    }

    private Position toPosition(Document document) {

        return null;
    }
}

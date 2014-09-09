package com.johannesbrodwall.events.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.TestDatabase;

public class EventRepositoryTest {

    private Database database = TestDatabase.getInstance();

    private EventRepository repository = new EventRepository();

    @Test
    public void shouldSaveEvents() {
        Event event = SampleEventData.sampleEvent();

        long id = database.executeInTransaction(() -> repository.insert(event));
        assertThat(database.executeInTransaction(() -> repository.fetch(id)))
            .isEqualTo(event);
    }

}

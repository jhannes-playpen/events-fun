package com.johannesbrodwall.events.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.events.category.CategoryRepository;
import com.johannesbrodwall.events.category.Category;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.TestDatabase;

public class EventRepositoryTest {

    private Database database = TestDatabase.getInstance();

    private EventRepository repository = new EventRepository();

    private Category category1 = SampleEventData.sampleCategory();
    private Category category2 = SampleEventData.sampleCategory();

    @Before
    public void storeBaseData() {
        CategoryRepository categoryRepo = new CategoryRepository();
        database.executeInTransaction(() -> {
            categoryRepo.insert(category1);
            categoryRepo.insert(category2);
        });
    }


    @Test
    public void shouldSaveEvents() {
        Event event = SampleEventData.sampleEvent(category1);
        database.executeInTransaction(() -> repository.insert(event));
        assertThat(database.executeInTransaction(() -> repository.fetch(event.getId())))
            .isEqualTo(event);

        assertThat(database.executeInTransaction(() -> repository.findAll()))
            .contains(event);

    }

}

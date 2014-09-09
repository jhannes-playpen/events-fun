package com.johannesbrodwall.events.category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.NotFoundException;
import com.johannesbrodwall.infrastructure.db.TestDatabase;
import com.johannesbrodwall.infrastructure.db.Transaction;

public class CategoryRepositoryTest {

    private Database database = TestDatabase.getInstance();
    private CategoryRepository repository = new CategoryRepository();

    @Test
    public void shouldRetrieveCategory() {
        EventCategory category = SampleEventData.sampleCategory();

        long id;
        try (Transaction tx = database.transaction()) {
            id = repository.insert(category);
            tx.setCommit();
        }

        try (Transaction tx = database.transaction()) {
            assertThat(repository.fetch(id)).isEqualTo(category);
        }
    }

    @Test
    public void shouldNotWriteOnRollback() {
        EventCategory category = SampleEventData.sampleCategory();

        long id;
        try (Transaction tx = database.transaction()) {
            id = repository.insert(category);
        }

        try (Transaction tx = database.transaction()) {
            repository.fetch(id);
            fail("Expected rolled back transaction not to be present");
        } catch (NotFoundException expected) {

        }

    }

    @Test
    public void shouldFindCategories() throws Exception {
        EventCategory category = SampleEventData.sampleCategory();

        try (Transaction tx = database.transaction()) {
            repository.insert(category);
            tx.setCommit();
        }

        try (Transaction tx = database.transaction()) {
            assertThat(repository.findAll())
                .contains(EventCategory.fromJSON(category.toJSON()));
        }
    }

}

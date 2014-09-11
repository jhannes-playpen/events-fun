package com.johannesbrodwall.events.project;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.johannesbrodwall.events.SampleEventData;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.TestDatabase;

import java.util.List;

public class ProjectRepositoryTest {

    private Database database = TestDatabase.getInstance();

    private ProjectRepository repository = new ProjectRepository();

    @Test
    public void shouldStoreProject() {
        Project project = SampleEventData.sampleProject();

        database.executeInTransaction(() -> repository.insert(project));
        database.executeInTransaction(() -> {
            assertThat(repository.findAll()).contains(project);
        });

        assertThat(database.executeInTransaction(() -> repository.fetch(project.getId())))
                .isEqualTo(project);
    }

    @Test
    public void shouldStoreProjectHierarchy() {
        Project parent = SampleEventData.sampleProject();

        database.executeInTransaction(() -> repository.insert(parent));

        Project child1 = SampleEventData.sampleProject(parent);
        Project child2 = SampleEventData.sampleProject(parent);

        database.executeInTransaction(() -> {
            repository.insert(child1);
            repository.insert(child2);
        });

        assertThat(database.executeInTransaction(() -> repository.fetch(parent.getId()))
            .getChildren()).extracting("id").contains(child1.getId(), child2.getId());

        List<Project> projects = database.executeInTransaction(() -> repository.findAll());

        assertThat(projects.stream().filter((p) -> p.equals(child1)).findFirst().get().getParent())
            .isEqualTo(parent);

        assertThat(projects.stream().filter((p) -> p.equals(parent)).findFirst().get().getChildren())
            .extracting("id").contains(child1.getId(), child2.getId());
    }

}

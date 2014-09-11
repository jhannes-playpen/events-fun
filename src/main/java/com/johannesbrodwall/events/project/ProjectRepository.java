package com.johannesbrodwall.events.project;

import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectRepository implements Repository<Project> {

    @Override
    public List<Project> findAll() {
        List<Project> projects = Database.executeQueryForAll("select * from projects", this::toEntity);
        Map<Integer, Project> projectMap = new HashMap<>();
        projects.forEach((p) -> projectMap.put(p.getId(), p));
        projects.forEach((p) -> fetchParentProject(projectMap, p));
        return projects;
    }

    private void fetchParentProject(Map<Integer, Project> projectMap, Project project) {
        if (project.getParentId() == null) return;
        Project parent = projectMap.get(project.getParentId());
        project.setParent(parent);
        parent.getChildren().add(project);
    }

    private Project toEntity(ResultSet rs) throws SQLException {
        Project project = new Project(rs.getString("displayName"));
        project.setId(rs.getInt("id"));
        project.setParentId(rs.getInt("parent_id"));
        return project;
    }

    @Override
    public void insert(Project entity) {
        Database.executeInsert("insert into projects (displayName, parent_id) values (?,?)", (stmt) -> {
           stmt.setString(1, entity.getDisplayName());
           stmt.setObject(2, entity.getParentId());
        });
        entity.setId(Database.queryForInt("select last_value from projects_id_seq"));
    }

    @Override
    public Project fetch(Integer id) {
        Project project = Database.executeFetchByKey("select * from projects where id = ?", id, this::toEntity);
        project.setChildren(Database.executeQuery("select * from projects where parent_id = ?",
                (stmt) -> stmt.setInt(1, id),
                this::toEntity));
        return project;
    }

}

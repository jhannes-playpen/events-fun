package com.johannesbrodwall.events.event;

import com.johannesbrodwall.events.category.CategoryRepository;
import com.johannesbrodwall.events.category.Category;
import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventRepository implements Repository<Event> {

    private CategoryRepository categoryRepository = new CategoryRepository();

    @Override
    public List<Event> findAll() {
        Map<Integer, Category> categories = findAllCategories();
        return Database.executeQueryForAll("select * from events", (rs) -> createEvent(rs, categories));
    }

    @Override
    public void insert(Event event) {
        String query = "insert into events (displayName, category_id, start_date, end_date) values (?, ?, ?, ?)";
        Database.executeInsert(query, (PreparedStatement stmt) -> {
            stmt.setString(1, event.getDisplayName());
            stmt.setLong(2, event.getCategoryId());
            stmt.setDate(3, Date.valueOf(event.getStartDate()));
            stmt.setDate(4, Date.valueOf(event.getEndDate()));
        });
        event.setId(Database.queryForInt("SELECT last_value FROM events_id_seq"));
    }

    @Override
    public Event fetch(Integer id) {
        Map<Integer, Category> categories = findAllCategories();

        String query = "select * from events where id = ?";
        return Database.executePreparedQuery(query, (PreparedStatement stmt) -> {
            stmt.setLong(1, id);
        }, (ResultSet rs) -> createEvent(rs, categories));
    }

    private Event createEvent(ResultSet rs, Map<Integer, Category> categories) throws SQLException {
        Event event = new Event(rs.getString("displayName"), categories.get(rs.getInt("category_id")));
        event.setId(rs.getInt("id"));
        event.setStartDate(rs.getDate("start_date").toLocalDate());
        event.setEndDate(rs.getDate("end_date").toLocalDate());
        return event;
    }

    private Map<Integer, Category> findAllCategories() {
        Map<Integer, Category> categories = new HashMap<>();
        categoryRepository.findAll().stream().forEach((c) -> categories.put(c.getId(), c));
        return categories;
    }

}

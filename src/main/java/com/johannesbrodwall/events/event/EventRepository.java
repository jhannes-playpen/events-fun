package com.johannesbrodwall.events.event;

import com.johannesbrodwall.infrastructure.db.Database;
import com.johannesbrodwall.infrastructure.db.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class EventRepository implements Repository<Event> {

    @Override
    public List<Event> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long insert(Event event) {
        String query = "insert into events (displayName) values (?)";
        Database.executeInsert(query, (PreparedStatement stmt) -> {
            stmt.setString(1, event.getDisplayName());
        });
        return Database.queryForLong("SELECT last_value FROM events_id_seq");
    }

    @Override
    public Event fetch(long id) {
        String query = "select * from events where id = ?";
        return Database.executePreparedQuery(query,
                (PreparedStatement stmt) -> {
                    stmt.setLong(1, id);
                },
                (ResultSet rs) -> {
                    return new Event(rs.getString("displayName"));
                });
    }

}

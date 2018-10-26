package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.hevs.aislab.demo.model.Account;
import ch.hevs.aislab.demo.model.Computer;

public class ComputerEntity implements Computer {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String label;
    private int type;
    private String description;
    private int	room_id;

    private Map<Integer, String> types_map = new HashMap<Integer, String>() {{
        put(1, "Notebook");
        put(2, "Desktop");
        put(3, "All in one");
    }};

    public ComputerEntity() {
    }

    public ComputerEntity(Computer computer) {
        id = computer.getId();
        label = computer.getLabel();
        type = computer.getType();
        description = computer.getDescription();
        room_id = computer.getRoomId();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public int getType() {
        return type;
    }
    public String getTypeString() {
        return types_map.get(type);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getRoomId() {
        return room_id;
    }
}

package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;
import ch.hevs.aislab.demo.model.Computer;

@Entity(tableName = "computers",
        foreignKeys =
        @ForeignKey(
                entity = RoomEntity.class,
                parentColumns = "id",
                childColumns = "roomId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(
                        value = {"roomId"}
                )}
)

public class ComputerEntity implements Computer {

    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String label;
    private int type;
    private String description;
    private Long roomId;

    public ComputerEntity(String label, int type, String description, long roomId) {
        this.label = label;
        this.type = type;
        this.description = description;
        this.roomId = roomId;
    }

    public ComputerEntity(Computer computer) {
        id = computer.getId();
        label = computer.getLabel();
        type = computer.getType();
        description = computer.getDescription();
        roomId = computer.getRoomId();
    }

    @Override
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) { this.label = label; }


    @Override
    public int getType() {
        return type;
    }
    public String getTypeString() {
        Map<Integer, String> types_map = new HashMap<Integer, String>() {{
            put(1, "Notebook");
            put(2, "Desktop");
            put(3, "All in one");
        }};

        return types_map.get(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId){ this.roomId = roomId;}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ComputerEntity)) return false;
        ComputerEntity o = (ComputerEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return label;
    }
}

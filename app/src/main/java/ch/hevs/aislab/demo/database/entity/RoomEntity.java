package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ch.hevs.aislab.demo.model.Room;

@Entity(tableName = "rooms")
public class RoomEntity implements Room {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String label;
    private int nbOfPlaces;

    public RoomEntity() { }

    public RoomEntity(Room room) {
        id = room.getId();
        label = room.getLabel();
        nbOfPlaces = room.getNbOfPlaces();
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
    public int getNbOfPlaces() {
        return nbOfPlaces;
    }
    public void setNbOfPlaces(int nbOfPlace) {
        this.nbOfPlaces = nbOfPlace;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof RoomEntity)) return false;
        RoomEntity o = (RoomEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return label;
    }
}

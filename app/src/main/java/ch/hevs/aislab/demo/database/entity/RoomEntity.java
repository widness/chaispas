package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ch.hevs.aislab.demo.model.Room;

@Entity(tableName = "rooms")
public class RoomEntity implements Room {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String label;
    private int nbOfPlace;

    public RoomEntity() { }

    public RoomEntity(Room room) {
        id = room.getId();
        label = room.getLabel();
        nbOfPlace = room.getNbOfPlaces();
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
        return nbOfPlace;
    }
    public void setNbOfPlaces(int nbOfPlace) {
        this.nbOfPlace = nbOfPlace;
    }
}

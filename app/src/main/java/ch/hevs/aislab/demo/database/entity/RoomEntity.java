package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

import ch.hevs.aislab.demo.model.Room;
import ch.hevs.aislab.demo.model.Student;

@Entity(tableName = "rooms")
public class RoomEntity implements Room {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String label;
    private int nbOfPlaces;


    @Ignore
    private Long nbStudents;
    private Long nbComputers;

    public RoomEntity(String label, int nbOfPlaces) {
        this.label = label;
        this.nbOfPlaces = nbOfPlaces;
    }

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

    public Long getNbStudents() {
        return nbStudents;
    }
    public void setNbStudents(Long nbStudents) {
        this.nbStudents = nbStudents;
    }

    public Long getNbComputers() {
        return nbComputers;
    }
    public void setNbComputers(Long nbComputers) {
        this.nbComputers = nbComputers;
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

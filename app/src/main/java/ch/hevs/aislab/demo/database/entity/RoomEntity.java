package ch.hevs.aislab.demo.database.entity;

import ch.hevs.aislab.demo.model.Room;

public class RoomEntity implements Room {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public int getNbOfPlaces() {
        return 0;
    }
}

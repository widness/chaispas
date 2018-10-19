package ch.hevs.aislab.demo.database.entity;

import ch.hevs.aislab.demo.model.Computer;

public class ComputerEntity implements Computer {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public int getRoomId() {
        return 0;
    }
}

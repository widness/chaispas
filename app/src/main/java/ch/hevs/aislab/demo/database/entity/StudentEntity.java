package ch.hevs.aislab.demo.database.entity;

import java.util.Date;

import ch.hevs.aislab.demo.model.Student;

public class StudentEntity implements Student {

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getFirstname() {
        return null;
    }

    @Override
    public String getLastname() {
        return null;
    }

    @Override
    public Date getBirthday() {
        return null;
    }

    @Override
    public int getRoomId() {
        return 0;
    }
}

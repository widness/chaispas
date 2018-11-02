package ch.hevs.aislab.demo.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import ch.hevs.aislab.demo.model.Room;
import ch.hevs.aislab.demo.model.Student;

@Entity(tableName = "students",
        foreignKeys =
        @ForeignKey(
                entity = RoomEntity.class,
                parentColumns = "id",
                childColumns = "student_id",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index(
                        value = {"student_id"}
                )}
)

public class StudentEntity implements Student {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private Long roomId;

    public StudentEntity() { }

    public StudentEntity(Student student) {
        id = student.getId();
        firstName = student.getFirstName();
        lastName = student.getLastName();
        birthDay = student.getBirthday();
        roomId = this.getRoomId();
    }

    // TODO: fill return
    @Override
    public Long getId() {
        return roomId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public Date getBirthday() {
        return birthDay;
    }

    @Override
    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof StudentEntity)) return false;
        StudentEntity o = (StudentEntity) obj;
        return o.getId().equals(this.getId());
    }

}

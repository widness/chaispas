package ch.hevs.aislab.demo.database;

import java.util.ArrayList;
import java.util.List;
import ch.hevs.aislab.demo.database.entity.ComputerEntity;
import ch.hevs.aislab.demo.database.entity.RoomEntity;
import ch.hevs.aislab.demo.database.entity.StudentEntity;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static List<RoomEntity> generateRooms() {
        List<RoomEntity> rooms = new ArrayList<>();

        RoomEntity room1 = new RoomEntity("100", 35);

        RoomEntity room2 = new RoomEntity("101", 20);

        RoomEntity room3 = new RoomEntity("200", 15);

        RoomEntity room4 = new RoomEntity("201", 22);

        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);
        rooms.add(room4);

        return rooms;
    }

    public static List<ComputerEntity> generateComputers() {
        List<ComputerEntity> computers = new ArrayList<>();

        ComputerEntity computer1 = new ComputerEntity("Computer de la mord", 1, "Magnifique, juste magnifique", 2);

        ComputerEntity computer2 = new ComputerEntity("Pc nul", 1, "nul, juste nul", 2);

        computers.add(computer1);
        computers.add(computer2);

        return computers;
    }

    public static List<StudentEntity> generateStudents() {
        List<StudentEntity> students = new ArrayList<>();

        StudentEntity student1 = new StudentEntity("Joan", "Doe", 1);
        StudentEntity student2 = new StudentEntity("Joan le second", "Doe", 1);

        students.add(student1);
        students.add(student2);

        return students;
    }
}

package kasia.mimuwmap;

import java.io.Serializable;

public class Professor implements Serializable {
    private String firstName;
    private String lastName;
    private String title;
    private int room;

    public Professor(String firstName, String lastName, String title, int room) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.room = room;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getFullName() {
        return title + " " + this;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

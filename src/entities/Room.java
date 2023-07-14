package entities;

import java.io.Serializable;

public class Room implements Serializable {
    private int id;
    private String type;
    private boolean isBooked;

    public Room(int id, String type, boolean isBooked) {
        this.id = id;
        this.type = type;
        this.isBooked = isBooked;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}

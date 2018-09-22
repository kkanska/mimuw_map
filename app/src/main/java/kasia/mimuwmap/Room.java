package kasia.mimuwmap;

import java.io.Serializable;

public class Room implements Serializable {
    private int number;
    private int floor;
    private float left;
    private float top;
    private float width;
    private float height;

    public Room(int number, int floor, float left, float top, float width, float height) {
        this.number = number;
        this.floor = floor;
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    public int getNumber() {
        return number;
    }

    public int getFloor() {
        return floor;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}

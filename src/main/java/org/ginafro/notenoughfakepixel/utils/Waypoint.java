package org.ginafro.notenoughfakepixel.utils;

public class Waypoint {
    private final String type;
    private final int[] coordinates;
    private boolean hidden = false;

    public Waypoint(String type, int[] coordinates) {
        this.type = type;
        this.coordinates = coordinates;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }


    public String getType() {
        return type;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    @Override
    public String toString() {
        return String.format("Waypoint{type='%s', coordinates=(%d, %d, %d)}",
                type, coordinates[0], coordinates[1], coordinates[2]
        );
    }
}
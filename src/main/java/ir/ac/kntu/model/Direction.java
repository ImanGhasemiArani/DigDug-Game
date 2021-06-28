package ir.ac.kntu.model;

public enum Direction {
    UP,DOWN,LEFT,RIGHT, NONE;


    public Direction valueOfCustomize(String s) {
        try {
            return valueOf(s);
        } catch (IllegalArgumentException e) {
            return NONE;
        }
    }
}

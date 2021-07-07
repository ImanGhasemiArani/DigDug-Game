package ir.ac.kntu.gameobjects;

import ir.ac.kntu.model.Direction;

public interface MovingGameObject {

    void move(Direction direction);

    void die();

    void remove();

}

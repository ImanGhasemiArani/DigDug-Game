package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Bullet extends Parent implements MovingGameObject {

    private ImageView bullet;
    private int x;
    private int y;
    private int range;
    private int counter;

    public Bullet(Direction direction, int range, int startX, int startY) {
        counter = 0;
        this.range = range;
        x = startX;
        y = startY;
        bullet = new ImageView();
        selectImage(direction);
        bullet.setOpacity(0);
        getChildren().add(bullet);
        GameAriaBuilder.getGameMap().getChildren().add(this);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80),e->{
            counter++;
            bullet.setOpacity(bullet.getOpacity() + 0.3);
            move(direction);
            if (counter == range) {
                remove();
                GameData.shootControlOn();
            }
        }));
        timeline.setCycleCount(range);
        timeline.play();
    }

    private void selectImage(Direction direction) {
        switch (direction) {
            case LEFT:
                bullet.setImage(new Image("assets/bulletLeft.png"));
                bullet.setFitWidth(GameData.GAP);
                bullet.setFitHeight(GameData.GAP/4);
                bullet.setX(x);
                bullet.setY(y + 17.5);
                break;
            case DOWN:
                bullet.setImage(new Image("assets/bulletDown.png"));
                bullet.setFitWidth(GameData.GAP/4);
                bullet.setFitHeight(GameData.GAP);
                bullet.setX(x + 17.5);
                bullet.setY(y);
                break;
            case UP:
                bullet.setImage(new Image("assets/bulletUp.png"));
                bullet.setFitWidth(GameData.GAP/4);
                bullet.setFitHeight(GameData.GAP);
                bullet.setX(x + 17.5);
                bullet.setY(y);
                break;
            default:
                bullet.setImage(new Image("assets/bulletRight.png"));
                bullet.setFitWidth(GameData.GAP);
                bullet.setFitHeight(GameData.GAP/4);
                bullet.setX(x);
                bullet.setY(y + 17.5);
                break;
        }
    }

    @Override
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            default:
                break;
        }
    }

    private void moveUp() {
        if ( y - GameData.GAP >= 0 &&
                GameData.MAP_DATA[ y/GameData.GAP -1 ][ x/GameData.GAP ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP -1 ][ x/GameData.GAP ] != GameData.STONE) {
            setY(y - GameData.GAP);
        }
    }


    private void moveDown() {
        if ( y + GameData.GAP <= GameData.END_Y_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] != GameData.STONE) {
            setY(y + GameData.GAP);
        }
    }

    private void moveLeft() {
        if ( x - GameData.GAP >= 0 &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] != GameData.STONE) {
            setX(x - GameData.GAP);
        }
    }

    private void moveRight() {
        if ( x + GameData.GAP <= GameData.END_X_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] != GameData.STONE) {
            setX(x + GameData.GAP);
        }
    }

    @Override
    public void stopMove() {

    }

    @Override
    public void remove() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80),e->{
            GameAriaBuilder.getGameMap().getChildren().remove(this);
        }));
        timeline.play();
    }

    public void setX(int x) {
        this.x = x;
        bullet.setX(x);
    }

    public void setY(int y) {
        this.y = y;
        bullet.setY(y);
    }
}

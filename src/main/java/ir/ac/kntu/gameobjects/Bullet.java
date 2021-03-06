package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.Direction;
import ir.ac.kntu.picture.PictureBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Bullet extends Parent implements MovingGameObject {

    private final ImageView bullet;
    private int x;
    private int y;
    private int counter;
    private boolean temp;

    public Bullet(Direction direction, int range, int startX, int startY) {
        counter = 0;
        x = startX;
        y = startY;
        bullet = new ImageView();
        selectImage(direction);
        bullet.setOpacity(0);
        getChildren().add(bullet);
        GameAriaBuilder.getGameMap().getChildren().add(this);
        temp = true;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80),e->{
            counter++;
            bullet.setOpacity(bullet.getOpacity() + 0.3);
            if (counter == range+1 || !temp) {
                remove();
                GameData.shootControlOn();
            }else {
                move(direction);
            }
        }));
        timeline.setCycleCount(range+1);
        timeline.play();
    }

    private void selectImage(Direction direction) {
        switch (direction) {
            case LEFT:
                bullet.setImage(PictureBuilder.BULLET_LEFT_IMAGE);
                bullet.setFitWidth(GameData.GAP);
                bullet.setFitHeight(GameData.GAP/4);
                bullet.setX(x);
                bullet.setY(y + 17.5);
                break;
            case DOWN:
                bullet.setImage(PictureBuilder.BULLET_DOWN_IMAGE);
                bullet.setFitWidth(GameData.GAP/4);
                bullet.setFitHeight(GameData.GAP);
                bullet.setX(x + 17.5);
                bullet.setY(y);
                break;
            case UP:
                bullet.setImage(PictureBuilder.BULLET_UP_IMAGE);
                bullet.setFitWidth(GameData.GAP/4);
                bullet.setFitHeight(GameData.GAP);
                bullet.setX(x + 17.5);
                bullet.setY(y);
                break;
            default:
                bullet.setImage(PictureBuilder.BULLET_RIGHT_IMAGE);
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

            if (    GameData.ENEMIES[y/GameData.GAP -1][x/GameData.GAP] != null &&
                    (GameData.MAP_DATA[ y/GameData.GAP -1 ][ x/GameData.GAP ] == GameData.ENEMY_SIMPLE ||
                    GameData.MAP_DATA[ y/GameData.GAP -1 ][ x/GameData.GAP ] == GameData.ENEMY_DEADLY)) {
                GameData.ENEMIES[y/GameData.GAP -1][x/GameData.GAP].inflating();
                temp = false;
            }

            setY(y - GameData.GAP);
        }
    }


    private void moveDown() {
        if ( y + GameData.GAP <= GameData.END_Y_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] != GameData.STONE) {

            if (    GameData.ENEMIES[y/GameData.GAP +1][x/GameData.GAP] != null &&
                    (GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] == GameData.ENEMY_SIMPLE ||
                    GameData.MAP_DATA[ y/GameData.GAP +1 ][ x/GameData.GAP ] == GameData.ENEMY_DEADLY)) {
                GameData.ENEMIES[y/GameData.GAP +1][x/GameData.GAP].inflating();
                temp = false;
            }

            setY(y + GameData.GAP);
        }
    }

    private void moveLeft() {
        if ( x - GameData.GAP >= 0 &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] != GameData.STONE) {

            if (    GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] == GameData.ENEMY_SIMPLE ||
                    GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP -1 ] == GameData.ENEMY_DEADLY) {
                GameData.ENEMIES[ y/GameData.GAP ][ x/GameData.GAP -1 ].inflating();
                temp = false;
            }

            setX(x - GameData.GAP);
        }
    }

    private void moveRight() {
        if ( x + GameData.GAP <= GameData.END_X_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] != GameData.BLOCK &&
                GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] != GameData.STONE) {

            if (    GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] == GameData.ENEMY_SIMPLE ||
                    GameData.MAP_DATA[ y/GameData.GAP ][ x/GameData.GAP +1 ] == GameData.ENEMY_DEADLY) {
                GameData.ENEMIES[ y/GameData.GAP ][ x/GameData.GAP +1 ].inflating();
                temp = false;
            }

            setX(x + GameData.GAP);
        }
    }

    @Override
    public void die() {

    }

    @Override
    public void remove() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(80),e-> GameAriaBuilder.getGameMap().getChildren().remove(this)));
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

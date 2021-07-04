package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


public class Stone extends Parent implements NotMovingGameObject {

    private final ImageView stone;
    private final int x;
    private int y;
    private boolean firstMove;


    public Stone(int xx, int yy) {
        x = xx;
        y = yy;
        stone = new ImageView(new Image("assets/stone.png"));
        stone.setFitHeight(GameData.GAP);
        stone.setFitWidth(GameData.GAP + 10);
        appear();
        firstMove = true;
        Timeline moveAnimation = new Timeline(new KeyFrame(Duration.millis(300),e-> changeImage()));
        moveAnimation.setCycleCount(5);
        Timeline checkMove = new Timeline(new KeyFrame(Duration.millis(500),e-> {
            GameData.MAP_DATA[y][x] = GameData.STONE;
            if (y+1 < GameData.SIZE_OF_GAME_ACTION_ARIA && GameData.MAP_DATA[y+1][x] != GameData.BLOCK) {
                moveAnimation.play();
            }
        }));
        checkMove.setCycleCount(Timeline.INDEFINITE);
        checkMove.play();
    }

    private void changeImage() {
        if (stone.getRotate() == 0 ) {
            stone.setRotate(-10);
        }else if (stone.getRotate() == -10) {
            stone.setRotate(10);
        } else if (stone.getRotate() == 10) {
            stone.setRotate(-9);
        } else if (stone.getRotate() == -9) {
            stone.setRotate(9);
        } else {
            stone.setRotate(0);
            if (firstMove) {
                GameData.MAP_DATA[y][x] = GameData.BLOCK;
                firstMove = false;
            } else {
                GameData.MAP_DATA[y][x] = GameData.EMPTY_BLOCK;
            }
            y += 1;
            if (GameData.MAP_DATA[y][x] == GameData.PLAYER_CHARACTER) {
                GameAriaBuilder.getPlayerCharacter().die();
            }
            GameData.MAP_DATA[y][x] = GameData.STONE;
            stone.setY(GameData.calculateRealXY(y));
        }
    }

    @Override
    public void appear() {
        stone.setX(GameData.calculateRealXY(x)-5);
        stone.setY(GameData.calculateRealXY(y));
        getChildren().add(stone);
    }

    @Override
    public void destroy() {
        // this object is not removable
    }

}

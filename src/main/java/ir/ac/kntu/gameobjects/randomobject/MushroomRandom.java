package ir.ac.kntu.gameobjects.randomobject;

import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.NotMovingGameObject;
import ir.ac.kntu.picture.PictureBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class MushroomRandom extends Parent implements NotMovingGameObject, RandomObject {

    private final ImageView mushroom;
    private int x;
    private int y;
    private Timeline timer = new Timeline(new KeyFrame(Duration.seconds(10),e->{
        destroy();
    }));

    public MushroomRandom(int xx, int yy) {
        x = xx;
        y = yy;
        mushroom = new ImageView(PictureBuilder.MUSHROOM);
        mushroom.setOpacity(0);
        appear();
    }

    @Override
    public void appear() {
        mushroom.setX(GameData.calculateRealXY(x)+5);
        mushroom.setY(GameData.calculateRealXY(y)+5);
        getChildren().add(mushroom);
        GameData.MAP_DATA[y][x] = GameData.MUSHROOM;
        GameData.NOT_MOVING_GAME_OBJECTS[x][y] = this;
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            mushroom.setOpacity(mushroom.getOpacity() + 0.1);
        }));
        t1.setCycleCount(10);
        t1.play();
        timer.play();
    }


    @Override
    public void destroy() {
        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            mushroom.setOpacity(mushroom.getOpacity() - 0.1);
            if (mushroom.getOpacity() <= 0.1) {
                remove();
            }
        }));
        t2.setCycleCount(10);
        t2.play();
    }

    @Override
    public void remove() {
        GameData.MAP_DATA[y][x] = GameData.EMPTY_BLOCK;
        GameData.NOT_MOVING_GAME_OBJECTS[x][y] = null;
        new Timeline(new KeyFrame(Duration.ONE, e-> GameAriaBuilder.getGameMap().getChildren().remove(this))).play();
    }

    @Override
    public void use() {
        AudioBuilder.playUseRandomAudio();
        GameData.increaseScore(30);
        remove();
        GameAriaBuilder.getPlayerCharacter().increaseSpeed();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10),e->{
            GameAriaBuilder.getPlayerCharacter().decreaseSpeed();
        }));
        timeline.play();
    }
}

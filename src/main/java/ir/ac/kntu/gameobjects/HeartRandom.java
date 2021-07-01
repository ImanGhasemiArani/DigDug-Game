package ir.ac.kntu.gameobjects;

import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class HeartRandom extends Parent implements NotMovingGameObject,RandomObject{

    private final ImageView heart;
    private int x;
    private int y;
    private Timeline timer = new Timeline(new KeyFrame(Duration.seconds(10),e->{
        destroy();
    }));

    public HeartRandom(int xx, int yy) {
        x = xx;
        y = yy;
        heart = new ImageView(new Image("assets/heart.png"));
        heart.setOpacity(0);
        appear();
    }

    @Override
    public void appear() {
        heart.setX(GameData.calculateRealXY(x)+5);
        heart.setY(GameData.calculateRealXY(y)+5);
        getChildren().add(heart);
        GameData.MAP_DATA[y][x] = GameData.HEART;
        GameData.NOT_MOVING_GAME_OBJECTS[x][y] = this;
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            heart.setOpacity(heart.getOpacity() + 0.1);
        }));
        t1.setCycleCount(10);
        t1.play();
        timer.play();
    }


    @Override
    public void destroy() {
        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            heart.setOpacity(heart.getOpacity() - 0.1);
            if (heart.getOpacity() <= 0.1) {
                remove();
            }
        }));
        t2.setCycleCount(10);
        t2.play();
    }

    private void remove() {
        GameData.MAP_DATA[y][x] = GameData.EMPTY_BLOCK;
        GameData.NOT_MOVING_GAME_OBJECTS[x][y] = null;
        GameAriaBuilder.getGameMap().getChildren().remove(this);
    }

    @Override
    public void use() {
        AudioBuilder.playUseRandomAudio();
        remove();
        GameAriaBuilder.getCurrentPlayer().increaseOneHealth();
        GameAriaBuilder.showHealthOrUpdate();
    }
}

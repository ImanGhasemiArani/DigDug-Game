package ir.ac.kntu.gameobjects.randomObject;

import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.NotMovingGameObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SniperRandom extends Parent implements NotMovingGameObject, RandomObject {

    private final ImageView sniper;
    private int x;
    private int y;
    private Timeline timer = new Timeline(new KeyFrame(Duration.seconds(10),e->{
        destroy();
    }));

    public SniperRandom(int xx, int yy) {
        x = xx;
        y = yy;
        sniper = new ImageView(new Image("assets/sniper.png"));
        sniper.setOpacity(0);
        appear();
    }

    @Override
    public void appear() {
        sniper.setX(GameData.calculateRealXY(x)+5);
        sniper.setY(GameData.calculateRealXY(y)+5);
        getChildren().add(sniper);
        GameData.MAP_DATA[y][x] = GameData.SNIPER;
        GameData.NOT_MOVING_GAME_OBJECTS[x][y] = this;
        Timeline t1 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            sniper.setOpacity(sniper.getOpacity() + 0.1);
        }));
        t1.setCycleCount(10);
        t1.play();
        timer.play();
    }


    @Override
    public void destroy() {
        Timeline t2 = new Timeline(new KeyFrame(Duration.millis(100),e->{
            sniper.setOpacity(sniper.getOpacity() - 0.1);
            if (sniper.getOpacity() <= 0.1) {
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
        GameAriaBuilder.getPlayerCharacter().increaseRangeOfBullets();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10),e->{
            GameAriaBuilder.getPlayerCharacter().decreaseRangeOfBullets();
        }));
        timeline.play();
    }
}

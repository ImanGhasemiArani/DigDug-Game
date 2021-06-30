package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Block extends Parent implements NotMovingGameObject{

    private final ImageView block;
    private final int x;
    private final int y;
    private Timeline timeline;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        block = new ImageView(selectImage(y));
        block.setFitHeight(GameData.GAP+8);
        block.setFitWidth(GameData.GAP+8);
        appear();
    }

    private Image selectImage(int y) {
        Image image;
        if ( y >= 0 && y < GameData.SIZE_OF_GAME_ACTION_ARIA/4 ) {
            image = new Image("assets/block1.jpg");
        } else if( y >= GameData.SIZE_OF_GAME_ACTION_ARIA/4 && y < GameData.SIZE_OF_GAME_ACTION_ARIA*2/4 ) {
            image = new Image("assets/block2.jpg");
        } else if( y >= GameData.SIZE_OF_GAME_ACTION_ARIA*2/4 && y < GameData.SIZE_OF_GAME_ACTION_ARIA*3/4 ) {
            image = new Image("assets/block3.jpg");
        } else {
            image = new Image("assets/block4.jpg");
        }
        return image;
    }

    @Override
    public void appear() {
        block.setX(GameData.calculateRealXY(x)-4);
        block.setY(GameData.calculateRealXY(y)-4);
        getChildren().add(block);
        GameData.BLOCKS[x][y] = this;
    }

    @Override
    public void destroy() {
        timeline = new Timeline(new KeyFrame(Duration.millis(20),e->{
            block.setOpacity(block.getOpacity()-0.1);
            if (block.getOpacity() <= 0) {
                this.remove();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        GameData.MAP_DATA[y][x] = GameData.EMPTY_BLOCK;
        GameData.increaseScore(50);
    }

    private void remove() {
        timeline.stop();
        GameAriaBuilder.getGameMap().getChildren().remove(this);
    }
}

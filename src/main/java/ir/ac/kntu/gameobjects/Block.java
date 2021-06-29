package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Block extends Parent {

    private final ImageView block;
    private final int x;
    private final int y;
    private Timeline timeline;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        block = new ImageView(selectImage(y));
        block.setFitHeight(GameData.GAP);
        block.setFitWidth(GameData.GAP);
        block.setX(GameData.calculateRealXY(x));
        block.setY(GameData.calculateRealXY(y));
        getChildren().add(block);
        GameData.BLOCKS[x][y] = this;
    }

    private Image selectImage(int y) {
        Image image;
        if ( y >= 0 && y <= GameData.SIZE_OF_GAME_ACTION_ARIA/4 ) {
            image = new Image("assets/block1.jpg");
        } else if( y > GameData.SIZE_OF_GAME_ACTION_ARIA/4 && y <= GameData.SIZE_OF_GAME_ACTION_ARIA/2 ) {
            image = new Image("assets/block2.jpg");
        } else if( y > GameData.SIZE_OF_GAME_ACTION_ARIA/2 && y <= GameData.SIZE_OF_GAME_ACTION_ARIA/4*3 ) {
            image = new Image("assets/block3.jpg");
        } else {
            image = new Image("assets/block4.jpg");
        }
        return image;
    }



    public void destroy() {
        timeline = new Timeline(new KeyFrame(Duration.millis(30),e->{
            block.setOpacity(block.getOpacity()-0.1);
            if (block.getOpacity() <= 0) {
                remove();
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

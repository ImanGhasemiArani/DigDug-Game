package ir.ac.kntu.gameObjects.enemy;

import ir.ac.kntu.controller.ai.AIDeadlyEnemy;
import ir.ac.kntu.gameBuilder.GameAriaBuilder;
import ir.ac.kntu.gameData.GameData;
import ir.ac.kntu.gameObjects.MovingGameObject;
import ir.ac.kntu.model.Direction;
import ir.ac.kntu.picture.PictureBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DeadlyEnemy extends Enemy implements MovingGameObject, EnemyInterface {

    private final ImageView fire;

    public DeadlyEnemy(int x, int y) {
        super(x, y, GameData.ENEMY_DEADLY,
                PictureBuilder.RIGHT_STAND_IMAGE_DEADLY, PictureBuilder.RIGHT_RUN_IMAGE_DEADLY, PictureBuilder.LEFT_STAND_IMAGE_DEADLY, PictureBuilder.LEFT_RUN_IMAGE_DEADLY,
                PictureBuilder.UP_STAND_IMAGE_DEADLY, PictureBuilder.UP_RUN_IMAGE_DEADLY, PictureBuilder.DOWN_STAND_IMAGE_DEADLY, PictureBuilder.DOWN_RUN_IMAGE_DEADLY,
                PictureBuilder.INFLATING_1_RIGHT_IMAGE_DEADLY, PictureBuilder.INFLATING_1_LEFT_IMAGE_DEADLY, PictureBuilder.INFLATING_1_UP_IMAGE_DEADLY, PictureBuilder.INFLATING_1_DOWN_IMAGE_DEADLY,
                PictureBuilder.INFLATING_2_RIGHT_IMAGE_DEADLY, PictureBuilder.INFLATING_2_LEFT_IMAGE_DEADLY, PictureBuilder.INFLATING_2_UP_IMAGE_DEADLY, PictureBuilder.INFLATING_2_DOWN_IMAGE_DEADLY,
                PictureBuilder.INFLATING_3_RIGHT_IMAGE_DEADLY, PictureBuilder.INFLATING_3_LEFT_IMAGE_DEADLY, PictureBuilder.INFLATING_3_UP_IMAGE_DEADLY, PictureBuilder.INFLATING_3_DOWN_IMAGE_DEADLY);
        fire = new ImageView(PictureBuilder.FIRE_RIGHT_IMAGE_DEADLY);
        fire.setFitWidth(GameData.GAP*3/4);
        fire.setFitHeight(GameData.GAP*3/4);
        configureAI(new AIDeadlyEnemy(this));
    }

    public void fire(Direction direction) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200),e-> GameAriaBuilder.getGameMap().getChildren().remove(fire)));
        timeline.setCycleCount(1);
        switch (direction) {
            case UP:
                changeImageForFire(Direction.UP);
                if (getFakeY() - 1 >= 0 && (GameData.MAP_DATA[getFakeY()-1][getFakeX()] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[getFakeY() -1][getFakeX()] == GameData.PLAYER_CHARACTER)) {
                    fire.setY(GameData.calculateRealXY(getFakeY() -1));
                    fire.setX(GameData.calculateRealXY(getFakeX()));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY() -1,getFakeX());
                    timeline.play();
                }
                break;
            case DOWN:
                changeImageForFire(Direction.DOWN);
                if ( getFakeY() + 1 < GameData.SIZE_OF_GAME_ACTION_ARIA && (GameData.MAP_DATA[getFakeY() +1][getFakeX()] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[getFakeY() +1][getFakeX()] == GameData.PLAYER_CHARACTER) ) {
                    fire.setY(GameData.calculateRealXY(getFakeY() +1));
                    fire.setX(GameData.calculateRealXY(getFakeX()));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY() +1,getFakeX());
                    timeline.play();
                }
                break;
            case LEFT:
                changeImageForFire(Direction.LEFT);
                if ( getFakeX() -1 >= 0 && (GameData.MAP_DATA[getFakeY()][getFakeX() -1] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[getFakeY()][getFakeX() -1] == GameData.PLAYER_CHARACTER )) {
                    fire.setY(GameData.calculateRealXY(getFakeY()));
                    fire.setX(GameData.calculateRealXY(getFakeX() -1));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY(),getFakeX() -1);
                    timeline.play();
                }
                break;
            default:
                changeImageForFire(Direction.RIGHT);
                if ( getFakeX() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA && (GameData.MAP_DATA[getFakeY()][getFakeX() +1] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[getFakeY()][getFakeX() +1] == GameData.PLAYER_CHARACTER )) {
                    fire.setY(GameData.calculateRealXY(getFakeY()));
                    fire.setX(GameData.calculateRealXY(getFakeX() +1));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY(),getFakeX() +1);
                    timeline.play();
                }
                break;
        }
    }

    private void changeImageForFire(Direction direction) {
        switch (direction) {
            case UP:
                fire.setImage(PictureBuilder.FIRE_UP_IMAGE_DEADLY);
                getEnemyCharacter().setImage(PictureBuilder.UP_STAND_IMAGE_DEADLY);
                break;
            case DOWN:
                fire.setImage(PictureBuilder.FIRE_DOWN_IMAGE_DEADLY);
                getEnemyCharacter().setImage(PictureBuilder.DOWN_STAND_IMAGE_DEADLY);
                break;
            case LEFT:
                fire.setImage(PictureBuilder.FIRE_LEFT_IMAGE_DEADLY);
                getEnemyCharacter().setImage(PictureBuilder.LEFT_STAND_IMAGE_DEADLY);
                break;
            default:
                fire.setImage(PictureBuilder.FIRE_RIGHT_IMAGE_DEADLY);
                getEnemyCharacter().setImage(PictureBuilder.RIGHT_STAND_IMAGE_DEADLY);
                break;
        }
    }

    private void checkFireHitPlayer(int x, int y) {
        if (GameData.MAP_DATA[x][y] == GameData.PLAYER_CHARACTER) {
            GameData.MAP_DATA[x][y] = GameData.EMPTY_BLOCK;
            GameAriaBuilder.getPlayerCharacter().die();
        }
    }

    @Override
    public void die() {
        super.die();
        GameData.increaseScore(150);
    }

}

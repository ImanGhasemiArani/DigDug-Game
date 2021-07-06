package ir.ac.kntu.gameobjects.enemy;

import ir.ac.kntu.controller.ai.AIDeadlyEnemy;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.MovingGameObject;
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
                PictureBuilder.rightStandImage, PictureBuilder.rightRunImage, PictureBuilder.leftStandImage, PictureBuilder.leftRunImage,
                PictureBuilder.upStandImage, PictureBuilder.upRunImage, PictureBuilder.downStandImage, PictureBuilder.downRunImage,
                PictureBuilder.inflating1RightImage, PictureBuilder.inflating1LeftImage, PictureBuilder.inflating1UpImage, PictureBuilder.inflating1DownImage,
                PictureBuilder.inflating2RightImage, PictureBuilder.inflating2LeftImage, PictureBuilder.inflating2UpImage, PictureBuilder.inflating2DownImage,
                PictureBuilder.inflating3RightImage, PictureBuilder.inflating3LeftImage, PictureBuilder.inflating3UpImage, PictureBuilder.inflating3DownImage);
        fire = new ImageView(PictureBuilder.fireRightImage);
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
                fire.setImage(PictureBuilder.fireUpImage);
                enemyCharacter.setImage(PictureBuilder.upStandImage);
                break;
            case DOWN:
                fire.setImage(PictureBuilder.fireDownImage);
                enemyCharacter.setImage(PictureBuilder.downStandImage);
                break;
            case LEFT:
                fire.setImage(PictureBuilder.fireLeftImage);
                enemyCharacter.setImage(PictureBuilder.leftStandImage);
                break;
            default:
                fire.setImage(PictureBuilder.fireRightImage);
                enemyCharacter.setImage(PictureBuilder.rightStandImage);
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

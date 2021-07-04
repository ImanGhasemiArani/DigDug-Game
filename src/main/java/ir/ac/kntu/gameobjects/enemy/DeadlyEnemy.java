package ir.ac.kntu.gameobjects.enemy;

import ir.ac.kntu.controller.AIDeadlyEnemy;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.MovingGameObject;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DeadlyEnemy extends Parent implements MovingGameObject,Enemy {

    private Image rightStandImage = new Image("assets/deadlyBalloonRight1.png");
    private Image rightRunImage = new Image("assets/deadlyBalloonRight2.png");
    private Image leftStandImage = new Image("assets/deadlyBalloonLeft1.png");
    private Image leftRunImage = new Image("assets/deadlyBalloonLeft2.png");
    private Image upStandImage = new Image("assets/deadlyBalloonUp1.png");
    private Image upRunImage = new Image("assets/deadlyBalloonUp2.png");
    private Image downStandImage = new Image("assets/deadlyBalloonDown1.png");
    private Image downRunImage = new Image("assets/deadlyBalloonDown2.png");
    private final Image inflating1RightImage = new Image("assets/inflating4Right.png");
    private final Image inflating1LeftImage = new Image("assets/inflating4Left.png");
    private final Image inflating1UpImage = new Image("assets/inflating4Up.png");
    private final Image inflating1DownImage = new Image("assets/inflating4Down.png");
    private final Image inflating2RightImage = new Image("assets/inflating5Right.png");
    private final Image inflating2LeftImage = new Image("assets/inflating5Left.png");
    private final Image inflating2UpImage = new Image("assets/inflating5Up.png");
    private final Image inflating2DownImage = new Image("assets/inflating5Down.png");
    private final Image inflating3RightImage = new Image("assets/inflating6Right.png");
    private final Image inflating3LeftImage = new Image("assets/inflating6Left.png");
    private final Image inflating3UpImage = new Image("assets/inflating6Up.png");
    private final Image inflating3DownImage = new Image("assets/inflating6Down.png");
    private final Image fireRightImage = new Image("assets/fireRight.png");
    private final Image fireLeftImage = new Image("assets/fireLeft.png");
    private final Image fireUpImage = new Image("assets/fireUp.png");
    private final Image fireDownImage = new Image("assets/fireDown.png");

    private Image standImage = rightStandImage;
    private Image runImage = rightRunImage;

    private final ImageView enemyCharacter = new ImageView(standImage);
    private final ImageView fire = new ImageView(fireRightImage);
    private final Timeline animationOfMovement = new Timeline(new KeyFrame(Duration.millis(80), e-> changeImageToCreateAnimation()));
    private int tempMoveHelper;
    private int directHelp;
    private int health;
    private final AIDeadlyEnemy ai = new AIDeadlyEnemy(this);
    private final Timeline aiLoop = new Timeline(new KeyFrame(Duration.millis(1000), e->{
        if (GameData.isAiControl()) {
            ai.startAI();
        }
    }));

    public DeadlyEnemy(int x,int y) {
        enemyCharacter.setFitWidth(GameData.GAP*3/4);
        enemyCharacter.setFitHeight(GameData.GAP*3/4);
        setPosition(GameData.calculateRealXY(x)+5,GameData.calculateRealXY(y)+5);
        GameData.ENEMIES[x][y] = this;
        getChildren().add(enemyCharacter);
        fire.setFitWidth(GameData.GAP*3/4);
        fire.setFitHeight(GameData.GAP*3/4);
        directHelp = 1;
        health = 3;
        animationOfMovement.setCycleCount(Timeline.INDEFINITE);
        aiLoop.setCycleCount(Timeline.INDEFINITE);
        aiLoop.play();
    }

    private final Timeline tUpDown = new Timeline(new KeyFrame(Duration.millis(80), e->{
        setYPosition(getYPosition() + directHelp * GameData.GAP / 10);
        if (getYPosition() == tempMoveHelper) {
            animationOfMovement.stop();
        }
    }));

    private final Timeline tLeftRight = new Timeline(new KeyFrame(Duration.millis(80),e->{
        setXPosition(getXPosition() + directHelp * GameData.GAP / 10);
        if (getXPosition() == tempMoveHelper) {
            animationOfMovement.stop();
        }
    }));

    @Override
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                directHelp = -1;
                moveUpDown();
                break;
            case DOWN:
                directHelp = 1;
                moveUpDown();
                break;
            case LEFT:
                directHelp = -1;
                moveLeftRight();
                break;
            case RIGHT:
                directHelp = 1;
                moveLeftRight();
                break;
            default:
                break;
        }
    }

    private void moveUpDown() {
        if (directHelp == 1) {
            standImage = downStandImage;
            runImage = downRunImage;
        } else {
            standImage = upStandImage;
            runImage = upRunImage;
        }
        if ( GameData.MAP_DATA[ getFakeY() +directHelp ][ getFakeX() ] == GameData.EMPTY_BLOCK) {
            GameData.MAP_DATA[ getFakeY() + directHelp ][ getFakeX() ] = GameData.ENEMY_DEADLY;
            GameData.ENEMIES[getFakeY() + directHelp][getFakeX()] = this;
            moveHelperMethod(getYPosition() + directHelp * GameData.GAP, tUpDown);
        }
    }

    private void moveLeftRight() {
        if (directHelp == 1) {
            standImage = rightStandImage;
            runImage = rightRunImage;
        } else {
            standImage = leftStandImage;
            runImage = leftRunImage;
        }
        if ( GameData.MAP_DATA[ getFakeY() ][ getFakeX() +directHelp ] == GameData.EMPTY_BLOCK) {
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() +directHelp ] = GameData.ENEMY_DEADLY;
            GameData.ENEMIES[getFakeY()][getFakeX() +directHelp] = this;
            moveHelperMethod(getXPosition() +directHelp * GameData.GAP,tLeftRight);
        }
    }

    private void moveHelperMethod(int result,Timeline t) {
        GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
        tempMoveHelper = result;
        t.setCycleCount(10);
        t.play();
        animationOfMovement.play();
    }

    public void fire(Direction direction) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100),e-> GameAriaBuilder.getGameMap().getChildren().remove(fire)));
        timeline.setCycleCount(1);
        switch (direction) {
            case UP:
                changeImageForFire(Direction.UP);
                if ( getFakeY() - 1 >= 0 && (GameData.MAP_DATA[ getFakeY() -1 ][ getFakeX() ] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[ getFakeY() -1 ][ getFakeX() ] == GameData.PLAYER_CHARACTER) ) {
                    fire.setY(GameData.calculateRealXY(getFakeY() -1));
                    fire.setX(GameData.calculateRealXY(getFakeX()));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY() -1,getFakeX());
                    timeline.play();
                }
                break;
            case DOWN:
                changeImageForFire(Direction.DOWN);
                if ( getFakeY() + 1 < GameData.SIZE_OF_GAME_ACTION_ARIA && (GameData.MAP_DATA[ getFakeY() +1 ][ getFakeX() ] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[ getFakeY() +1 ][ getFakeX() ] == GameData.PLAYER_CHARACTER) ) {
                    fire.setY(GameData.calculateRealXY(getFakeY() +1));
                    fire.setX(GameData.calculateRealXY(getFakeX()));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY() +1,getFakeX());
                    timeline.play();
                }
                break;
            case LEFT:
                changeImageForFire(Direction.LEFT);
                if ( getFakeX() -1 >= 0 && (GameData.MAP_DATA[ getFakeY() ][ getFakeX() -1 ] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[ getFakeY() ][ getFakeX() -1 ] == GameData.PLAYER_CHARACTER )) {
                    fire.setY(GameData.calculateRealXY(getFakeY()));
                    fire.setX(GameData.calculateRealXY(getFakeX() -1));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY(),getFakeX() -1);
                    timeline.play();
                }
                break;
            case RIGHT:
                changeImageForFire(Direction.RIGHT);
                if ( getFakeX() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA && (GameData.MAP_DATA[ getFakeY() ][ getFakeX() +1 ] == GameData.EMPTY_BLOCK || GameData.MAP_DATA[ getFakeY() ][ getFakeX() +1 ] == GameData.PLAYER_CHARACTER )) {
                    fire.setY(GameData.calculateRealXY(getFakeY()));
                    fire.setX(GameData.calculateRealXY(getFakeX() +1));
                    GameAriaBuilder.getGameMap().getChildren().add(fire);
                    checkFireHitPlayer(getFakeY(),getFakeX() +1);
                    timeline.play();
                }
                break;
            default:
                break;
        }
    }

    private void changeImageForFire(Direction direction) {
        switch (direction) {
            case UP:
                fire.setImage(fireUpImage);
                enemyCharacter.setImage(upStandImage);
                break;
            case DOWN:
                fire.setImage(fireDownImage);
                enemyCharacter.setImage(downStandImage);
                break;
            case LEFT:
                fire.setImage(fireLeftImage);
                enemyCharacter.setImage(leftStandImage);
                break;
            case RIGHT:
                fire.setImage(fireRightImage);
                enemyCharacter.setImage(rightStandImage);
                break;
            default:
                break;
        }
    }

    private void checkFireHitPlayer(int x, int y) {
        if (GameData.MAP_DATA[x][y] == GameData.PLAYER_CHARACTER) {
            GameAriaBuilder.getPlayerCharacter().die();
        }
    }

    @Override
    public void die() {
        aiLoop.stop();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),e->{
            remove();
            GameData.increaseScore(300);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void inflating() {
        if (health == 3) {
            health--;
            standImage = runImage =rightStandImage = rightRunImage = inflating1RightImage;
            leftStandImage = leftRunImage = inflating1LeftImage;
            downStandImage = downRunImage = inflating1DownImage;
            upStandImage = upRunImage = inflating1UpImage;
            enemyCharacter.setImage(runImage);
        } else if(health == 2) {
            health--;
            standImage = runImage =rightStandImage = rightRunImage = inflating2RightImage;
            leftStandImage = leftRunImage = inflating2LeftImage;
            downStandImage = downRunImage = inflating2DownImage;
            upStandImage = upRunImage = inflating2UpImage;
            enemyCharacter.setImage(runImage);
        }else if(health == 1) {
            health--;
            standImage = runImage =rightStandImage = rightRunImage = inflating3RightImage;
            leftStandImage = leftRunImage = inflating3LeftImage;
            downStandImage = downRunImage = inflating3DownImage;
            upStandImage = upRunImage = inflating3UpImage;
            enemyCharacter.setImage(runImage);
            die();
        }
    }

    @Override
    public void remove() {
        GameData.MAP_DATA[getFakeY()][getFakeX()] = GameData.EMPTY_BLOCK;
        GameAriaBuilder.getGameMap().getChildren().remove(this);
    }

    private void changeImageToCreateAnimation() {
        if ( enemyCharacter.getImage().equals(standImage) ) {
            enemyCharacter.setImage(runImage);
        } else {
            enemyCharacter.setImage(standImage);
        }
    }

    public int getXPosition() {
        return (int) enemyCharacter.getX();
    }

    public int getYPosition() {
        return (int) enemyCharacter.getY();
    }

    private void setXPosition(int x) {
        enemyCharacter.setX(x);
    }

    private void setYPosition(int y) {
        enemyCharacter.setY(y);
    }

    public void setPosition(int x, int y) {
        setXPosition(x);
        setYPosition(y);
    }

    public int getFakeX() {
        return (getXPosition()-5)/GameData.GAP;
    }

    public int getFakeY() {
        return (getYPosition()-5)/GameData.GAP;
    }

    public Timeline getAiLoop() {
        return aiLoop;
    }
}

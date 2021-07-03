package ir.ac.kntu.gameobjects.enemy;

import ir.ac.kntu.controller.AIGame;
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



public class SimpleEnemy extends Parent implements MovingGameObject {
    private Image rightStandImage = new Image("assets/simpleBalloonRight1.png");
    private Image rightRunImage = new Image("assets/simpleBalloonRight2.png");
    private Image leftStandImage = new Image("assets/simpleBalloonLeft1.png");
    private Image leftRunImage = new Image("assets/simpleBalloonLeft2.png");
    private Image upStandImage = new Image("assets/simpleBalloonUp1.png");
    private Image upRunImage = new Image("assets/simpleBalloonUp2.png");
    private Image downStandImage = new Image("assets/simpleBalloonDown1.png");
    private Image downRunImage = new Image("assets/simpleBalloonDown2.png");
    private Image inflating1RightImage = new Image("assets/inflating1Right.png");
    private Image inflating1LeftImage = new Image("assets/inflating1Left.png");
    private Image inflating1UpImage = new Image("assets/inflating1Up.png");
    private Image inflating1DownImage = new Image("assets/inflating1Down.png");
    private Image inflating2RightImage = new Image("assets/inflating2Right.png");
    private Image inflating2LeftImage = new Image("assets/inflating2Left.png");
    private Image inflating2UpImage = new Image("assets/inflating2Up.png");
    private Image inflating2DownImage = new Image("assets/inflating2Down.png");
    private Image inflating3RightImage = new Image("assets/inflating3Right.png");
    private Image inflating3LeftImage = new Image("assets/inflating3Left.png");
    private Image inflating3UpImage = new Image("assets/inflating3Up.png");
    private Image inflating3DownImage = new Image("assets/inflating3Down.png");

    private Image standImage = rightStandImage;
    private Image runImage = rightRunImage;

    private final ImageView enemyCharacter = new ImageView(standImage);
    private final Timeline animationOfMovement = new Timeline(new KeyFrame(Duration.millis(80),e-> changeImageToCreateAnimation()));
    private int tempMoveHelper;
    private int directHelp;
    private int health;
    private final AIGame ai = new AIGame(this);
    private final Timeline aiLoop = new Timeline(new KeyFrame(Duration.millis(1000), e->{
        if (GameData.isAiControl()) {
            ai.startAI();
        }
    }));

    public SimpleEnemy(int x,int y) {
        enemyCharacter.setFitWidth(GameData.GAP*3/4);
        enemyCharacter.setFitHeight(GameData.GAP*3/4);
        setPosition(GameData.calculateRealXY(x)+5,GameData.calculateRealXY(y)+5);
        GameData.ENEMIES[x][y] = this;
        getChildren().add(enemyCharacter);
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
            GameData.MAP_DATA[ getFakeY() + directHelp ][ getFakeX() ] = GameData.Enemy_simple;
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
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() +directHelp ] = GameData.Enemy_simple;
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

    @Override
    public void die() {
        aiLoop.stop();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),e->{
            remove();
            GameData.increaseScore(150);
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

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









package ir.ac.kntu.gameobjects.enemy;

import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.controller.ai.AIInterface;
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



public class Enemy extends Parent implements MovingGameObject,EnemyInterface {
    private Image rightStandImage;
    private Image rightRunImage;
    private Image leftStandImage;
    private Image leftRunImage;
    private Image upStandImage;
    private Image upRunImage;
    private Image downStandImage;
    private Image downRunImage;
    private final Image inflating1RightImage;
    private final Image inflating1LeftImage;
    private final Image inflating1UpImage;
    private final Image inflating1DownImage;
    private final Image inflating2RightImage;
    private final Image inflating2LeftImage;
    private final Image inflating2UpImage;
    private final Image inflating2DownImage;
    private final Image inflating3RightImage;
    private final Image inflating3LeftImage;
    private final Image inflating3UpImage;
    private final Image inflating3DownImage;

    private Image standImage;
    private Image runImage;

    protected final ImageView enemyCharacter;
    private final Timeline animationOfMovement = new Timeline(new KeyFrame(Duration.millis(80),e-> changeImageToCreateAnimation()));
    private int tempMoveHelper;
    private int directHelp;
    private int health;

    private AIInterface ai;
    private final Timeline aiLoop;
    private final int relatedNumber;

    public Enemy(int x, int y, int relatedNumber,
                 Image rightStandImage, Image rightRunImage, Image leftStandImage, Image leftRunImage,
                 Image upStandImage, Image upRunImage, Image downStandImage, Image downRunImage,
                 Image inflating1RightImage, Image inflating1LeftImage, Image inflating1UpImage, Image inflating1DownImage,
                 Image inflating2RightImage, Image inflating2LeftImage, Image inflating2UpImage, Image inflating2DownImage,
                 Image inflating3RightImage, Image inflating3LeftImage, Image inflating3UpImage, Image inflating3DownImage) {
        this.relatedNumber = relatedNumber;
        this.rightStandImage = rightStandImage;
        this.rightRunImage = rightRunImage;
        this.leftStandImage = leftStandImage;
        this.leftRunImage = leftRunImage;
        this.upStandImage = upStandImage;
        this.upRunImage = upRunImage;
        this.downStandImage = downStandImage;
        this.downRunImage = downRunImage;
        this.inflating1RightImage = inflating1RightImage;
        this.inflating1LeftImage = inflating1LeftImage;
        this.inflating1UpImage = inflating1UpImage;
        this.inflating1DownImage = inflating1DownImage;
        this.inflating2RightImage = inflating2RightImage;
        this.inflating2LeftImage = inflating2LeftImage;
        this.inflating2UpImage = inflating2UpImage;
        this.inflating2DownImage = inflating2DownImage;
        this.inflating3RightImage = inflating3RightImage;
        this.inflating3LeftImage = inflating3LeftImage;
        this.inflating3UpImage = inflating3UpImage;
        this.inflating3DownImage = inflating3DownImage;
        standImage = rightStandImage;
        runImage = rightRunImage;
        enemyCharacter = new ImageView(standImage);
        enemyCharacter.setFitWidth(GameData.GAP*3/4);
        enemyCharacter.setFitHeight(GameData.GAP*3/4);
        setPosition(GameData.calculateRealXY(x)+5,GameData.calculateRealXY(y)+5);
        GameData.ENEMIES[x][y] = this;
        getChildren().add(enemyCharacter);
        directHelp = 1;
        health = 3;
        animationOfMovement.setCycleCount(Timeline.INDEFINITE);
        aiLoop = new Timeline(new KeyFrame(Duration.millis(1200), e->{
            if (GameData.isAiControl()) {
                ai.startAI();
            }
        }));
        aiLoop.setCycleCount(Timeline.INDEFINITE);
    }

    protected void configureAI(AIInterface ai) {
        this.ai = ai;
        GameData.AIS.add(ai);
        new Timeline(new KeyFrame(Duration.seconds(1),e->aiLoop.play())).play();
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

            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() + directHelp ][ getFakeX() ] = relatedNumber;
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

            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() +directHelp ] = relatedNumber;
            GameData.ENEMIES[getFakeY()][getFakeX() +directHelp] = this;
            moveHelperMethod(getXPosition() +directHelp * GameData.GAP,tLeftRight);

        }
    }

    private void moveHelperMethod(int result,Timeline t) {
//        GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
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

    @Override
    public void inflating() {
        aiLoop.stop();
        AudioBuilder.playInflatingEnemyAudio();
        if (health == 3) {
            health--;
            standImage = runImage =rightStandImage = rightRunImage = inflating1RightImage;
            leftStandImage = leftRunImage = inflating1LeftImage;
            downStandImage = downRunImage = inflating1DownImage;
            upStandImage = upRunImage = inflating1UpImage;
            enemyCharacter.setImage(runImage);
            aiLoop.play();
        } else if(health == 2) {
            health--;
            standImage = runImage =rightStandImage = rightRunImage = inflating2RightImage;
            leftStandImage = leftRunImage = inflating2LeftImage;
            downStandImage = downRunImage = inflating2DownImage;
            upStandImage = upRunImage = inflating2UpImage;
            enemyCharacter.setImage(runImage);
            aiLoop.play();
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
        AudioBuilder.playKillEnemyAudio();
        GameData.decreaseNumberOfEnemy();
        GameData.MAP_DATA[getFakeY()][getFakeX()] = GameData.EMPTY_BLOCK;
        GameAriaBuilder.getGameMap().getChildren().remove(this);
        GameAriaBuilder.checkForNextLevel();
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









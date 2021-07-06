package ir.ac.kntu.gameobjects;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Random;


public class PlayerCharacter extends Parent implements MovingGameObject {

    private final Image rightStandImage = new Image("assets/rightStand.png");
    private final Image rightRunImage = new Image("assets/rightRun.png");
    private final Image leftStandImage = new Image("assets/leftStand.png");
    private final Image leftRunImage = new Image("assets/leftRun.png");
    private final Image upStandImage = new Image("assets/upStand.png");
    private final Image upRunImage = new Image("assets/upRun.png");
    private final Image downStandImage = new Image("assets/downStand.png");
    private final Image downRunImage = new Image("assets/downRun.png");
    private final Image digRightImage = new Image("assets/digRight.png");
    private final Image dig2RightImage = new Image("assets/dig2Right.png");
    private final Image digLeftImage = new Image("assets/digLeft.png");
    private final Image dig2LeftImage = new Image("assets/dig2Left.png");
    private final Image digUpImage = new Image("assets/digUp.png");
    private final Image dig2UpImage = new Image("assets/dig2Up.png");
    private final Image digDownImage = new Image("assets/digDown.png");
    private final Image dig2DownImage = new Image("assets/dig2Down.png");

    private Image standImage = rightStandImage;
    private Image runImage = rightRunImage;

    private final ImageView playerCharacter = new ImageView(standImage);
    private final Timeline animationOfMovement = new Timeline(new KeyFrame(Duration.millis(80),e-> changeImageToCreateAnimation()));
    private int tempMoveHelper;
    private int directHelp;
    private Direction lastDirection;
    private int speedTemp;
    private int speed;
    private int rangeOfBullets;

    public PlayerCharacter(int x,int y) {
        playerCharacter.setFitWidth(GameData.GAP);
        playerCharacter.setFitHeight(GameData.GAP);
        setPosition(GameData.calculateRealXY(x),GameData.calculateRealXY(y));
        getChildren().add(playerCharacter);
        speedTemp = speed = 1;
        directHelp = 1;
        rangeOfBullets = 3;
        lastDirection = Direction.RIGHT;
        animationOfMovement.setCycleCount(Timeline.INDEFINITE);
    }

    private final Timeline tUpDown = new Timeline(new KeyFrame(Duration.millis(50), e->{
        if (!GameData.isGameControl()) {
            setYPosition(getYPosition() + directHelp * speedTemp * GameData.GAP / 10);
            if (getYPosition() == tempMoveHelper) {
                animationOfMovement.stop();
                GameData.gameControlOn();
            }
        }else {
            animationOfMovement.stop();
        }
    }));

    private final Timeline tLeftRight = new Timeline(new KeyFrame(Duration.millis(50), e->{
        if (!GameData.isGameControl()) {
            setXPosition(getXPosition() + directHelp * speedTemp * GameData.GAP / 10);
            if (getXPosition() == tempMoveHelper) {
                animationOfMovement.stop();
                GameData.gameControlOn();
            }
        }else {
            animationOfMovement.stop();
        }
    }));

    @Override
    public void move(Direction direction) {
        speedTemp = speed;
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            default:
                break;
        }
    }


    private void moveUp() {
        lastDirection = Direction.UP;
        standImage = upStandImage;
        runImage = upRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getFakeY() - 1 >= 0 &&
                GameData.MAP_DATA[ getFakeY() -1 ][ getFakeX() ] == GameData.EMPTY_BLOCK) {
            GameData.setPositionXYPLayerCharacter(getFakeX(),getFakeY()-1);
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() -1 ][ getFakeX() ] = GameData.PLAYER_CHARACTER;
            moveHelperMethod(getYPosition() - GameData.GAP, tUpDown,-1);
        }
    }


    private void moveDown() {
        lastDirection = Direction.DOWN;
        standImage = downStandImage;
        runImage = downRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getFakeY() + 1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ getFakeY() +1 ][ getFakeX() ] == GameData.EMPTY_BLOCK) {
            GameData.setPositionXYPLayerCharacter(getFakeX(),getFakeY()+1);
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() +1 ][ getFakeX() ] = GameData.PLAYER_CHARACTER;
            moveHelperMethod(getYPosition() + GameData.GAP,tUpDown,1);
        }
    }

    private void moveLeft() {
        lastDirection = Direction.LEFT;
        standImage = leftStandImage;
        runImage = leftRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getFakeX() - 1 >= 0 &&
                GameData.MAP_DATA[ getFakeY() ][ getFakeX() -1 ] == GameData.EMPTY_BLOCK) {
            GameData.setPositionXYPLayerCharacter(getFakeX() -1,getFakeY());
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() -1 ] = GameData.PLAYER_CHARACTER;
            moveHelperMethod(getXPosition() - GameData.GAP, tLeftRight,-1);
        }
    }

    private void moveRight() {
        lastDirection = Direction.RIGHT;
        standImage = rightStandImage;
        runImage = rightRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getFakeX() + 1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ getFakeY() ][ getFakeX() +1 ] == GameData.EMPTY_BLOCK) {
            GameData.setPositionXYPLayerCharacter(getFakeX() +1,getFakeY());
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
            GameData.MAP_DATA[ getFakeY() ][ getFakeX() +1 ] = GameData.PLAYER_CHARACTER;
            moveHelperMethod(getXPosition() + GameData.GAP,tLeftRight,1);
        }
    }

    private void moveHelperMethod(int result,Timeline t,int d) {
//        GameData.MAP_DATA[ getFakeY() ][ getFakeX() ] = GameData.EMPTY_BLOCK;
        tempMoveHelper = result;
        directHelp = d;
        t.setCycleCount(10/ speedTemp);
        GameData.gameControlOff();
        t.play();
        animationOfMovement.play();
    }

    public void digging() {
        switch (lastDirection) {
            case UP:
                if ( getFakeY() -1 >=0 &&
                        GameData.MAP_DATA[getFakeY() -1 ][getFakeX()] == GameData.BLOCK) {
                    standImage = digUpImage;
                    runImage = dig2UpImage;
                    GameData.getBlockInSpecificFakeXY(getFakeX(),getFakeY() -1).destroy();
                }
                break;
            case DOWN:
                if (getFakeY() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        GameData.MAP_DATA[getFakeY() +1 ][getFakeX()] == GameData.BLOCK) {
                    standImage = digDownImage;
                    runImage = dig2DownImage;
                    GameData.getBlockInSpecificFakeXY(getFakeX(),getFakeY() +1).destroy();
                }
                break;
            case LEFT:
                if ( getFakeX() -1 >=0 &&
                        GameData.MAP_DATA[getFakeY()][getFakeX() -1 ] == GameData.BLOCK) {
                    standImage = digLeftImage;
                    runImage = dig2LeftImage;
                    GameData.getBlockInSpecificFakeXY(getFakeX() -1,getFakeY() ).destroy();
                }
                break;
            case RIGHT:
                if ( getFakeX() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        GameData.MAP_DATA[getFakeY()][getFakeX() +1 ] == GameData.BLOCK) {
                    standImage = digRightImage;
                    runImage = dig2RightImage;
                    GameData.getBlockInSpecificFakeXY(getFakeX() +1,getFakeY() ).destroy();
                }
                break;
            default:
                break;
        }
    }

    public void useRandomObject() {
        switch (lastDirection) {
            case UP:
                if ( getFakeY() -1 >=0 &&
                        (GameData.MAP_DATA[getFakeY() -1 ][getFakeX()] == GameData.HEART ||
                        GameData.MAP_DATA[getFakeY() -1 ][getFakeX()] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[getFakeY() -1 ][getFakeX()] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getFakeX(),getFakeY() -1).use();
                }
                break;
            case DOWN:
                if ( getFakeY() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        (GameData.MAP_DATA[getFakeY() +1 ][getFakeX()] == GameData.HEART ||
                        GameData.MAP_DATA[getFakeY() +1 ][getFakeX()] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[getFakeY() +1 ][getFakeX()] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getFakeX(),getFakeY() +1).use();
                }
                break;
            case LEFT:
                if ( getFakeX() -1 >=0 &&
                        (GameData.MAP_DATA[getFakeY()][getFakeX() -1 ] == GameData.HEART ||
                        GameData.MAP_DATA[getFakeY()][getFakeX() -1 ] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[getFakeY()][getFakeX() -1 ] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getFakeX() -1,getFakeY() ).use();
                }
                break;
            case RIGHT:
                if ( getFakeX() +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        (GameData.MAP_DATA[getFakeY()][getFakeX() +1 ] == GameData.HEART ||
                        GameData.MAP_DATA[getFakeY()][getFakeX() +1 ] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[getFakeY()][getFakeX() +1 ] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getFakeX() +1,getFakeY() ).use();
                }
                break;
            default:
                break;
        }
    }

    public void shoot() {
        GameData.shootControlOff();
        new Bullet(lastDirection,rangeOfBullets,getXPosition(),getYPosition());
    }

    public void doneLevel() {
        AudioBuilder.playWinPlayerAudio();
    }

    @Override
    public void die() {
        GameData.gameControlOff();
        GameData.MAP_DATA[getFakeY()][getFakeX()] = GameData.EMPTY_BLOCK;
        AudioBuilder.playDiePlayerAudio();
        GameAriaBuilder.getCurrentPlayer().decreaseOneHealth();
        remove();
        if (GameAriaBuilder.getCurrentPlayer().getHealth() == -1) {
            GameData.gameOverGame();
            GameStarter.endGame();
        }else {
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500),e->{
                int x;
                int y;
                do {
                    x = new Random().nextInt(GameData.MAP_DATA.length);
                    y = new Random().nextInt(GameData.MAP_DATA[0].length);
                }while (GameData.MAP_DATA[y][x] != GameData.EMPTY_BLOCK);
                setPosition(GameData.calculateRealXY(x),GameData.calculateRealXY(y));
                GameData.MAP_DATA[y][x] = GameData.PLAYER_CHARACTER;
                GameAriaBuilder.getGameMap().getChildren().add(this);
                Timeline timeline1 = new Timeline(new KeyFrame(Duration.millis(1000),ee->GameData.gameControlOn()));
                timeline1.play();
            }));
            timeline.play();
        }
    }

    @Override
    public void remove() {
        GameAriaBuilder.getGameMap().getChildren().remove(this);
    }

    public void increaseSpeed() {
        this.speed = 2;
    }

    public void decreaseSpeed() {
        this.speed = 1;
    }

    public void increaseRangeOfBullets() {
        rangeOfBullets = 5;
    }

    public void decreaseRangeOfBullets() {
        rangeOfBullets = 3;
    }

    private void changeImageToCreateAnimation() {
        if ( playerCharacter.getImage().equals(standImage) ) {
            playerCharacter.setImage(runImage);
        } else {
            playerCharacter.setImage(standImage);
        }
    }

    public int getXPosition() {
        return (int) playerCharacter.getX();
    }

    public int getYPosition() {
        return (int) playerCharacter.getY();
    }

    private void setXPosition(int x) {
        playerCharacter.setX(x);
    }

    private void setYPosition(int y) {
        playerCharacter.setY(y);
    }

    public void setPosition(int x, int y) {
        setXPosition(x);
        setYPosition(y);
    }

    public int getFakeX() {
        return getXPosition()/GameData.GAP;
    }

    public int getFakeY() {
        return getYPosition()/GameData.GAP;
    }

}

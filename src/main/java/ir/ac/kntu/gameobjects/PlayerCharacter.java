package ir.ac.kntu.gameobjects;

import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;


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
    private Image digImage = digRightImage;

    private final ImageView playerCharacter = new ImageView(standImage);
    private Timeline animationOfMovement;
    private int tempMoveHelper;
    private Direction lastDirection;
    private int speedTemp;
    private int speed;

    public PlayerCharacter(int x,int y) {
        playerCharacter.setFitWidth(GameData.GAP);
        playerCharacter.setFitHeight(GameData.GAP);
        setPosition(GameData.calculateRealXY(x),GameData.calculateRealXY(y));
        getChildren().add(playerCharacter);
        speedTemp = speed = 1;
        lastDirection = Direction.RIGHT;
        creteAnimationOfMovementTimeLine();
    }

    private Timeline tUp = new Timeline(new KeyFrame(Duration.millis(50),e->{
        setYPosition(getYPosition() - speedTemp *GameData.GAP/10);
        if (getYPosition() == tempMoveHelper) {
            animationOfMovement.stop();
            GameData.gameControlOn();
            updatePositionXY();
        }
    }));

    private Timeline tDown = new Timeline(new KeyFrame(Duration.millis(50),e->{
        setYPosition(getYPosition() + speedTemp *GameData.GAP/10);
        if (getYPosition() == tempMoveHelper) {
            animationOfMovement.stop();
            GameData.gameControlOn();
            updatePositionXY();
        }
    }));

    private Timeline tLeft = new Timeline(new KeyFrame(Duration.millis(50),e->{
        setXPosition(getXPosition() - speedTemp *GameData.GAP/10);
        if (getXPosition() == tempMoveHelper) {
            animationOfMovement.stop();
            GameData.gameControlOn();
            updatePositionXY();
        }
    }));

    private Timeline tRight = new Timeline(new KeyFrame(Duration.millis(50),e->{
        setXPosition(getXPosition() + speedTemp *GameData.GAP/10);
        if (getXPosition() == tempMoveHelper) {
            animationOfMovement.stop();
            GameData.gameControlOn();
            updatePositionXY();
        }
    }));

    private void creteAnimationOfMovementTimeLine() {
        animationOfMovement = new Timeline(new KeyFrame(Duration.millis(80),e-> changeImageToCreateAnimation()));
        animationOfMovement.setCycleCount(Timeline.INDEFINITE);
    }

    private void updatePositionXY() {
        GameData.MAP_DATA[ getYPosition()/GameData.GAP ][ getXPosition()/GameData.GAP ] = GameData.PLAYER_CHARACTER;
    }

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
        if ( getYPosition() - GameData.GAP >= 0 &&
                GameData.MAP_DATA[ getYPosition()/GameData.GAP -1 ][ getXPosition()/GameData.GAP ] == GameData.EMPTY_BLOCK) {
            moveHelperMethod(getYPosition() - GameData.GAP,tUp);
        }
    }


    private void moveDown() {
        lastDirection = Direction.DOWN;
        standImage = downStandImage;
        runImage = downRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getYPosition() + GameData.GAP + playerCharacter.getFitHeight() <= GameData.END_Y_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ getYPosition()/GameData.GAP +1 ][ getXPosition()/GameData.GAP ] == GameData.EMPTY_BLOCK) {
            moveHelperMethod(getYPosition() + GameData.GAP,tDown);
        }
    }

    private void moveLeft() {
        lastDirection = Direction.LEFT;
        standImage = leftStandImage;
        runImage = leftRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getXPosition() - GameData.GAP >= 0 &&
                GameData.MAP_DATA[ getYPosition()/GameData.GAP ][ getXPosition()/GameData.GAP -1 ] == GameData.EMPTY_BLOCK) {
            moveHelperMethod(getXPosition() - GameData.GAP,tLeft);
        }
    }

    private void moveRight() {
        lastDirection = Direction.RIGHT;
        standImage = rightStandImage;
        runImage = rightRunImage;
        changeImageToCreateAnimation();
        digging();
        useRandomObject();
        if ( getXPosition() + GameData.GAP + playerCharacter.getFitWidth() <= GameData.END_X_GAME_ACTION_ARIA &&
                GameData.MAP_DATA[ getYPosition()/GameData.GAP ][ getXPosition()/GameData.GAP +1 ] == GameData.EMPTY_BLOCK) {
            moveHelperMethod(getXPosition() + GameData.GAP,tRight);
        }
    }

    private void moveHelperMethod(int result,Timeline t) {
        GameData.MAP_DATA[ getYPosition()/GameData.GAP ][ getXPosition()/GameData.GAP ] = GameData.EMPTY_BLOCK;
        tempMoveHelper = result;
        t.setCycleCount(10/ speedTemp);
        GameData.gameControlOff();
        t.play();
        animationOfMovement.play();
    }

    public void digging() {
        switch (lastDirection) {
            case UP:
                if ( (getYPosition())/GameData.GAP -1 >=0 &&
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP -1 ][getXPosition()/GameData.GAP] == GameData.BLOCK) {
                    standImage = digUpImage;
                    runImage = dig2UpImage;
                    GameData.getBlockInSpecificFakeXY(getXPosition()/GameData.GAP,(getYPosition())/GameData.GAP -1).destroy();
                }
                break;
            case DOWN:
                if ((getYPosition())/GameData.GAP +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP +1 ][getXPosition()/GameData.GAP] == GameData.BLOCK) {
                    standImage = digDownImage;
                    runImage = dig2DownImage;
                    GameData.getBlockInSpecificFakeXY(getXPosition()/GameData.GAP,(getYPosition())/GameData.GAP +1).destroy();
                }
                break;
            case LEFT:
                if ( (getXPosition())/GameData.GAP -1 >=0 &&
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP -1 ] == GameData.BLOCK) {
                    standImage = digLeftImage;
                    runImage = dig2LeftImage;
                    GameData.getBlockInSpecificFakeXY(getXPosition()/GameData.GAP -1,(getYPosition())/GameData.GAP ).destroy();
                }
                break;
            case RIGHT:
                if ( (getXPosition())/GameData.GAP +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP +1 ] == GameData.BLOCK) {
                    standImage = digRightImage;
                    runImage = dig2RightImage;
                    GameData.getBlockInSpecificFakeXY(getXPosition()/GameData.GAP +1,(getYPosition())/GameData.GAP ).destroy();
                }
                break;
            default:
                break;
        }
    }

    public void useRandomObject() {
        switch (lastDirection) {
            case UP:
                if ( (getYPosition())/GameData.GAP -1 >=0 &&
                        (GameData.MAP_DATA[(getYPosition())/GameData.GAP -1 ][getXPosition()/GameData.GAP] == GameData.HEART ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP -1 ][getXPosition()/GameData.GAP] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP -1 ][getXPosition()/GameData.GAP] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getXPosition()/GameData.GAP,(getYPosition())/GameData.GAP -1).use();
                }
                break;
            case DOWN:
                if ((getYPosition())/GameData.GAP +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        (GameData.MAP_DATA[(getYPosition())/GameData.GAP +1 ][getXPosition()/GameData.GAP] == GameData.HEART ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP +1 ][getXPosition()/GameData.GAP] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP +1 ][getXPosition()/GameData.GAP] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getXPosition()/GameData.GAP,(getYPosition())/GameData.GAP +1).use();
                }
                break;
            case LEFT:
                if ( (getXPosition())/GameData.GAP -1 >=0 &&
                        (GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP -1 ] == GameData.HEART ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP -1 ] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP -1 ] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getXPosition()/GameData.GAP -1,(getYPosition())/GameData.GAP ).use();
                }
                break;
            case RIGHT:
                if ( (getXPosition())/GameData.GAP +1 < GameData.SIZE_OF_GAME_ACTION_ARIA &&
                        (GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP +1 ] == GameData.HEART ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP +1 ] == GameData.MUSHROOM ||
                        GameData.MAP_DATA[(getYPosition())/GameData.GAP][getXPosition()/GameData.GAP +1 ] == GameData.SNIPER)) {
                    GameData.getNotMovingGameObjectsInSpecificFakeXY(getXPosition()/GameData.GAP +1,(getYPosition())/GameData.GAP ).use();
                }
                break;
            default:
                break;
        }
    }

    public void shoot() {

    }

    @Override
    public void stopMove() {
//        move(0.0,Direction.RIGHT);
//        animationOfMovement.stop();
    }

    @Override
    public void remove() {

    }

    public void increaseSpeed() {
        this.speed = 2;
    }

    public void decreaseSpeed() {
        this.speed = 1;
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

    private void setYPosition(int x) {
        playerCharacter.setY(x);
    }

    public void setPosition(int x, int y) {
        setXPosition(x);
        setYPosition(y);
    }

}

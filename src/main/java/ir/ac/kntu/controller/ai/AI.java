package ir.ac.kntu.controller.ai;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.enemy.Enemy;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AI implements AIInterface {

    private final Enemy enemy;
    private int x;
    private int y;
    private int lastXPosition;
    private int lastYPosition;

    public AI(Enemy enemy) {
        this.enemy = enemy;
        lastXPosition = enemy.getFakeX();
        lastYPosition = enemy.getFakeY();
    }

    @Override
    public void startAI() {
        Thread thread = new Thread(()-> {
            x = GameData.getXPositionPlayerCharacter();
            y = GameData.getYPositionPlayerCharacter();
            if (enemy.getFakeX() -1 < 0 || enemy.getFakeX() +1 >= GameData.SIZE_OF_GAME_ACTION_ARIA ||
                    enemy.getFakeY() -1 < 0 || enemy.getFakeY() +1 >= GameData.SIZE_OF_GAME_ACTION_ARIA ) {
                Timeline timeline = new Timeline(new KeyFrame(Duration.ONE,e->{
                    GameAriaBuilder.getCurrentPlayer().decreaseOneHealth();
                    if (GameAriaBuilder.getCurrentPlayer().getHealth() == -1) {
                        GameData.gameOverGame();
                        GameStarter.endGame();
                    }
                    enemy.remove();
                    enemy.getAiLoop().stop();
                }));
                timeline.play();
            } else {
                selectActionOnLeftRight();
                lastXPosition = enemy.getFakeX();
                lastYPosition = enemy.getFakeY();
            }
        });
        thread.start();
    }

    @Override
    public void endAI() {
        enemy.getAiLoop().stop();
    }

    protected void selectActionOnLeftRight() {
        x = GameData.getXPositionPlayerCharacter();
        y = GameData.getYPositionPlayerCharacter();
        if (enemy.getFakeX() - x > 0) {
            if ( GameData.MAP_DATA[ enemy.getFakeY() ][ enemy.getFakeX() +1 ] == GameData.EMPTY_BLOCK && enemy.getFakeX()+1 != lastXPosition) {
                enemy.
                        move(Direction.RIGHT);
            } else {
                if (!selectActionOnDownUp()) {
                    enemy.move(Direction.LEFT);
                }
            }
        }else if (enemy.getFakeX() - x < 0){
            if ( GameData.MAP_DATA[ enemy.getFakeY() ][ enemy.getFakeX() -1 ] == GameData.EMPTY_BLOCK && enemy.getFakeX()-1 != lastXPosition) {
                enemy.move(Direction.LEFT);
            }else {
                if (!selectActionOnDownUp()) {
                    enemy.move(Direction.RIGHT);
                }
            }
        }else {
            if ( GameData.MAP_DATA[ enemy.getFakeY() ][ enemy.getFakeX() +1 ] == GameData.EMPTY_BLOCK && enemy.getFakeX()+1 != lastXPosition) {
                enemy.move(Direction.RIGHT);
            } else if ( GameData.MAP_DATA[ enemy.getFakeY() ][ enemy.getFakeX() -1 ] == GameData.EMPTY_BLOCK && enemy.getFakeX()-1 != lastXPosition) {
                enemy.move(Direction.LEFT);
            } else {
                selectActionOnDownUp();
            }
        }
    }

    protected boolean selectActionOnDownUp() {
        x = GameData.getXPositionPlayerCharacter();
        y = GameData.getYPositionPlayerCharacter();
        if (enemy.getFakeY() - y > 0) {
            if ( GameData.MAP_DATA[ enemy.getFakeY() +1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() + 1 != lastYPosition) {
                enemy.move(Direction.DOWN);
                return true;
            }else if ( GameData.MAP_DATA[ enemy.getFakeY() -1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() - 1 != lastYPosition) {
                enemy.move(Direction.UP);
                return true;
            }else {
                return false;
            }

        } else if(enemy.getFakeY() - y < 0){
            if ( GameData.MAP_DATA[ enemy.getFakeY() -1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() - 1 != lastYPosition) {
                enemy.move(Direction.UP);
                return true;
            } else if ( GameData.MAP_DATA[ enemy.getFakeY() +1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() + 1 != lastYPosition) {
                enemy.move(Direction.DOWN);
                return true;
            }else {
                return false;
            }
        }else {
            if ( GameData.MAP_DATA[ enemy.getFakeY() +1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() + 1 != lastYPosition) {
                enemy.move(Direction.DOWN);
                return true;
            }else if ( GameData.MAP_DATA[ enemy.getFakeY() -1 ][ enemy.getFakeX() ] == GameData.EMPTY_BLOCK && enemy.getFakeY() - 1 != lastYPosition) {
                enemy.move(Direction.UP);
                return true;
            }
        }
        return false;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLastXPosition(int lastXPosition) {
        this.lastXPosition = lastXPosition;
    }

    public void setLastYPosition(int lastYPosition) {
        this.lastYPosition = lastYPosition;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLastXPosition() {
        return lastXPosition;
    }

    public int getLastYPosition() {
        return lastYPosition;
    }
}

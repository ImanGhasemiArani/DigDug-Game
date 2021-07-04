package ir.ac.kntu.controller;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.enemy.DeadlyEnemy;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AIDeadlyEnemy implements AI{

    private final DeadlyEnemy enemy;
    private int x;
    private int y;
    private int lastXPosition;
    private int lastYPosition;
    private boolean fire;

    public AIDeadlyEnemy(DeadlyEnemy enemy) {
        this.enemy = enemy;
        lastXPosition = enemy.getFakeX();
        lastYPosition = enemy.getFakeY();
        fire = false;
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
                    enemy.remove();
                    enemy.getAiLoop().stop();
                }));
                timeline.play();
            } else if (!fire || !selectFire() ) {
                fire = true;
                selectActionOnLeftRight();
                lastXPosition = enemy.getFakeX();
                lastYPosition = enemy.getFakeY();
            }
        });
        thread.start();
    }

    private boolean selectFire() {
        if ((enemy.getFakeX() - x <= 3 && enemy.getFakeX() - x >= -3) && (enemy.getFakeY() - y <= 3 && enemy.getFakeY() - y >= -3)) {
            if (GameData.MAP_DATA[enemy.getFakeY()][enemy.getFakeX() +1] == GameData.PLAYER_CHARACTER) {
                new Timeline(new KeyFrame(Duration.ONE,e-> enemy.fire(Direction.RIGHT))).play();
            } else if (GameData.MAP_DATA[enemy.getFakeY()][enemy.getFakeX() -1] == GameData.PLAYER_CHARACTER) {
                new Timeline(new KeyFrame(Duration.ONE,e-> enemy.fire(Direction.LEFT))).play();
            } else if (GameData.MAP_DATA[enemy.getFakeY() -1][enemy.getFakeX()] == GameData.PLAYER_CHARACTER) {
                new Timeline(new KeyFrame(Duration.ONE,e-> enemy.fire(Direction.UP))).play();
            } else if (GameData.MAP_DATA[enemy.getFakeY() +1][enemy.getFakeX()] == GameData.PLAYER_CHARACTER) {
                new Timeline(new KeyFrame(Duration.ONE,e-> enemy.fire(Direction.DOWN))).play();
            }else {
                new Timeline(new KeyFrame(Duration.ONE,e-> enemy.fire(Direction.RIGHT))).play();
            }
            fire = false;
            return true;
        }else {
            return false;
        }
    }

    private void selectActionOnLeftRight() {
        if (enemy.getFakeX() - x > 0) {
            if ( GameData.MAP_DATA[ enemy.getFakeY() ][ enemy.getFakeX() +1 ] == GameData.EMPTY_BLOCK && enemy.getFakeX()+1 != lastXPosition) {
                enemy.move(Direction.RIGHT);
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

    private boolean selectActionOnDownUp() {
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

}

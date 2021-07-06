package ir.ac.kntu.controller.ai;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.enemy.DeadlyEnemy;
import ir.ac.kntu.gameobjects.enemy.Enemy;
import ir.ac.kntu.model.Direction;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AIDeadlyEnemy extends AI {

    private final DeadlyEnemy enemy;
    private boolean fire;

    public AIDeadlyEnemy(Enemy enemy) {
        super(enemy);
        this.enemy = (DeadlyEnemy) enemy;
        fire = false;
    }

    @Override
    public void startAI() {
        Thread thread = new Thread(()-> {
            setX(GameData.getXPositionPlayerCharacter());
            setY(GameData.getYPositionPlayerCharacter());
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
            } else if (!fire || !selectFire() ) {
                fire = true;
                selectActionOnLeftRight();
                setLastXPosition(enemy.getFakeX());
                setLastYPosition(enemy.getFakeY());
            }
        });
        thread.start();
    }

    private boolean selectFire() {
        if ((enemy.getFakeX() - getX() <= 3 && enemy.getFakeX() - getX() >= -3) && (enemy.getFakeY() - getY() <= 3 && enemy.getFakeY() - getY() >= -3)) {
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

}

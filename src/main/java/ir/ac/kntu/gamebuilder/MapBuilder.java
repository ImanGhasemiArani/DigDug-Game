package ir.ac.kntu.gamebuilder;

import com.sun.javafx.collections.MappingChange;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.*;
import ir.ac.kntu.gameobjects.enemy.DeadlyEnemy;
import ir.ac.kntu.gameobjects.enemy.SimpleEnemy;
import javafx.scene.Group;

import java.util.Map;


public class MapBuilder {

    private Group group;
    private PlayerCharacter playerCharacter;

    public MapBuilder(int[][] mapData) {
        GameData.assignCurrentMapData(mapData);
        group = new Group();
        group.setLayoutX(GameData.START_X_GAME_ACTION_ARIA);
        group.setLayoutY(GameData.START_Y_GAME_ACTION_ARIA);
        createObjects();
        createObjects2();
        createObjects3();
        playerCharacter = new PlayerCharacter(GameData.getXPositionPlayerCharacter(),GameData.getYPositionPlayerCharacter());
        group.getChildren().add(playerCharacter);
    }

    private void createObjects() {
        for (int i = 0; i < GameData.MAP_DATA.length; i++) {
            for (int j = 0; j < GameData.MAP_DATA[0].length; j++) {
                switch (GameData.MAP_DATA[i][j]) {
                    case GameData.STONE:
                    case GameData.BLOCK:
                        group.getChildren().add(new Block(j,i));
                        break;
                    case GameData.PLAYER_CHARACTER:
                        GameData.setPositionXYPLayerCharacter(j,i);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void createObjects2() {
        for (int i = 0; i < GameData.MAP_DATA.length; i++) {
            for (int j = 0; j < GameData.MAP_DATA[0].length; j++) {
                switch (GameData.MAP_DATA[i][j]) {
                    case GameData.STONE:
                        group.getChildren().add(new Stone(j,i));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void createObjects3() {
        for (int i = 0; i < GameData.MAP_DATA.length; i++) {
            for (int j = 0; j < GameData.MAP_DATA[0].length; j++) {
                switch (GameData.MAP_DATA[i][j]) {
                    case GameData.Enemy_simple:
                        group.getChildren().add(new SimpleEnemy(j,i));
                        break;
                    case GameData.Enemy_DEADLY:
                        group.getChildren().add(new DeadlyEnemy(j,i));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public Group getGroup() {
        return group;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }
}

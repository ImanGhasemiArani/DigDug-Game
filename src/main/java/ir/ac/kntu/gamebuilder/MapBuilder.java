package ir.ac.kntu.gamebuilder;

import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.Block;
import ir.ac.kntu.gameobjects.PlayerCharacter;
import ir.ac.kntu.gameobjects.Stone;
import javafx.scene.Group;

public class MapBuilder {

    private Group group;
    private PlayerCharacter playerCharacter;
    private int xP;
    private int yP;

    public MapBuilder(int[][] mapData) {
        GameData.assignCurrentMapData(mapData);
        group = new Group();
        group.setLayoutX(GameData.START_X_GAME_ACTION_ARIA);
        group.setLayoutY(GameData.START_Y_GAME_ACTION_ARIA);
        createObjects();
        createObjects2();
        playerCharacter = new PlayerCharacter(xP,yP);
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
                        xP = j;
                        yP = i;
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

    public Group getGroup() {
        return group;
    }

    public PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }
}

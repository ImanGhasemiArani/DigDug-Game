package ir.ac.kntu.gamebuilder;

import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.Block;
import ir.ac.kntu.gameobjects.PlayerCharacter;
import javafx.scene.Group;

public class MapBuilder {

    private Group group;
    private PlayerCharacter playerCharacter;

    public MapBuilder(int[][] mapData) {
        GameData.assignCurrentMapData(mapData);
        group = new Group();
        group.setLayoutX(GameData.START_X_GAME_ACTION_ARIA);
        group.setLayoutY(GameData.START_Y_GAME_ACTION_ARIA);
        createObjects();
    }

    private void createObjects() {
        for (int i = 0; i < GameData.MAP_DATA.length; i++) {
            for (int j = 0; j < GameData.MAP_DATA[0].length; j++) {
                switch (GameData.MAP_DATA[i][j]) {
                    case GameData.BLOCK:
                        group.getChildren().add(new Block(j,i));
                        break;
                    case GameData.PLAYER_CHARACTER:
                        playerCharacter = new PlayerCharacter(j,i);
                        group.getChildren().add(playerCharacter);
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

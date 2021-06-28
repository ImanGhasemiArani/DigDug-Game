package ir.ac.kntu.gamebuilder;

import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.Block;
import ir.ac.kntu.gameobjects.PlayerCharacter;
import javafx.scene.Group;

public class MapBuilder {

    private int[][] mapData;

    private Group group;

    private PlayerCharacter playerCharacter;

    public MapBuilder(int[][] mapData) {
        this.mapData = mapData;
        group = new Group();
        group.setLayoutX(GameData.START_X_GAME_ACTION_ARIA);
        group.setLayoutY(GameData.START_Y_GAME_ACTION_ARIA);
        createObjects();
    }

    private void createObjects() {
        for (int i = 0; i < mapData.length; i++) {
            for (int j = 0; j < mapData[0].length; j++) {
                switch (mapData[i][j]) {
                    case 1:
                        group.getChildren().add(new Block(j,i));
                        break;
                    case 2:
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

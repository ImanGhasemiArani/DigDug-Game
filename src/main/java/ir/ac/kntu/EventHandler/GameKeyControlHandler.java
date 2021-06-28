package ir.ac.kntu.EventHandler;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.Direction;
import javafx.scene.input.KeyCode;

public class GameKeyControlHandler {
    private static GameKeyControlHandler instance = new GameKeyControlHandler();

    public static GameKeyControlHandler getInstance() {
        return instance;
    }

    private GameKeyControlHandler() {}

    public void attachEventHandlers(){
        GameStarter.SCENE.setOnKeyPressed(k -> {
            if(k.getCode().equals(KeyCode.ESCAPE) && GameData.isStopControl()) {
                GameStarter.showOrRemoveStopMenu();
            }else if(GameData.isGameControl() && k.getCode().equals(KeyCode.D)) {
                GameAriaBuilder.getPlayerCharacter().digging();
            }else if (GameData.isGameControl()) {
                GameAriaBuilder.getPlayerCharacter().move(Direction.DOWN.valueOfCustomize(k.getCode().toString()));
            }
        });
//        GameStarter.scene.setOnKeyReleased(k -> {
//            if (k.getCode().equals(KeyCode.UP) || k.getCode().equals(KeyCode.DOWN) || k.getCode().equals(KeyCode.LEFT) || k.getCode().equals(KeyCode.RIGHT) ) {
//                GameAriaBuilder.getPlayerCharacter().stopMove();
//            }
//        });
    }

}

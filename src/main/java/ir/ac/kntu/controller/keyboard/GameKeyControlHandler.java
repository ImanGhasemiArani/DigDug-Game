package ir.ac.kntu.controller.keyboard;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gameBuilder.GameAriaBuilder;
import ir.ac.kntu.gameData.GameData;
import ir.ac.kntu.model.Direction;
import javafx.scene.input.KeyCode;

public class GameKeyControlHandler {
    private static final GameKeyControlHandler INSTANCE = new GameKeyControlHandler();

    public static GameKeyControlHandler getInstance() {
        return INSTANCE;
    }

    private GameKeyControlHandler() {}

    public void attachEventHandlers(){
        GameStarter.SCENE.setOnKeyPressed(k -> {
            if(k.getCode().equals(KeyCode.ESCAPE) && GameData.isStopControl()) {
                GameStarter.showOrRemoveStopMenu();
            }else if (GameData.isGameControl() && GameData.isShootControl() && k.getCode().equals(KeyCode.SPACE)) {
                GameAriaBuilder.getPlayerCharacter().shoot();
            }else if(GameData.isGameControl()) {
                GameAriaBuilder.getPlayerCharacter().move(Direction.DOWN.valueOfCustomize(k.getCode().toString()));
            }
            if (k.getCode().equals(KeyCode.F3)) {
                GameAriaBuilder.cheatLevel();
            }
            if (k.getCode().equals(KeyCode.A)) {
                GameData.printGameDate();
                System.out.println("\n\n\n\n");
            }
        });
    }

}

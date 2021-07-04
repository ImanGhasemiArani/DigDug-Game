package ir.ac.kntu.controller;

import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
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
            }else if (GameData.isGameControl() && GameData.isShootControl() && k.getCode().equals(KeyCode.A)) {
                GameAriaBuilder.getPlayerCharacter().shoot();
            }else if(GameData.isGameControl()) {
                GameAriaBuilder.getPlayerCharacter().move(Direction.DOWN.valueOfCustomize(k.getCode().toString()));
            }
            if(k.getCode().equals(KeyCode.SPACE)) {
                GameData.printGameDate();
                System.out.println("\n\n\n\n");
            }
            if(k.getCode().equals(KeyCode.D)) {
                System.out.println(GameAriaBuilder.getCurrentPlayer().getHealth());
            }
        });
    }

}

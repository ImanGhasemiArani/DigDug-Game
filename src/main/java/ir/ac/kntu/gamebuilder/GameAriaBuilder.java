package ir.ac.kntu.gamebuilder;

import ir.ac.kntu.controller.keyboard.GameKeyControlHandler;
import ir.ac.kntu.GameStarter;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.gameobjects.*;
import ir.ac.kntu.gameobjects.randomObject.HeartRandom;
import ir.ac.kntu.gameobjects.randomObject.MushroomRandom;
import ir.ac.kntu.gameobjects.randomObject.SniperRandom;
import ir.ac.kntu.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.scene.control.Label;

import java.util.Random;


public class GameAriaBuilder {

    private static Player currentPlayer = null;
    private final StackPane gameAria;
    private static MapBuilder mapBuilder;
    private static AnchorPane gameMap;
    private final HBox upHBox;
    private final VBox gameInformationAria;
    private Label timer;
    private static PlayerCharacter playerCharacter;
    private static Label score;
    private static VBox healthBar;
    private static Label roundLabel;
    private final Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.01), e-> {
        timer.setText(String.format("%.2f",(Double.parseDouble(timer.getText()) + 0.01)));
        currentPlayer.setTime(Double.parseDouble(timer.getText()));
    }));
    private final Timeline randomTask = new Timeline(new KeyFrame(Duration.seconds(15),e-> {
        int x;
        int y;
        do {
            x = new Random().nextInt(GameData.MAP_DATA.length);
            y = new Random().nextInt(GameData.MAP_DATA[0].length);
        }while (GameData.MAP_DATA[y][x] != GameData.EMPTY_BLOCK);
        switch (new Random().nextInt(3)) {
            case 0:
                gameMap.getChildren().add(new HeartRandom(x,y));
                break;
            case 1:
                gameMap.getChildren().add(new MushroomRandom(x,y));
                break;
            case 2:
                gameMap.getChildren().add(new SniperRandom(x,y));
            default:
                break;
        }
    }));

    public GameAriaBuilder(Player player) {
        currentPlayer = player;
        gameAria = new StackPane();
        gameAria.setMinSize(1000,850);
        gameAria.setMaxSize(1000,850);
        gameMap = new AnchorPane();
        gameMap.setMinSize(GameData.calculateRealXY(GameData.SIZE_OF_GAME_ACTION_ARIA),GameData.calculateRealXY(GameData.SIZE_OF_GAME_ACTION_ARIA));
        gameMap.setMaxSize(GameData.calculateRealXY(GameData.SIZE_OF_GAME_ACTION_ARIA),GameData.calculateRealXY(GameData.SIZE_OF_GAME_ACTION_ARIA));
        upHBox = new HBox();
        upHBox.setAlignment(Pos.TOP_CENTER);
        gameInformationAria = new VBox();
        gameInformationAria.setAlignment(Pos.TOP_RIGHT);
        gameInformationAria.setPadding(new Insets(15,0,0,0));

        gameAria.getChildren().addAll(upHBox,gameInformationAria,gameMap);

        addObjectToUpHBox();
        addObjectToGameMap();
        addObjectToGameInformationAria();
    }

    private static void addObjectToGameMap() {
        gameMap.getChildren().clear();
        mapBuilder = new MapBuilder(currentPlayer.getLastSavedMapData());
        gameMap.getChildren().addAll(mapBuilder.getGroup().getChildren());
        playerCharacter = mapBuilder.getPlayerCharacter();
        GameKeyControlHandler.getInstance().attachEventHandlers();
    }

    private void addObjectToUpHBox() {
        ImageView gameLogo = new ImageView(new Image("assets/digDugText.png"));
        Label timeLabel = new Label("Time: ");
        timeLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 22px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        timeLabel.setPrefHeight(60);
        timer = new Label("" + currentPlayer.getTime());
        timer.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 22px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        timer.setPrefHeight(60);
        timer.setPrefWidth(100);

        Label playerNameLabel = new Label("Player: ");
        playerNameLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 22px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        playerNameLabel.setPrefHeight(60);
        playerNameLabel.setPrefWidth(100);
        Label playerLabel = new Label(currentPlayer.getPlayerName());
        playerLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 22px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        playerLabel.setPrefHeight(60);
        playerLabel.setPrefWidth(250);
        upHBox.getChildren().addAll(gameLogo,timeLabel,timer,playerNameLabel,playerLabel);
    }

    private void addObjectToGameInformationAria() {
        ImageView stopMenuIcon = new ImageView(new Image("assets/menu.png"));
        stopMenuIcon.setFitWidth(40);
        stopMenuIcon.setFitHeight(40);
        Label highScoreLabel = new Label("High Score");
        highScoreLabel.setPrefWidth(130);
        highScoreLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 30px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        Label highScore = new Label("" + currentPlayer.getHighScore());
        highScore.setPrefWidth(120);
        highScore.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 35px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        VBox vBox = new VBox(highScoreLabel,highScore);
        vBox.setAlignment(Pos.TOP_RIGHT);
        vBox.setPadding(new Insets(100,0,100,0));

        Label scoreLabel = new Label("Score");
        scoreLabel.setPrefWidth(120);
        scoreLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 30px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        score = new Label("" + currentPlayer.getScore());
        GameData.resetScore(currentPlayer.getScore());
        score.setPrefWidth(120);
        score.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 35px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        VBox vBox1 = new VBox(scoreLabel,score);
        vBox1.setAlignment(Pos.TOP_RIGHT);

        healthBar = new VBox();
        healthBar.setAlignment(Pos.TOP_RIGHT);
        healthBar.setPadding(new Insets(100,25,100,0));
        showHealthOrUpdate();

        roundLabel = new Label("Round " + currentPlayer.getCurrentRound());
        roundLabel.setPrefWidth(120);
        roundLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        gameInformationAria.getChildren().addAll(stopMenuIcon,vBox,vBox1,healthBar,roundLabel);

        stopMenuIcon.setOnMouseEntered(e-> stopMenuIcon.setOpacity(0.6));
        stopMenuIcon.setOnMouseExited(e-> stopMenuIcon.setOpacity(1));
        stopMenuIcon.setOnMouseClicked(e-> {
            if (GameData.isStopControl()) {
                GameStarter.showOrRemoveStopMenu();
            }
        });

    }

    public static void showHealthOrUpdate() {
        healthBar.getChildren().clear();
        HBox temp = new HBox();
        healthBar.getChildren().add(temp);
        temp.setAlignment(Pos.TOP_RIGHT);
        for (int i = 0; i < currentPlayer.getHealth(); i++) {
            if (i % 3 == 0) {
                HBox hBox = new HBox();
                healthBar.getChildren().add(hBox);
                hBox.setAlignment(Pos.TOP_RIGHT);
                temp = hBox;
            }
            ImageView imageView = new ImageView(new Image("assets/heart.png"));
            temp.getChildren().add(imageView);
        }
    }

    public static void checkForNextLevel() {
        if (GameData.getNumberOfEnemy() == 0) {
            if (GameData.NUMBER_OF_LEVELS  == currentPlayer.getCurrentRound()) {
                GameData.gameWin();
                GameStarter.endGame();
            }else {
                playerCharacter.doneLevel();
                currentPlayer.setCurrentRound(currentPlayer.getCurrentRound() + 1);
                roundLabel.setText("Round " + currentPlayer.getCurrentRound());
                currentPlayer.nextLevelMap();
                addObjectToGameMap();
            }
        }
    }

    private Thread creatingThreadForRandomObject() {
        return new Thread( () -> {
            randomTask.setCycleCount(Timeline.INDEFINITE);
            randomTask.play();
        });
    }

    public void startThreadRandomObject() {
        creatingThreadForRandomObject().start();
    }

    public void stopRandomTask() {
        randomTask.stop();
    }

    public void resumeRandomTask() {
        randomTask.play();
    }

    private Thread threadForTimer() {
        return new Thread(() -> {
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        });
    }

    public void startThreadForTimer() {
        threadForTimer().start();
    }

    public void stopTimer() {
        timeline.stop();
    }

    public void resumeTimer() {
        timeline.play();
    }

    public static Label getScore() {
        return score;
    }

    public static PlayerCharacter getPlayerCharacter() {
        return playerCharacter;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public StackPane getGameAria() {
        return gameAria;
    }

    public static AnchorPane getGameMap() {
        return gameMap;
    }
}

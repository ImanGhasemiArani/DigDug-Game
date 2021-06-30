package ir.ac.kntu;

import ir.ac.kntu.audio.AudioBuilder;
import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gamedata.GameData;
import ir.ac.kntu.model.GameStatus;
import ir.ac.kntu.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameStarter extends Application {

    private final static Stage STAGE = new Stage();
    private final static StackPane MAIN = new StackPane();
    public final static Scene SCENE = new Scene(MAIN, 1000, 850, Color.rgb(0,0,0));
    private static Player player;
    private static StackPane game;
    private static GameAriaBuilder currentGameAriaBuilder;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage st){

        MAIN.setStyle("-fx-border-width: 0 0 5 0;");
        MAIN.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

//        STAGE.setFullScreen(true);
        STAGE.setResizable(false);
        STAGE.setFullScreenExitHint("");
        STAGE.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        STAGE.setTitle("DigDig");
        STAGE.setScene(SCENE);
//        stage.initStyle(StageStyle.UNDECORATED);

        GameData.readOrImportFileToPlayers();
        gameMenu();

//        continueGamePage();

        Player newPlayer = new Player("Iman");
        GameData.addPlayer(newPlayer);
        player = newPlayer;
        MAIN.getChildren().clear();
        creatingGameAria().start();

        STAGE.show();
    }

    public static void gameMenu() {

        MAIN.getChildren().clear();
        ImageView digDugLogo = new ImageView(new Image("assets/digDugText.png"));
        digDugLogo.setFitWidth(470);
        digDugLogo.setFitHeight(80);
        Label newPlayerLabel = new Label("New Player");
        newPlayerLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label continueLabel = new Label("Continue Game");
        continueLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label gameGuideLabel = new Label("Game Guide");
        gameGuideLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label exitLabel = new Label("Exit");
        exitLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        VBox options = new VBox(digDugLogo, newPlayerLabel, continueLabel, gameGuideLabel, exitLabel);
        options.setSpacing(20);
        options.setAlignment(Pos.CENTER);

        MAIN.getChildren().add(options);

        mouseEnterExitLabelOptionAction(newPlayerLabel);
        mouseEnterExitLabelOptionAction(continueLabel);
        mouseEnterExitLabelOptionAction(gameGuideLabel);
        mouseEnterExitLabelOptionAction(exitLabel);

        newPlayerLabel.setOnMouseClicked(e-> newPlayerPage());
        continueLabel.setOnMouseClicked(e-> continueGamePage());
        gameGuideLabel.setOnMouseClicked(e-> gameGuidePage());
        exitLabel.setOnMouseClicked(e-> STAGE.close());
    }

    private static void newPlayerPage() {
        MAIN.getChildren().clear();

        TextField playerNameTextField = new TextField();
        playerNameTextField.setPromptText("write your name");
        playerNameTextField.setMinSize(230,35);
        playerNameTextField.setStyle("-fx-text-inner-color: GREEN;-fx-border-color: GRAY;-fx-border-width: 0 0 2 0;-fx-background-color: BLACK;-fx-padding: 5 0 0 5;");
        playerNameTextField.setFont(Font.font("Evil Empire", FontWeight.BOLD,20));


        Label playerNameLabel = new Label("Player's Name:");
        playerNameLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 20px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        HBox hBox = new HBox(playerNameLabel,playerNameTextField);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(20);

        Label startLabel = new Label("Start");
        startLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label backLabel = new Label("Back");
        backLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        mouseEnterExitLabelOptionAction(startLabel);
        mouseEnterExitLabelOptionAction(backLabel);

        VBox vBox = new VBox(hBox, startLabel, backLabel);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);

        MAIN.getChildren().add(vBox);

        backLabel.setOnMouseClicked(e-> gameMenu());

        startLabel.setOnMouseClicked(e -> {
            if (!playerNameTextField.getText().trim().equals("")) {
                player = new Player(playerNameTextField.getText().trim());
                GameData.addPlayer(player);
                MAIN.getChildren().clear();
                playCountDownTimer();
                creatingGameAria().start();

            }
        });

    }

    private static Thread creatingGameAria() {
        return new Thread( () -> {
            currentGameAriaBuilder = new GameAriaBuilder(player);
            GameData.gameControlOff();
            GameData.stopControlOff();
            game = new StackPane(currentGameAriaBuilder.getGameAria());
            game.setOpacity(0.1);

            Timeline helper = new Timeline(new KeyFrame(Duration.seconds(6),e->{
                game.setOpacity(1);
                GameData.runGame();
                GameData.gameControlOn();
                GameData.stopControlOn();
                currentGameAriaBuilder.startThreadForTimer();
                currentGameAriaBuilder.startThreadRandomObject();
            }));
            Timeline mainLine = new Timeline(new KeyFrame(Duration.ONE,e-> {
                MAIN.getChildren().add(game);
                helper.play();
            }));

            mainLine.play();
        });
    }

    private static void playCountDownTimer() {
        Arc arcOuter = new Arc();
        arcOuter.setRadiusX(150);
        arcOuter.setRadiusY(150);
        arcOuter.setStartAngle(90);
        arcOuter.setLength(0);
        arcOuter.setType(ArcType.ROUND);
        arcOuter.setFill(Color.GOLD);
        Arc arcInner = new Arc();
        arcInner.setRadiusX(147);
        arcInner.setRadiusY(147);
        arcInner.setLength(360);
        arcInner.setType(ArcType.ROUND);
        Group processRing = new Group(arcOuter,arcInner);
        processRing.minHeight(200);
        processRing.minWidth(200);
        MAIN.getChildren().add(processRing);
        Label countDownNum = new Label();
        countDownNum.setStyle("-fx-text-fill: GOLD;-fx-font-size: 150px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        MAIN.getChildren().add(countDownNum);
        final int[] i = {6};
        Timeline helper = new Timeline(new KeyFrame(Duration.seconds(1),e-> {
            AudioBuilder.playCountDownTimerAudio();
            MAIN.getChildren().removeAll(processRing,countDownNum);
        }));
        helper.setCycleCount(1);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000/360),e-> arcOuter.setLength(arcOuter.getLength() + 1)));
        timeline.setCycleCount(360);
        Timeline countDown = new Timeline(new KeyFrame(Duration.seconds(1),e->{
            AudioBuilder.playCountDownTimerAudio();
            i[0]--;
            if (i[0] == 3) {
                arcOuter.setFill(Color.RED);
                countDownNum.setTextFill(Color.RED);
            }
            arcOuter.setLength(0);
            timeline.play();
            countDownNum.setText("" + i[0]);
            if (i[0] == 1) {
                helper.play();
            }
        }));
        countDown.setCycleCount(5);
        countDown.play();
    }

    private static void continueGamePage() {
        MAIN.getChildren().clear();
        TableView<Player> tableView = createTableList();
        Label selectedPlayer = new Label("Selected Player: ");
        selectedPlayer.setStyle("-fx-text-fill: Wheat;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        selectedPlayer.setMinWidth(562);
        Label backLabel = new Label("Back");
        backLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        Label newGameLabel = new Label("New Game");
        newGameLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        Label continueGameLabel = new Label("Continue");
        continueGameLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        HBox hBox = new HBox(backLabel,newGameLabel,continueGameLabel);
        hBox.setSpacing(165);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        VBox vBox = new VBox(tableView,selectedPlayer,hBox);
        vBox.setMinSize(622,600);
        vBox.setMaxSize(622,600);
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        MAIN.getChildren().add(vBox);
        newGameLabel.setDisable(true);
        continueGameLabel.setDisable(true);
        tableView.setOnMouseClicked(e->{
            if (tableView.getSelectionModel().getSelectedItem() != null)  {
                selectedPlayer.setText("Selected Player:\t\t" + tableView.getSelectionModel().getSelectedItem().getPlayerName());
                newGameLabel.setDisable(false);
                continueGameLabel.setDisable(false);
            }
        });
        newGameLabel.setOnMouseClicked(e-> {
            player = tableView.getSelectionModel().getSelectedItem();
            player.assignNewGame();
            MAIN.getChildren().clear();
            playCountDownTimer();
            creatingGameAria().start();
        });
        continueGameLabel.setOnMouseClicked(e-> {
            player = tableView.getSelectionModel().getSelectedItem();
            MAIN.getChildren().clear();
            playCountDownTimer();
            creatingGameAria().start();
        });
        mouseEnterExitLabelOptionAction(backLabel);
        mouseEnterExitLabelOptionAction(newGameLabel);
        mouseEnterExitLabelOptionAction(continueGameLabel);
        backLabel.setOnMouseClicked(e-> gameMenu());
    }

    private static TableView<Player> createTableList() {
        TableView<Player> playersList = new TableView<>();
        playersList.setEditable(false);
        Label placeholderLabel = new Label("No Player");
        placeholderLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 20px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        playersList.setPlaceholder(placeholderLabel);
        playersList.setMinSize(622,500);
        playersList.setMaxSize(622,500);
        playersList.setStyle("-fx-background-radius: 50px;-fx-background-color: GRAY;");

        TableColumn<Player,String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        rankColumn.setReorderable(false);
        rankColumn.setSortable(false);
        rankColumn.setStyle("-fx-border-width: 0px;-fx-background-color: Black;-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        rankColumn.setMaxWidth(60);
        rankColumn.setMinWidth(60);

        TableColumn<Player,String> playerNameColumn = new TableColumn<>("Player's Name");
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<>("playerName"));
        playerNameColumn.setReorderable(false);
        playerNameColumn.setSortable(false);
        playerNameColumn.setStyle("-fx-border-width: 0px;-fx-background-color: Black;-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        playerNameColumn.setMaxWidth(300);
        playerNameColumn.setMinWidth(300);

        TableColumn<Player,String> highScore = new TableColumn<>("High Score");
        highScore.setCellValueFactory(new PropertyValueFactory<>("highScore"));
        highScore.setReorderable(false);
        highScore.setSortable(false);
        highScore.setStyle("-fx-border-width: 0px;-fx-background-color: Black;-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        highScore.setMaxWidth(200);
        highScore.setMinWidth(200);

        TableColumn<Player,String> countColumn = new TableColumn<>("NoG");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfGames"));
        countColumn.setReorderable(false);
        countColumn.setSortable(false);
        countColumn.setStyle("-fx-border-width: 0px;-fx-background-color: Black;-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        countColumn.setMaxWidth(60);
        countColumn.setMinWidth(60);

        playersList.getColumns().addAll(rankColumn,playerNameColumn,highScore,countColumn);
        playersList.getItems().addAll(GameData.players());

        return playersList;
    }

    private static void gameGuidePage() {
        MAIN.getChildren().clear();
        Label movementLabel = new Label("Movement");
        movementLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        ImageView arrowsKey = new ImageView(new Image("assets/arrowsKey.png"));
        arrowsKey.setFitWidth(250);
        arrowsKey.setFitHeight(170);
        VBox movement = new VBox(movementLabel,arrowsKey);
        movement.setSpacing(20);
        Label shootLabel = new Label("Shoot");
        shootLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        ImageView aKey = new ImageView(new Image("assets/aKey.png"));
        aKey.setFitWidth(80);
        aKey.setFitHeight(81);
        VBox shoot = new VBox(shootLabel,aKey);
        shoot.setSpacing(20);
        Label digLabel = new Label("Dig");
        digLabel.setStyle("-fx-text-fill: WHEAT;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        ImageView dKey = new ImageView(new Image("assets/dKey.png"));
        dKey.setFitWidth(80);
        dKey.setFitHeight(84);
        VBox dig = new VBox(digLabel,dKey);
        dig.setSpacing(20);
        Label backLabel = new Label("Back");
        backLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");
        HBox hBox = new HBox(movement,shoot,dig);
        hBox.setSpacing(50);
        movement.setAlignment(Pos.CENTER);
        shoot.setAlignment(Pos.CENTER);
        dig.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER);
        VBox vBox = new VBox(hBox,backLabel);
        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);

        mouseEnterExitLabelOptionAction(backLabel);

        backLabel.setOnMouseClicked(e-> gameMenu());

        MAIN.getChildren().addAll(vBox);
    }

    private static void stopMenu() {
        StackPane stopMenu = new StackPane();
        Label resumeLabel = new Label("Resume Game");
        resumeLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label exitToMainMenuLabel = new Label("Save & Exit To Main Menu");
        exitToMainMenuLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        Label exitLabel = new Label("Save & Exit");
        exitLabel.setStyle("-fx-text-fill: RED;-fx-font-size: 25px;-fx-font-family: 'Evil Empire';-fx-font-weight: BOLD;");

        VBox options = new VBox(resumeLabel, exitToMainMenuLabel, exitLabel);
        options.setSpacing(20);
        options.setAlignment(Pos.CENTER);

        stopMenu.getChildren().add(options);

        mouseEnterExitLabelOptionAction(resumeLabel);
        mouseEnterExitLabelOptionAction(exitToMainMenuLabel);
        mouseEnterExitLabelOptionAction(exitLabel);

        resumeLabel.setOnMouseClicked(e-> showOrRemoveStopMenu());
        exitToMainMenuLabel.setOnMouseClicked(e-> {
            saveGame();
            MAIN.getChildren().clear();
            gameMenu();

        });
        exitLabel.setOnMouseClicked(e-> {
            saveGame();
            STAGE.close();
        });


        MAIN.getChildren().add(stopMenu);
    }

    private static void saveGame() {
        currentGameAriaBuilder.getCurrentPlayer().updateHighScore(GameData.getCurrentScore());
        currentGameAriaBuilder.getCurrentPlayer().updateScore();
        currentGameAriaBuilder.getCurrentPlayer().saveOrUpdateLastSavedData();
        GameData.saveOrUpdatePlayersToFile();
    }

    private static void stopGame() {
        GameData.stopGame();
        currentGameAriaBuilder.stopTimer();
        currentGameAriaBuilder.stopRandomTask();
        game.setOpacity(0.1);
    }

    private static void resumeGame() {
        GameData.runGame();
        currentGameAriaBuilder.resumeTimer();
        currentGameAriaBuilder.resumeRandomTask();
        game.setOpacity(1);
    }

    public static void showOrRemoveStopMenu() {
        if (GameData.gameStatus().equals(GameStatus.STOP)) {
            MAIN.getChildren().remove(MAIN.getChildren().size()-1);
            resumeGame();
        } else {
            stopGame();
            stopMenu();
        }
    }

    private static void mouseEnterExitLabelOptionAction(Label label) {
        label.setOnMouseEntered(e-> {
            AudioBuilder.playSelectingOptionAudio();
            label.setTextFill(Color.WHEAT);
        });
        label.setOnMouseExited(e-> label.setTextFill(Color.RED));

    }

    public static Stage getStage() {
        return STAGE;
    }

    public static StackPane getMain() {
        return MAIN;
    }

    public static StackPane getGame() {
        return game;
    }
}

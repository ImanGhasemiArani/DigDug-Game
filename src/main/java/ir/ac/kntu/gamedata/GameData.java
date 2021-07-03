package ir.ac.kntu.gamedata;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gameobjects.Block;
import ir.ac.kntu.gameobjects.enemy.SimpleEnemy;
import ir.ac.kntu.gameobjects.randomObject.RandomObject;
import ir.ac.kntu.model.GameStatus;
import ir.ac.kntu.model.Player;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class GameData {

    public static final int[][] MAP_1 = importMap("src/main/resources/maps/map_1.txt");
    private static ArrayList<Player> players = new ArrayList<>();
    private static GameStatus gameStatus = GameStatus.STOP;
    private static boolean gameControl = false;
    private static boolean shootControl = true;
    private static boolean stopControl = false;
    private static boolean aiControl = false;
    private static int currentScore = 0;
    public final static int FIRST_HEALTH_OF_PLAYER = 3;
    public final static int NUMBER_OF_LEVELS = 3;
    public final static int EMPTY_BLOCK = 0;
    public final static int BLOCK = 1;
    public final static int PLAYER_CHARACTER = 2;
    public final static int STONE = 3;
    public final static int HEART = 4;
    public final static int MUSHROOM = 5;
    public final static int SNIPER = 6;
    public final static int Enemy_simple = 7;
    public final static int Enemy_DEADLY = 8;
    public final static int GAP = 40;
    public final static int SIZE_OF_GAME_ACTION_ARIA = 18;
    public final static int REAL_SIZE_OF_GAME_ACTION_ARIA = SIZE_OF_GAME_ACTION_ARIA * GAP;
    public final static int START_X_GAME_ACTION_ARIA = 30;
    public final static int START_Y_GAME_ACTION_ARIA = 80;
    public final static int END_X_GAME_ACTION_ARIA = REAL_SIZE_OF_GAME_ACTION_ARIA;
    public final static int END_Y_GAME_ACTION_ARIA = REAL_SIZE_OF_GAME_ACTION_ARIA;
    public final static int[][] MAP_DATA = new int[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static Block[][] BLOCKS = new Block[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static RandomObject[][] NOT_MOVING_GAME_OBJECTS = new RandomObject[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static SimpleEnemy[][] ENEMIES = new SimpleEnemy[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    private static int xPositionPlayerCharacter;
    private static int yPositionPlayerCharacter;

    public static void setPositionXYPLayerCharacter(int x, int y) {
        xPositionPlayerCharacter = x;
        yPositionPlayerCharacter = y;
    }

    public static int getXPositionPlayerCharacter() {
        return xPositionPlayerCharacter;
    }

    public static int getYPositionPlayerCharacter() {
        return yPositionPlayerCharacter;
    }

    public static int calculateRealXY(int fake) {
        return fake * GAP;
    }

    public static ArrayList<Player> players() {
        return (ArrayList<Player>) sortPlayers().clone();
    }

    public static void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public static void gameControlOn() {
        gameControl = true;
    }

    public static void gameControlOff() {
        gameControl = false;
    }

    public static void aiControlOn() {
        aiControl = true;
    }

    public static void aiControlOff() {
        aiControl = false;
    }

    public static boolean isAiControl() {
        return aiControl;
    }

    public static Block getBlockInSpecificFakeXY(int x, int y) {
        return BLOCKS[x][y];
    }

    public static RandomObject getNotMovingGameObjectsInSpecificFakeXY(int x, int y) {
        return NOT_MOVING_GAME_OBJECTS[x][y];
    }

    public static void stopGame() {
        gameStatus = GameStatus.STOP;
        gameControlOff();
        aiControlOff();
    }

    public static void runGame() {
        gameStatus = GameStatus.RUNNING;
        gameControlOn();
        aiControlOn();
    }

    public static void gameOverGame() {
        gameStatus = GameStatus.GAMEOVER;
        gameControlOff();
    }

    public static GameStatus gameStatus() {
        return gameStatus;
    }

    public static boolean isGameControl() {
        return gameControl;
    }

    public static boolean isStopControl() {
        return stopControl;
    }

    public static void stopControlOn() {
        stopControl = true;
    }

    public static void stopControlOff() {
        stopControl = false;
    }

    public static void shootControlOn() {
        shootControl = true;
    }

    public static void shootControlOff() {
        shootControl = false;
    }

    public static boolean isShootControl() {
        return shootControl;
    }

    public static int getCurrentScore() {
        return currentScore;
    }

    public static void increaseScore(int value) {
        currentScore += value;
        GameAriaBuilder.getScore().setText("" + currentScore);
    }

    public static void resetScore(int value) {
        currentScore = value;
    }

    public static void assignCurrentMapData(int[][] mapData) {
        IntStream.range(0, mapData.length).forEach(i -> System.arraycopy(mapData[i], 0, MAP_DATA[i], 0, mapData[0].length));
    }

    private static int[][] importMap(String fileURLAddress) {
        int[][] map = new int[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
        try {
            File mapFile = new File(fileURLAddress);
            if (mapFile.exists()) {
                Scanner reader = new Scanner(mapFile);
                while (reader.hasNextInt()) {
                    for (int i = 0; i < map.length; i++) {
                        for (int j = 0; j < map[0].length; j++) {
                            map[i][j] = reader.nextInt();
                        }
                    }
                }
            }
        } catch (FileNotFoundException ignored) {
        }
        return map;
    }

    public static void printGameDate() {
        for (int i = 0; i < MAP_DATA.length; i++) {
            for (int j = 0; j < MAP_DATA[0].length; j++) {
                System.out.print(MAP_DATA[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void saveOrUpdatePlayersToFile() {
        File playersFile = new File("src/main/java/ir/ac/kntu/playersFile");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(playersFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(players);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void readOrImportFileToPlayers() {
        File playersFile = new File("src/main/java/ir/ac/kntu/playersFile");
        if (playersFile.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(playersFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                players = (ArrayList<Player>) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static ArrayList<Player> sortPlayers() {
        players.sort(Comparator.comparing(Player::getHighScore).reversed());
        for (Player p : players) {
            p.setRank(players.indexOf(p) +1);
        }
        return players;
    }

}

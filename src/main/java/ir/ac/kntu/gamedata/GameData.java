package ir.ac.kntu.gamedata;

import ir.ac.kntu.gamebuilder.GameAriaBuilder;
import ir.ac.kntu.gameobjects.Block;
import ir.ac.kntu.gameobjects.Stone;
import ir.ac.kntu.gameobjects.enemy.Enemy;
import ir.ac.kntu.gameobjects.randomObject.RandomObject;
import ir.ac.kntu.model.GameStatus;
import ir.ac.kntu.model.Player;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class GameData {

    private static boolean playSound = true;
    public static final int[][] MAP_1 = importMap("src/main/resources/maps/map_1.txt");
    public static final int[][] MAP_2 = importMap("src/main/resources/maps/map_2.txt");
    public static final int[][] MAP_3 = importMap("src/main/resources/maps/map_3.txt");
    public static final int[][] MAP_4 = importMap("src/main/resources/maps/map_4.txt");
    public static final int[][] MAP_5 = importMap("src/main/resources/maps/map_5.txt");
    public final static int NUMBER_OF_LEVELS = 5;
    private static ArrayList<Player> players = new ArrayList<>();
    private static GameStatus gameStatus = GameStatus.STOP;
    private static boolean gameControl = false;
    private static boolean shootControl = true;
    private static boolean stopControl = false;
    private static boolean aiControl = false;
    private static int currentScore = 0;
    private static int numberOfEnemy = 0;
    public final static int FIRST_HEALTH_OF_PLAYER = 3;
    public final static int EMPTY_BLOCK = 0;
    public final static int BLOCK = 1;
    public final static int PLAYER_CHARACTER = 2;
    public final static int STONE = 3;
    public final static int HEART = 4;
    public final static int MUSHROOM = 5;
    public final static int SNIPER = 6;
    public final static int ENEMY_SIMPLE = 7;
    public final static int ENEMY_DEADLY = 8;
    public final static int GAP = 40;
    public final static int SIZE_OF_GAME_ACTION_ARIA = 18;
    public final static int REAL_SIZE_OF_GAME_ACTION_ARIA = SIZE_OF_GAME_ACTION_ARIA * GAP;
    public final static int START_X_GAME_ACTION_ARIA = 30;
    public final static int START_Y_GAME_ACTION_ARIA = 80;
    public final static int END_X_GAME_ACTION_ARIA = REAL_SIZE_OF_GAME_ACTION_ARIA;
    public final static int END_Y_GAME_ACTION_ARIA = REAL_SIZE_OF_GAME_ACTION_ARIA;
    public final static int[][] MAP_DATA = new int[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static Block[][] BLOCKS = new Block[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static ArrayList<Stone> STONES = new ArrayList<>();
    public final static RandomObject[][] NOT_MOVING_GAME_OBJECTS = new RandomObject[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
    public final static Enemy[][] ENEMIES = new Enemy[SIZE_OF_GAME_ACTION_ARIA][SIZE_OF_GAME_ACTION_ARIA];
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

    public static boolean isPlaySound() {
        return playSound;
    }

    public static void setPlaySound(boolean playSound) {
        GameData.playSound = playSound;
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

    public static void increaseNumberOfEnemy() {
        numberOfEnemy++;
    }

    public static void decreaseNumberOfEnemy() {
        numberOfEnemy--;
    }

    public static int getNumberOfEnemy() {
        return numberOfEnemy;
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
        aiControlOff();
    }

    public static void gameWin() {
        gameStatus = GameStatus.WIN;
        gameControlOff();
        aiControlOff();
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
        if (currentScore + value >= 0) {
            currentScore += value;
            GameAriaBuilder.getScore().setText("" + currentScore);
        }
    }

    public static void resetScore(int value) {
        currentScore = value;
    }

    public static void assignCurrentMapData(int[][] mapData) {
        STONES.forEach(Stone::destroy);
        STONES.clear();
        IntStream.range(0, mapData.length).forEach(i -> System.arraycopy(mapData[i], 0, MAP_DATA[i], 0, mapData[0].length));
        for (int i = 0; i < BLOCKS.length; i++) {
            for (int j = 0; j < BLOCKS[0].length; j++) {
                BLOCKS[i][j] = null;
            }
        }
        for (int i = 0; i < NOT_MOVING_GAME_OBJECTS.length; i++) {
            for (int j = 0; j < NOT_MOVING_GAME_OBJECTS[0].length; j++) {
                NOT_MOVING_GAME_OBJECTS[i][j] = null;
            }
        }
        for (int i = 0; i < ENEMIES.length; i++) {
            for (int j = 0; j < ENEMIES[0].length; j++) {
                ENEMIES[i][j] = null;
            }
        }
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
        Arrays.stream(MAP_DATA).forEach(mapDatum -> {
            IntStream.range(0, MAP_DATA[0].length).mapToObj(j -> mapDatum[j] + " ").forEach(System.out::print);
            System.out.println();
        });
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

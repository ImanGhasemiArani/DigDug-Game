package ir.ac.kntu.model;

import ir.ac.kntu.gamedata.GameData;

import java.io.Serializable;
import java.util.stream.IntStream;

public class Player implements Serializable {

    private int rank;

    private final String playerName;

    private Integer highScore;

    private Integer score;

    private Double time;

    private int health;

    private int numberOfGames;

    private int[][] lastSavedMapData;

    private int currentRound;

    public Player(String playerName) {
        this.playerName = playerName;
        rank = GameData.players().size() + 1;
        highScore = 0;
        assignNewHealth();
        numberOfGames = 0;
        currentRound = 0;
        lastSavedMapData = new int[GameData.SIZE_OF_GAME_ACTION_ARIA][GameData.SIZE_OF_GAME_ACTION_ARIA];
        assignNewGame();
    }

    public void assignNewGame() {
        time = 0.0;
        score = 0;
        assignMap(GameData.MAP_1);
        currentRound = 1;
        assignNewHealth();
        increaseOneNumberOfGames();
    }

    private void assignMap(int[][] temp) {
        IntStream.range(0, lastSavedMapData.length).forEach(i -> System.arraycopy(temp[i], 0, lastSavedMapData[i], 0, lastSavedMapData[0].length));
    }

    public void updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
        }
    }

    public void updateScore() {
        score = GameData.getCurrentScore();
    }

    public void decreaseOneHealth() {
        health--;
    }

    public void increaseOneHealth() {
        health++;
    }

    public void assignNewHealth() {
        health = GameData.FIRST_HEALTH_OF_PLAYER;
    }

    private void increaseOneNumberOfGames() {
        numberOfGames++;
    }

    public void saveOrUpdateLastSavedData() {
        assignMap(GameData.MAP_DATA);
    }

    public void setLastSavedMapData(int[][] lastSavedMapData) {
        this.lastSavedMapData = lastSavedMapData;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Integer getHighScore() {
        return highScore;
    }

    public int getHealth() {
        return health;
    }

    public int getNumberOfGames() {
        return numberOfGames;
    }

    public int[][] getLastSavedMapData() {
        return lastSavedMapData;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }


}

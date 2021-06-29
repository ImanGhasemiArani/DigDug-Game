package ir.ac.kntu.model;

import ir.ac.kntu.gamedata.GameData;

import java.io.Serializable;

public class Player implements Serializable {

    private int rank;

    private final String playerName;

    private Integer highScore;

    private int health;

    private int numberOfGames;

    private int[][] lastSavedMapData;

    private int currentRound;

    public Player(String playerName) {
        this.playerName = playerName;
        rank = GameData.PLAYERS.size() + 1;
        highScore = 0;
        assignedNewHealth();
        numberOfGames = 0;
        currentRound = 1;
        lastSavedMapData = GameData.importMap("maps/map_1.txt");
    }

    public void updateHighScore(int newScore) {
        if (newScore > highScore) {
            highScore = newScore;
        }
    }

    public void decreaseOneHealth() {
        health--;
    }

    public void increaseOneHealth() {
        health++;
    }

    public void assignedNewHealth() {
        health = GameData.FIRST_HEALTH_OF_PLAYER;
    }

    public void increaseOneNumberOfGames() {
        numberOfGames++;
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
}

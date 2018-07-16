package com.appsinfinity.fingercricket.models;

/**
 * Created by macbook on 11/25/16.
 */

public class Game {
    Team teamA, teamB;
    int wicket, over, ball,target;
    boolean isFirstInning;
    Player player;
    boolean gameFinished;

    public boolean isGameFinished() {
        return gameFinished;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getBall() {
        return ball;
    }

    public void setBall(int ball,Game game) {
        if (this.ball == 5) {
            this.ball = 0;
            game.setOver(game.getOver()+1);
        }else{
            this.ball = ball;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isFirstInning() {
        return isFirstInning;
    }

    public void setFirstInning(boolean firstInning) {
        isFirstInning = firstInning;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }

    public int getWicket() {
        return wicket;
    }

    public void setWicket(int wicket) {
        this.wicket = wicket;
    }

    public int getOver() {
        return over;
    }

    public void setOver(int over) {
        this.over = over;
    }

    public void setBall(int ball) {
        this.ball = ball;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }
}

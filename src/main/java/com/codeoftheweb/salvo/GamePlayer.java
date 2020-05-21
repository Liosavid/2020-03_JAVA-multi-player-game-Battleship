package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long gamePlayerId;
    private Date joinDate;
    private GameStatus status;
    private boolean isFirst;




    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;


    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer (Game game, Player player){
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
    }

    public GamePlayer (Game game, Player player, boolean isFirst){
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
        this.isFirst = isFirst;
    }

    public GamePlayer() { }


    public GamePlayer(Optional<Game> currentGame, Player currentPlayer) { }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }


    public long getGamePlayerId() {
        return gamePlayerId;
    }

    public void setGamePlayerId(long gamePlayerId) {
        this.gamePlayerId = gamePlayerId;
    }

    public Player getPlayer() {
        return player;
    }


    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public enum GameStatus{
        WaitingForShips,
        WaitingForSecondPlayer,
        WaitingForSalvoes,
        WaitingForEnemy,
        GameOver
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }


    public Score getScore() {
        return player.getScore(game);
    }
}
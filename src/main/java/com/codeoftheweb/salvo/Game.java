package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long gameId;
    private Date gameCreationDate;


    public Date getGameCreationDate() {
        return gameCreationDate;
    }

    public void setGameCreationDate(Date gameCreationDate) {
        this.gameCreationDate = gameCreationDate;
    }


    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }


    public Game() {
        this.gameCreationDate = new Date();
    }





}
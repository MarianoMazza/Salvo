package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Score_Game_Id")
    private Game gameId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Score_Player_Id")
    private Player playerId;

    private float score;

    private LocalDateTime finishDate;

    public long getId() {
        return id;
    }

    public Game getGameId() {
        return gameId;
    }

    public void setGameId(Game gameId) {
        this.gameId = gameId;
    }

    public Player getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Player playerId) {
        this.playerId = playerId;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public LocalDateTime getCurrentDateGame() {
        return finishDate;
    }

    public void setCurrentDateGame(LocalDateTime currentDateGame) {
        this.finishDate = currentDateGame;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public Score() {
    }

    public Score(Game gameId, Player playerId, float score) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.score = score;
        this.finishDate = LocalDateTime.now();
    }

    public Map<String,Object> ToDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("finishDate", this.getCurrentDateGame());
        dto.put("score", this.getScore());
        dto.put("player", this.getPlayerId().getId());
        return dto;
    }
}

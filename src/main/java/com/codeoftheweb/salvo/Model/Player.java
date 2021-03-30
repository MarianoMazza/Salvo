package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String userName ;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<GamePlayer> gamePlayerSet;

    @OneToMany(mappedBy = "playerId", fetch = FetchType.EAGER)
    private Set<Score> score;

    public Player() { }

    public Player(String _userName) {
        userName = _userName;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Score> getScore() {
        return score;
    }

    public Optional<Score> getScore(Game game) {
        return getScore().stream().filter(score1 -> score1.getGameId().equals(game)).findFirst();
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }

    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public void setGamePlayerSet(Set<GamePlayer> gamePlayerSet) {
        this.gamePlayerSet = gamePlayerSet;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setPlayer(this);
        gamePlayerSet.add(gamePlayer);
    }

    public Map<String,Object> ToDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("mail", this.getUserName());
        return dto;
    }

   /*public Map<String,Object> ScorePlayerDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("total", TotalScore());
        dto.put("win", WinScore());
        dto.put("tie", TieScore());
        dto.put("lose", LoseScore());
        return dto;
    }*/
}
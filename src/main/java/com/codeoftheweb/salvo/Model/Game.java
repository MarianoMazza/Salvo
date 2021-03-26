package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime currentDateGame;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private Set<GamePlayer> players;

    public Game() { }

    public Game(LocalDateTime currentDate) {
        this.currentDateGame = currentDate;
    }

    public void setCurrentDate(LocalDateTime currentDate) {
        this.currentDateGame = currentDate;
    }

    public LocalDateTime getCurrentDate() {
        return currentDateGame;
    }

    public long getId() {
        return id;
    }

    public void addGamePlayer(GamePlayer gamePlayer){
        gamePlayer.setGame(this);
        players.add(gamePlayer);
    }

    public Map<String,Object> ToDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCurrentDate());
        List<Object> gamePlayersList = players.stream().map(a -> a.ToDTO()).collect(toList());
        dto.put("gamePlayers", gamePlayersList);
        return dto;
    }
}
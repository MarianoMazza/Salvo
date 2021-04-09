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

    @OneToMany(mappedBy = "gameId", fetch = FetchType.EAGER)
    private Set<Score> score;

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

    public Set<GamePlayer> getPlayers() {
        return players;
    }

    public void setPlayers(Set<GamePlayer> players) {
        this.players = players;
    }

    public Set<Score> getScore() {
        return score;
    }

    public void setScore(Set<Score> score) {
        this.score = score;
    }

    public Map<String,Object> ToDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("created", this.getCurrentDate());
        dto.put("gameState", "PLACESHIPS");
        List<Object> gamePlayersList = players.stream().map(player -> player.ToDTO()).collect(toList());
        dto.put("gamePlayers", gamePlayersList);
        if(getScore().stream().map(Score::ToDTO).collect(toList()) != null) {
            dto.put("scores", getScore().stream().map(score1 -> score1.ToDTO()).collect(toList()));
        }
        return dto;
    }

}
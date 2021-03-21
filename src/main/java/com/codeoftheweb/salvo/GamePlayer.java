package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    public GamePlayer() {
    }

    public GamePlayer(LocalDateTime currentDateGamePlayer, Game game, Player player) {
        this.creationDate = currentDateGamePlayer;
        this.game = game;
        this.player = player;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCurrentDateGamePlayer() {
        return creationDate;
    }

    public void setCurrentDateGamePlayer(LocalDateTime currentDateGamePlayer) {
        this.creationDate = currentDateGamePlayer;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<String,Object> ToDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        Map<String, Object> gamePlayersList = player.ToDTO();
        dto.put("player", gamePlayersList);
        return dto;
    }
}

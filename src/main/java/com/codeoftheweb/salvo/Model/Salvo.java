package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "cellsSalvo")
    private Set<String> salvoLocations;

    private int turn;

    public Salvo() {
    }

    @Override
    public String toString(){

        return "Estas son las locations" + this.salvoLocations + "el turno" + this.turn;
    }

    public Salvo(GamePlayer gamePlayer, Set<String> cellsSalvo, int turn) {
        this.gamePlayer = gamePlayer;
        this.salvoLocations = cellsSalvo;
        this.turn = turn;
    }

    public long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public Set<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(Set<String> locations) {
        this.salvoLocations = locations;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Map<String,Object> salvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("player", getGamePlayer().getPlayer().getId());
        dto.put("locations", getSalvoLocations());
        dto.put("turn", getTurn());
        return dto;
    }
}

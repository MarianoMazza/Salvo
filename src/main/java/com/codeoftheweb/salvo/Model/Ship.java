package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "cell")
    private Set<String> cell;

    private String shipType;

    public Ship() {
    }

    public Ship(Set<String> cell, String shipType, GamePlayer gamePlayer) {
        this.cell = cell;
        this.shipType = shipType;
        this.gamePlayer = gamePlayer;
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

    public Set<String> getCell() {
        return cell;
    }

    public void setCell(Set<String> cell) {
        this.cell = cell;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Map<String,Object> shipDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("type", getShipType());
        dto.put("locations", getCell());
        return dto;
    }
}

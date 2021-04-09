package com.codeoftheweb.salvo.Model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
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
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "locations")
    private Set<String> locations;

    private String type;

    public Ship() {
        locations = new HashSet<>();
    }

    @Override
    public String toString(){

        return "Estas son las locations" + this.locations + "el type" + this.type;
    }

    public Ship(Set<String> cell, String shipType, GamePlayer gamePlayer) {
        this.locations = cell;
        this.type = shipType;
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

    public String getType() {
        return type;
    }

    public void setType(String shipType) {
        this.type = shipType;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }

    public Map<String,Object> shipDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("type", getType());
        dto.put("locations", getLocations());
        return dto;
    }
}

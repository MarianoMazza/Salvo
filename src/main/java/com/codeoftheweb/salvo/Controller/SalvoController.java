package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Interface.*;
import com.codeoftheweb.salvo.Model.*;
import com.codeoftheweb.salvo.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerId, Authentication authentication) {
        int[] totalDamagesSelf = new int[6];
        int[] totalDamagesOpp = new int[6];
        if (Utils.isGuest(authentication)) {
            return Utils.ResponseWithMap("Problem", "player not logged in", HttpStatus.UNAUTHORIZED);
        }
        Optional<GamePlayer> gameplayer = gamePlayerRepository.findById(gamePlayerId);
        if (!gameplayer.isPresent())
            return Utils.ResponseWithMap("Problem", "gameplayer does not exist", HttpStatus.UNAUTHORIZED);
        boolean playerHasAccess = playerRepository.findByUserName(authentication.getName()).getId() == gameplayer.get().getPlayer().getId();
        if (playerHasAccess) {
            Map<String, Object> gameDTO = gameplayer.get().getGame().ToDTO();
            gameDTO.put("ships", gamePlayerRepository.getOne(gamePlayerId).getShips().stream().map(Ship::shipDTO));
            gameDTO.put("salvoes", gamePlayerRepository.getOne(gamePlayerId).getGame().getPlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvos().stream().map(salvo -> salvo.salvoDTO())).collect(toList()));
            GamePlayer enemy = gameplayer.get().getGame().GetEnemyGamePlayer(gameplayer.get());
            gameDTO.put("hits", GameSalvoHitsMaker(gameplayer.get(), enemy, totalDamagesSelf, totalDamagesOpp));
            gameDTO.put("gameState", GameState(gameplayer.get(), enemy, totalDamagesSelf,totalDamagesOpp));
            return new ResponseEntity<>(gameDTO, HttpStatus.ACCEPTED);
        } else {
            return Utils.ResponseWithMap("Problem", "not your game", HttpStatus.UNAUTHORIZED);
        }
    }
    private String GameState(GamePlayer player, GamePlayer enemy, int[] totalDamagesSelf, int[] totalDamagesOpp){
        if(enemy == null) return "WAITINGFOROPP";
        if(player.getShips().size() == 0)
            return "PLACESHIPS";
        if(totalDamagesSelf[0] == 5 && totalDamagesSelf[1] == 4 && totalDamagesSelf[2] == 3 && totalDamagesSelf[3] == 3 && totalDamagesSelf[4] == 2
                && totalDamagesOpp[0] == 5 && totalDamagesOpp[1] == 4 && totalDamagesOpp[2] == 3 && totalDamagesOpp[3] == 3 && totalDamagesOpp[4] == 2){
            return "TIE";
        }
        if(totalDamagesSelf[0] == 5 && totalDamagesSelf[1] == 4 && totalDamagesSelf[2] == 3 && totalDamagesSelf[3] == 3 && totalDamagesSelf[4] == 2){
            return "WON";
        }
        if(totalDamagesOpp[0] == 5 && totalDamagesOpp[1] == 4 && totalDamagesOpp[2] == 3 && totalDamagesOpp[3] == 3 && totalDamagesOpp[4] == 2){
            return "LOST";
        }
        if(CalculateSalvoTurn(player) != 0){
            return "PLAY";
        }
        else{ return "WAIT"; }
        //Map<String, Object> hits = (Map)lastGameDTO.get("hits");
        //Map<String, Object> self = (Map)lastGameDTO.get("self");
        //Map<String, Object> damages = (Map)lastGameDTO.get("damages");
    }

    public Map<String, Object> GameSalvoHitsMaker(GamePlayer gamePlayer, GamePlayer enemy, int[] totalDamagesSelf, int[] totalDamagesOpp) {
        Map<String, Object> hits = new LinkedHashMap<>();
        hits.put("self",GameSalvoHits(gamePlayer,  enemy, totalDamagesSelf));
        hits.put("opponent",GameSalvoHits(enemy,  gamePlayer, totalDamagesOpp));
        return hits;
    }

    public ArrayList GameSalvoHits(GamePlayer gamePlayer, GamePlayer enemy, int[] totalDamagesOut) {
        ArrayList self = new ArrayList<>();
        int[] totalDamages = new int[6];
        //List <Salvo> opponentList = new ArrayList<>(gamePlayer.getSalvos()) ;
        //opponentList.sort(Comparator.comparing(Salvo::getId));
        for (Salvo salvoInList : gamePlayer.getSalvos()) {
            ArrayList hitLocations = new ArrayList<>();
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("turn", salvoInList.getTurn());
            map.put("damages", HitsAndSinks(salvoInList.getSalvoLocations(), enemy.getShips(), totalDamages, hitLocations));
            map.put("hitLocations", hitLocations);
            map.put("missed", totalDamages[5]);
            self.add(map);
        }
        for (int i = 0; i<totalDamages.length; i++) {
            totalDamagesOut[i] = totalDamages[i];
        }
        return self;
    }

    public Map<String, Object> HitsAndSinks(Set<String> salvoLocations, Set<Ship> enemyShips, int[] totalDamages, ArrayList hitLocations) {

        Map<String, Object> damages = new LinkedHashMap<>();
        totalDamages[5] = salvoLocations.size();
        int carrierHits, battleshipHits, submarineHits, destroyerHits, patrolboatHits;
        carrierHits = FindHitsForShips(salvoLocations, enemyShips.stream().filter(ship -> "carrier".equals(ship.getType())).findFirst(), totalDamages,hitLocations);
        totalDamages[0] += carrierHits;
        damages.put("carrierHits", carrierHits);
        battleshipHits = FindHitsForShips(salvoLocations, enemyShips.stream().filter(ship -> "battleship".equals(ship.getType())).findFirst(), totalDamages,hitLocations);
        totalDamages[1] += battleshipHits;
        damages.put("battleshipHits", battleshipHits);
        submarineHits = FindHitsForShips(salvoLocations, enemyShips.stream().filter(ship -> "submarine".equals(ship.getType())).findFirst(), totalDamages,hitLocations);
        totalDamages[2] += submarineHits;
        damages.put("submarineHits", submarineHits);
        destroyerHits = FindHitsForShips(salvoLocations, enemyShips.stream().filter(ship -> "destroyer".equals(ship.getType())).findFirst(), totalDamages,hitLocations);
        totalDamages[3] += destroyerHits;
        damages.put("destroyerHits", destroyerHits);
        patrolboatHits = FindHitsForShips(salvoLocations, enemyShips.stream().filter(ship -> "patrolboat".equals(ship.getType())).findFirst(), totalDamages,hitLocations);
        totalDamages[4] += patrolboatHits;
        damages.put("patrolboatHits", patrolboatHits);
        damages.put("carrier", totalDamages[0]);
        damages.put("battleship", totalDamages[1]);
        damages.put("submarine", totalDamages[2]);
        damages.put("destroyer", totalDamages[3]);
        damages.put("patrolboat", totalDamages[4]);
        return damages;
    }

    private int FindHitsForShips(Set<String> salvoLocations, Optional<Ship> enemyShip, int[] missed, ArrayList hitted) {
        int hitCounter = 0;
        for (String salvoLocation : salvoLocations) {
            if (enemyShip.get().getLocations().contains(salvoLocation)) {
                hitCounter++;
                missed[5] -= 1;
                hitted.add(salvoLocation);
            }
        }
        return hitCounter;
    }

    @PostMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String, Object>> SaveSalvos(@PathVariable long gamePlayerId, Authentication authentication, @RequestBody Salvo salvo) {
        System.out.println(salvo);
        if (Utils.isGuest(authentication))
            return Utils.ResponseWithMap("error", "player does not exist", HttpStatus.UNAUTHORIZED);
        Optional<GamePlayer> givenGP = gamePlayerRepository.findById(gamePlayerId);
        if (!givenGP.isPresent())
            return Utils.ResponseWithMap("error", "game does not exist", HttpStatus.FORBIDDEN);
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        if (givenGP.get().getPlayer().getUserName().compareTo(loggedPlayer.getUserName()) != 0)
            return Utils.ResponseWithMap("error", "logged user different from game player", HttpStatus.FORBIDDEN);
        salvo.setTurn(CalculateSalvoTurn(givenGP.get()));
        if (salvo.getTurn() == 0 || givenGP.get().HasSalvo(salvo)) {
            return Utils.ResponseWithMap("error", "user already shot salvo this turn", HttpStatus.FORBIDDEN);
        } else {
            salvo.setGamePlayer(givenGP.get());
            givenGP.get().getSalvos().add(salvo);
            gamePlayerRepository.save(givenGP.get());
            return Utils.ResponseWithMap("OK", "salvo added to gameplayer", HttpStatus.OK);
        }
    }

    public int CalculateSalvoTurn(GamePlayer gamePlayer) {
        GamePlayer enemyGamePlayer = gamePlayer.getGame().GetEnemyGamePlayer(gamePlayer);
        if (enemyGamePlayer != null && gamePlayer.getSalvos().size() <= enemyGamePlayer.getSalvos().size()) {
            return gamePlayer.getSalvos().size() + 1;
        }
        //if enemy doesn't exist or shot has already been fired, returns 0
        return 0;
    }
}

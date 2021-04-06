package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.Interface.*;
import com.codeoftheweb.salvo.Model.*;

import org.hibernate.sql.OracleJoinFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @RequestMapping("/games")
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String,Object> dto = new LinkedHashMap<>();
        if(isGuest(authentication)) {
            dto.put("player", "Guest");
        }else{
            dto.put("player", playerRepository.findByUserName(authentication.getName()).ToDTO());
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.ToDTO()).collect(toList()));
        return dto;
    }

    @GetMapping("/players")
    public List<Object> getAllPlayers() {
        List<Player> list = playerRepository.findAll();
        return list.stream().map(player -> player.ToDTO()).collect(toList());
    }

    @PostMapping("/players")
    public ResponseEntity<Map<String,Object>> register(
            @RequestParam String userName, @RequestParam String password) {

        if ( userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(MakeMap("Error","Missing data"), HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByUserName(userName) !=  null) {
            return new ResponseEntity<>(MakeMap("Error","Name already in use"), HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(MakeMap("Message","Success, player created"),HttpStatus.CREATED);
    }

   /* @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long gamePlayerId) {

        Optional<GamePlayer> gameplayer = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String,Object>> response;

        if(gameplayer.isPresent()){
            Map<String,Object> gameDTO = gameplayer.get().getGame().ToDTO();
            gameDTO.put("ships", gamePlayerRepository.getOne(gamePlayerId).getShips().stream().map(Ship::shipDTO));
            gameDTO.put("salvoes", gamePlayerRepository.getOne(gamePlayerId).getGame().getPlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvos().stream().map(salvo -> salvo.salvoDTO())) .collect(toList()));
            response = new ResponseEntity<>(gameDTO, HttpStatus.ACCEPTED);
        }else{
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","gameplayer does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }*/

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long gamePlayerId,Authentication authentication) {

        Optional<GamePlayer> gameplayer = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String,Object>> response;

        if(gameplayer.isPresent()){
            if(playerRepository.findByUserName(authentication.getName()).getId() == gameplayer.get().getPlayer().getId()){
                Map<String,Object> gameDTO = gameplayer.get().getGame().ToDTO();
                gameDTO.put("ships", gamePlayerRepository.getOne(gamePlayerId).getShips().stream().map(Ship::shipDTO));
                gameDTO.put("salvoes", gamePlayerRepository.getOne(gamePlayerId).getGame().getPlayers().stream().flatMap(gamePlayer1 -> gamePlayer1.getSalvos().stream().map(salvo -> salvo.salvoDTO())) .collect(toList()));
                response = new ResponseEntity<>(gameDTO, HttpStatus.ACCEPTED);
            }else{
                Map<String,Object> mapAux = new LinkedHashMap<>();
                mapAux.put("Problem","not your game");
                response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
            }
        }else{
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","gameplayer does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> MakeMap(String key, String message){

        Map<String,Object> map = new LinkedHashMap<>();
        map.put(key, message);
        return map;
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String,Object>> CreateGame(Authentication authentication) {
        ResponseEntity<Map<String,Object>> response;
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        if(loggedPlayer != null){

            Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame.getCurrentDate(),newGame,loggedPlayer));

            Map<String,Object> gameDTO = new LinkedHashMap<>();
            gameDTO.put("gpid", newGamePlayer.getId());

            response = new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
        }else{
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","player does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }

    @PostMapping("/game/{gameid}/players")
    public ResponseEntity<Map<String,Object>> JoinGame(@PathVariable long gameid,Authentication authentication) {
        Map<String,Object> gameDTO = new LinkedHashMap<>();
        ResponseEntity<Map<String,Object>> response;

        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        if(loggedPlayer != null){
            Game gameToJoin = gameRepository.findById(gameid);
            if(gameToJoin != null) {
                if( gameToJoin.getPlayers().size() < 2){
                    GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(gameToJoin.getCurrentDate(),gameToJoin,loggedPlayer));
                    gameDTO.put("gpid", newGamePlayer.getId());
                    response = new ResponseEntity<>(gameDTO, HttpStatus.CREATED);

                }else{
                    Map<String,Object> mapAux = new LinkedHashMap<>();
                    mapAux.put("Problem","game is full");
                    response = new ResponseEntity<>(mapAux, HttpStatus.FORBIDDEN);
                }
            } else {
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","game does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.FORBIDDEN);
            }
        }else{
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","player does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
        }

            return response;
    }
}

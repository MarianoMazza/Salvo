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
        if(!isGuest(authentication)) {
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

    @RequestMapping("/game_view/{gamePlayerId}")
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
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> MakeMap(String key, String message){

        Map<String,Object> map = new LinkedHashMap<>();
        map.put(key, message);
        return map;
    }
}

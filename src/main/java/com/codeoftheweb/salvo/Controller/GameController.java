package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Interface.GamePlayerRepository;
import com.codeoftheweb.salvo.Interface.GameRepository;
import com.codeoftheweb.salvo.Interface.PlayerRepository;
import com.codeoftheweb.salvo.Model.Game;
import com.codeoftheweb.salvo.Model.GamePlayer;
import com.codeoftheweb.salvo.Model.Player;
import com.codeoftheweb.salvo.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @RequestMapping("/games")
    public Map<String, Object> getAllGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if (isGuest(authentication)) {
            dto.put("player", "Guest");
        } else {
            dto.put("player", playerRepository.findByUserName(authentication.getName()).ToDTO());
        }
        dto.put("games", gameRepository.findAll().stream().map(game -> game.ToDTO()).collect(toList()));
        return dto;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @PostMapping("/games")
    public ResponseEntity<Map<String, Object>> CreateGame(Authentication authentication) {
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        if (loggedPlayer != null) {
            Game newGame = gameRepository.save(new Game(LocalDateTime.now()));
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(newGame.getCurrentDate(), newGame, loggedPlayer));
            Map<String, Object> gameDTO = new LinkedHashMap<>();
            gameDTO.put("gpid", newGamePlayer.getId());
            return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
        } else {
            return Utils.ResponseWithMap("Problem", "player does not exist", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/game/{gameid}/players")
    public ResponseEntity<Map<String, Object>> JoinGame(@PathVariable long gameid, Authentication authentication) {
        if (Utils.isGuest(authentication))
            return Utils.ResponseWithMap("error", "player does not exist", HttpStatus.UNAUTHORIZED);
        Game gameToJoin = gameRepository.findById(gameid);
        if (gameToJoin == null)
            return Utils.ResponseWithMap("error", "game does not exist", HttpStatus.FORBIDDEN);
        if (gameToJoin.getPlayers().size() < 2) {
            Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
            GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(gameToJoin.getCurrentDate(), gameToJoin, loggedPlayer));
            Map<String, Object> gameDTO = new LinkedHashMap<>();
            gameDTO.put("gpid", newGamePlayer.getId());
            return new ResponseEntity<>(gameDTO, HttpStatus.OK);
        } else {
            return Utils.ResponseWithMap("error", "game is full", HttpStatus.CONFLICT);
        }
    }
}

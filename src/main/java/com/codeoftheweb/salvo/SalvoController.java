package com.codeoftheweb.salvo;
import com.codeoftheweb.salvo.Interface.GamePlayerRepository;
import com.codeoftheweb.salvo.Interface.GameRepository;
import com.codeoftheweb.salvo.Interface.PlayerRepository;
import com.codeoftheweb.salvo.Model.Game;
import com.codeoftheweb.salvo.Model.GamePlayer;
import com.codeoftheweb.salvo.Model.Player;
import com.codeoftheweb.salvo.Model.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        @RequestMapping("/games")
        public List<Object> getAllGames() {
                List<Game> list = gameRepository.findAll();
                return list.stream().map(game -> game.ToDTO()).collect(toList());
        }

        @RequestMapping("/players")
        public List<Object> getAllPlayers() {
                List<Player> list = playerRepository.findAll();
                return list.stream().map(player -> player.ToDTO()).collect(toList());
        }

    @RequestMapping("/game_view/{gamePlayerId}")
    public ResponseEntity<Map<String,Object>> getGameView(@PathVariable long gamePlayerId) {

        Optional<GamePlayer> gameplayer = gamePlayerRepository.findById(gamePlayerId);
        ResponseEntity<Map<String,Object>> response;

        if(gameplayer.isPresent()){
            Map<String,Object> gameDTO = gameplayer.get().getGame().ToDTO();
            gameDTO.put("Ships", gamePlayerRepository.getOne(gamePlayerId).getShips().stream().map(Ship::shipDTO));
            response = new ResponseEntity<>(gameDTO, HttpStatus.ACCEPTED);
        }else{
            Map<String,Object> mapAux = new LinkedHashMap<>();
            mapAux.put("Problem","gameplayer does not exist");
            response = new ResponseEntity<>(mapAux, HttpStatus.UNAUTHORIZED);
        }
        return response;
    }
}

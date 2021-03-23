package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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


   /* @RequestMapping("/game_view/{nn}")
    public List<Object> getGameView(@PathVariable long nn) {
        List<GamePlayer> list = gamePlayerRepository.findAll();
        List<Long> lista = list.stream().map(gamePlayer -> gamePlayer.HasPlayer(nn)).collect(toList());
        List<GamePlayer> list3 = gamePlayerRepository.findAllById(lista);
        return list3.stream().map(gamePlayer -> gamePlayer.GameViewDTO()).collect(toList());
    }*/

    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String,Object> getGameView(@PathVariable long gamePlayerId) {
        Map<String,Object> mapa = gamePlayerRepository.getOne(gamePlayerId).getGame().ToDTO();
        //mapa.put("Ships", gamePlayerRepository.getOne(gamePlayerId).ShipsDTO());
        mapa.put("Ships", gamePlayerRepository.getOne(gamePlayerId).getShips().stream().map(Ship::shipDTO));

        return mapa;
    }
}

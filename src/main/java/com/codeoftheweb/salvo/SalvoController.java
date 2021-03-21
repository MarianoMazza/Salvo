package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
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
}

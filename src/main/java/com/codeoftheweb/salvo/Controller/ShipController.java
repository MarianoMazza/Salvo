package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Interface.GamePlayerRepository;
import com.codeoftheweb.salvo.Interface.PlayerRepository;
import com.codeoftheweb.salvo.Model.GamePlayer;
import com.codeoftheweb.salvo.Model.Player;
import com.codeoftheweb.salvo.Model.Ship;
import com.codeoftheweb.salvo.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String, Object>> SaveShips(@PathVariable long gamePlayerId, Authentication authentication, @RequestBody Set<Ship> ships) {
        if (Utils.isGuest(authentication))
            return Utils.ResponseWithMap("error", "player does not exist", HttpStatus.UNAUTHORIZED);
        Optional<GamePlayer> givenGP = gamePlayerRepository.findById(gamePlayerId);
        if (!givenGP.isPresent())
            return Utils.ResponseWithMap("error", "game does not exist", HttpStatus.FORBIDDEN);
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        if (givenGP.get().getPlayer().getUserName().compareTo(loggedPlayer.getUserName()) != 0)
            return Utils.ResponseWithMap("error", "logged user different from game player", HttpStatus.FORBIDDEN);
        if(!givenGP.get().getShips().isEmpty()) {
            return Utils.ResponseWithMap("error", "user already has ships", HttpStatus.FORBIDDEN);
        }else {
            givenGP.get().setShips(ships);
            gamePlayerRepository.save(givenGP.get());
            return Utils.ResponseWithMap("OK", "ships loaded on game player", HttpStatus.OK);
        }
    }
}

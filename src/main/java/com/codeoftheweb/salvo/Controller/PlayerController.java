package com.codeoftheweb.salvo.Controller;

import com.codeoftheweb.salvo.Interface.PlayerRepository;
import com.codeoftheweb.salvo.Model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class PlayerController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/players")
    public List<Object> getAllPlayers() {
        List<Player> list = playerRepository.findAll();
        return list.stream().map(player -> player.ToDTO()).collect(toList());
    }

    @PostMapping("/players")
    public ResponseEntity<Map<String, Object>> register(
            @RequestParam String userName, @RequestParam String password) {
        if (userName.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(MakeMap("Error", "Missing data"), HttpStatus.FORBIDDEN);
        }
        if (playerRepository.findByUserName(userName) != null) {
            return new ResponseEntity<>(MakeMap("Error", "Name already in use"), HttpStatus.FORBIDDEN);
        }
        playerRepository.save(new Player(userName, passwordEncoder.encode(password)));
        return new ResponseEntity<>(MakeMap("Message", "Success, player created"), HttpStatus.CREATED);
    }

    private Map<String, Object> MakeMap(String key, String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(key, message);
        return map;
    }
}

package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Interface.*;
import com.codeoftheweb.salvo.Model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository repositoryGames, GamePlayerRepository repoGamePlayers, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {

			// save a couple of players
			Player Player1 = repository.save(new Player("Jack@gmail.com"));
			Player Player2 = repository.save(new Player("Chloe@gmail.com"));
			Player Player3 = repository.save(new Player("Kim@gmail.com"));
			Player Player4 = repository.save(new Player("David@gmail.com"));
			repository.save(new Player("Michelle@gmail.com"));

			//save games
			Game game1 = repositoryGames.save(new Game(LocalDateTime.now()));
			Game game2 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(1)));
			repositoryGames.save(new Game(LocalDateTime.now().plusHours(2)));

			//game players repo
			GamePlayer gamePlayer1 = new GamePlayer(game1.getCurrentDate(),game1,Player1);
			repoGamePlayers.save(gamePlayer1);
			GamePlayer gamePlayer2 = new GamePlayer(game1.getCurrentDate(),game1,Player2);
			repoGamePlayers.save(gamePlayer2);
			repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player3));
			repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player4));

			//ships
			Ship ship1 = new Ship( new HashSet(Arrays.asList("H1", "H2", "H3", "H4")),"Battleship", gamePlayer1);
			Ship ship2 = new Ship( new HashSet(Arrays.asList("A1", "A2", "A3", "A4", "A5")),"Carrier", gamePlayer1);
			Ship ship3 = new Ship( new HashSet(Arrays.asList("C1", "C2", "C3")),"Submarine", gamePlayer1);
			Ship ship4 = new Ship( new HashSet(Arrays.asList("D1", "D2", "D3")),"Destroyer", gamePlayer1);
			Ship ship5 = new Ship( new HashSet(Arrays.asList("J1", "J2")),"Patrol Boat", gamePlayer1);
			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

			//ships
			Ship ship6 = new Ship( new HashSet(Arrays.asList("H1", "H2", "H3", "H4")),"Battleship", gamePlayer2);
			Ship ship7 = new Ship( new HashSet(Arrays.asList("B1", "B2", "B3", "B4", "B5")),"Carrier", gamePlayer2);
			Ship ship8 = new Ship( new HashSet(Arrays.asList("C1", "C2", "C3")),"Submarine", gamePlayer2);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);

			Salvo salvo1 = new Salvo(gamePlayer1, new HashSet(Arrays.asList("H1", "A1")), 1);
			salvoRepository.save(salvo1);
		};
	}
}

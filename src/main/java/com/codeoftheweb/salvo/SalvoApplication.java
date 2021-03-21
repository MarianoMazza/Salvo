package com.codeoftheweb.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository repositoryGames, GamePlayerRepository repoGamePlayers) {
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
			repoGamePlayers.save(new GamePlayer(game1.getCurrentDate(),game1,Player1));
			repoGamePlayers.save(new GamePlayer(game1.getCurrentDate(),game1,Player2));
			repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player3));
			repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player4));
		};
	}
}

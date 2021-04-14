package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Interface.*;
import com.codeoftheweb.salvo.Model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository repository, GameRepository repositoryGames, GamePlayerRepository repoGamePlayers, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			// save a couple of players
			Player Player1 = repository.save(new Player("j.bauer@ctu.gov",passwordEncoder().encode("24")));
			Player Player2 = repository.save(new Player("c.obrian@ctu.gov",passwordEncoder().encode("42")));
			Player Player3 = repository.save(new Player("kim_bauer@ctu.gov",passwordEncoder().encode("kb")));
			Player Player4 = repository.save(new Player("t.almeida@ctu.gov",passwordEncoder().encode("mole")));

			//save games
			Game game1 = repositoryGames.save(new Game(LocalDateTime.now()));
			Game game2 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(1)));
			Game game3 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(2)));
			Game game4 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(3)));
			Game game5 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(4)));
			Game game6 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(5)));
			Game game7 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(6)));
			Game game8 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(7)));

			//game players repo
			GamePlayer gamePlayer1 = repoGamePlayers.save(new GamePlayer(game1.getCurrentDate(),game1,Player1));
			GamePlayer gamePlayer2 = repoGamePlayers.save(new GamePlayer(game1.getCurrentDate(),game1,Player2));
			GamePlayer gamePlayer3 = repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player1));
			GamePlayer gamePlayer4 = repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player2));
			GamePlayer gamePlayer5 = repoGamePlayers.save(new GamePlayer(game3.getCurrentDate(),game3,Player2));
			GamePlayer gamePlayer6 = repoGamePlayers.save(new GamePlayer(game3.getCurrentDate(),game3,Player4));
			GamePlayer gamePlayer7 = repoGamePlayers.save(new GamePlayer(game4.getCurrentDate(),game4,Player2));
			GamePlayer gamePlayer8 = repoGamePlayers.save(new GamePlayer(game4.getCurrentDate(),game4,Player3));
			GamePlayer gamePlayer9 = repoGamePlayers.save(new GamePlayer(game5.getCurrentDate(),game5,Player4));
			GamePlayer gamePlayer10 = repoGamePlayers.save(new GamePlayer(game5.getCurrentDate(),game5,Player1));
			GamePlayer gamePlayer11 = repoGamePlayers.save(new GamePlayer(game6.getCurrentDate(),game6,Player1));
			GamePlayer gamePlayer12 = repoGamePlayers.save(new GamePlayer(game7.getCurrentDate(),game7,Player4));
			GamePlayer gamePlayer13 = repoGamePlayers.save(new GamePlayer(game8.getCurrentDate(),game8,Player3));
			GamePlayer gamePlayer14 = repoGamePlayers.save(new GamePlayer(game8.getCurrentDate(),game8,Player4));
		};
	}
}


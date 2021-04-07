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

			//scores
			Score score1= scoreRepository.save(new Score(game1,Player1,1));
			Score score2= scoreRepository.save(new Score(game1,Player2,0f));
			Score score3= scoreRepository.save(new Score(game2,Player1,0.5f));
			Score score4= scoreRepository.save(new Score(game2,Player2,0.5f));
			Score score5= scoreRepository.save(new Score(game3,Player2,1));
			Score score6= scoreRepository.save(new Score(game3,Player4,0));
			Score score7= scoreRepository.save(new Score(game4,Player2,0.5f));
			Score score8= scoreRepository.save(new Score(game4,Player3,0.5f));

			Score score9= scoreRepository.save(new Score(game5,Player2,0.5f));
			Score score10= scoreRepository.save(new Score(game5,Player3,0.5f));
			Score score11= scoreRepository.save(new Score(game6,Player2,0.5f));
			Score score12= scoreRepository.save(new Score(game7,Player3,0.5f));
			Score score13= scoreRepository.save(new Score(game8,Player2,0.5f));
			Score score14= scoreRepository.save(new Score(game8,Player3,0.5f));

			//ships
			Ship ship1 = shipRepository.save(new Ship( new HashSet(Arrays.asList("E1", "F1", "G1")),"Submarine", gamePlayer1));
			Ship ship2 = shipRepository.save(new Ship( new HashSet(Arrays.asList("H2", "H3", "H4")),"Destroyer", gamePlayer1));
			Ship ship3 = shipRepository.save(new Ship( new HashSet(Arrays.asList("B4", "B5")),"Patrol Boat", gamePlayer1));

			//ships
			Ship ship6 = shipRepository.save(new Ship( new HashSet(Arrays.asList("B5", "C5", "D5")),"Destroyer", gamePlayer2));
			Ship ship7 = shipRepository.save(new Ship( new HashSet(Arrays.asList("F1","F2")),"Patrol Boat", gamePlayer2));



			Ship ship9 = new Ship( new HashSet(Arrays.asList("H1", "H2", "H3", "H4")),"Battleship", gamePlayer3);
			Ship ship10 = new Ship( new HashSet(Arrays.asList("B1", "B2", "B3", "B4", "B5")),"Carrier", gamePlayer3);
			Ship ship11 = new Ship( new HashSet(Arrays.asList("C1", "C2", "C3")),"Submarine", gamePlayer3);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);

			Ship ship12 = new Ship( new HashSet(Arrays.asList("H1", "H2", "H3", "H4")),"Battleship", gamePlayer4);
			Ship ship13 = new Ship( new HashSet(Arrays.asList("B1", "B2", "B3", "B4", "B5")),"Carrier", gamePlayer4);
			Ship ship14 = new Ship( new HashSet(Arrays.asList("C1", "C2", "C3")),"Submarine", gamePlayer4);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);



			Salvo salvo1 = salvoRepository.save(new Salvo(gamePlayer1, new HashSet(Arrays.asList("B5", "C5","F1")), 1));

			Salvo salvo2 = salvoRepository.save(new Salvo(gamePlayer2, new HashSet(Arrays.asList("B4", "B5" ,"B6")), 1));

			Salvo salvo3 = salvoRepository.save(new Salvo(gamePlayer1, new HashSet(Arrays.asList("F2","D5")), 2));

			Salvo salvo4 = salvoRepository.save(new Salvo(gamePlayer2, new HashSet(Arrays.asList("E1", "H3" ,"A2")), 2));


			Salvo salvo5 = salvoRepository.save(new Salvo(gamePlayer3, new HashSet(Arrays.asList("A2", "A4", "G6")), 1));

			Salvo salvo6 = salvoRepository.save(new Salvo(gamePlayer4, new HashSet(Arrays.asList("B5", "D5","C7")), 1));

			Salvo salvo7 = salvoRepository.save(new Salvo(gamePlayer3, new HashSet(Arrays.asList("A3", "H6")), 2));

			Salvo salvo8 = salvoRepository.save(new Salvo(gamePlayer4, new HashSet(Arrays.asList("C5", "C6")), 2));


			Salvo salvo9 = salvoRepository.save(new Salvo(gamePlayer5, new HashSet(Arrays.asList("G6", "H6", "A4")), 1));

			Salvo salvo10 = salvoRepository.save(new Salvo(gamePlayer6, new HashSet(Arrays.asList("H1", "H2","H3")), 1));

			Salvo salvo11 = salvoRepository.save(new Salvo(gamePlayer5, new HashSet(Arrays.asList("A2", "A3", "D8")), 2));

			Salvo salvo12 = salvoRepository.save(new Salvo(gamePlayer6, new HashSet(Arrays.asList("E1", "F2", "G3")), 2));


			Salvo salvo13 = salvoRepository.save(new Salvo(gamePlayer7, new HashSet(Arrays.asList("A3", "A4", "F7")), 1));

			Salvo salvo14 = salvoRepository.save(new Salvo(gamePlayer8, new HashSet(Arrays.asList("B5", "C6","H1")), 1));

			Salvo salvo15 = salvoRepository.save(new Salvo(gamePlayer7, new HashSet(Arrays.asList("A2", "G6", "H6")), 2));

			Salvo salvo16 = salvoRepository.save(new Salvo(gamePlayer8, new HashSet(Arrays.asList("C5", "C7", "D5")), 2));


			Salvo salvo17 = salvoRepository.save(new Salvo(gamePlayer9, new HashSet(Arrays.asList("A1", "A2", "A3")), 1));

			Salvo salvo18 = salvoRepository.save(new Salvo(gamePlayer10, new HashSet(Arrays.asList("B5", "B6","C7")), 1));

			Salvo salvo19 = salvoRepository.save(new Salvo(gamePlayer9, new HashSet(Arrays.asList("G6", "G7", "G8")), 2));

			Salvo salvo20 = salvoRepository.save(new Salvo(gamePlayer10, new HashSet(Arrays.asList("C6", "D6", "E6")), 2));

			Salvo salvo21 = salvoRepository.save(new Salvo(gamePlayer9, new HashSet(Arrays.asList("H1","H2")), 3));

		};
	}
}


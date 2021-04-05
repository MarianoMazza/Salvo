package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.Interface.*;
import com.codeoftheweb.salvo.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

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
			Player Player1 = new Player("j.bauer@ctu.gov",passwordEncoder().encode("24"));
			//Player1.setScore(new HashSet(Arrays.asList(score1,score2)));
			repository.save(Player1);
			Player Player2 = repository.save(new Player("c.obrian@ctu.gov",passwordEncoder().encode("42")));
			Player Player3 = repository.save(new Player("kim_bauer@ctu.gov",passwordEncoder().encode("kb")));
			Player Player4 = repository.save(new Player("t.almeida@ctu.gov",passwordEncoder().encode("mole")));

			//save games
			Game game1 = repositoryGames.save(new Game(LocalDateTime.now()));
			Game game2 = repositoryGames.save(new Game(LocalDateTime.now().plusHours(1)));

			//game players repo
			GamePlayer gamePlayer1 = new GamePlayer(game1.getCurrentDate(),game1,Player1);
			repoGamePlayers.save(gamePlayer1);
			GamePlayer gamePlayer2 = new GamePlayer(game1.getCurrentDate(),game1,Player2);
			repoGamePlayers.save(gamePlayer2);
			GamePlayer gamePlayer3 = repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player3));
			GamePlayer gamePlayer4 = repoGamePlayers.save(new GamePlayer(game2.getCurrentDate(),game2,Player4));

			//scores
			Score score1= scoreRepository.save(new Score(game1,Player1,0.5f));
			Score score2= scoreRepository.save(new Score(game1,Player2,1));
			Score score3= scoreRepository.save(new Score(game2,Player3,0.5f));
			Score score4= scoreRepository.save(new Score(game2,Player4,1));


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
			Salvo salvo2 = new Salvo(gamePlayer2, new HashSet(Arrays.asList("B2", "A1")), 2);
			salvoRepository.save(salvo2);

			Salvo salvo3 = new Salvo(gamePlayer3, new HashSet(Arrays.asList("H1", "A1")), 1);
			salvoRepository.save(salvo3);
			Salvo salvo4 = new Salvo(gamePlayer4, new HashSet(Arrays.asList("B2", "A1")), 2);
			salvoRepository.save(salvo4);

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
		};
	}
}


package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository) {
		return (args) -> {

			// NEW PLAYERS

			Player player1 = new Player("lio@gmail.com");
			Player player2 = new Player("john@gmail.com");
			Player player3 = new Player("hector@gmail.com");

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);


			// NEW GAMES

Game game1 = new Game();


Game game2 = new Game();
Date game2Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(3600));
game2.setGameCreationDate(game2Date);


Game game3 = new Game();
Date game3Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(7200));
game3.setGameCreationDate(game3Date);

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);


		// GAMEPLAYER REPOSITORY

			GamePlayer gamePlayer1 = new GamePlayer(game1, player2);
			GamePlayer gamePlayer2= new GamePlayer(game1,player1);
			GamePlayer gamePlayer3 = new GamePlayer(game2, player3);
			GamePlayer gamePlayer4 = new GamePlayer(game2, player1);
			GamePlayer gamePlayer5 = new GamePlayer(game3, player2);
			GamePlayer gamePlayer6 = new GamePlayer(game3, player3);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);

			// NEW LOCATIONS

			List<String> location1 = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			List<String> location2 = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			List<String> location3 = new ArrayList<>(Arrays.asList("B4", "B5", "B6", "B7", "B8"));
			List<String> location4 = new ArrayList<>(Arrays.asList("E1", "F1", "G1"));
			List<String> location5 = new ArrayList<>(Arrays.asList("C5", "D5", "E5", "F5"));
			List<String> location6 = new ArrayList<>(Arrays.asList("H7", "H8"));
			List<String> location7 = new ArrayList<>(Arrays.asList("B7", "D5", "A3"));

			// NEW SHIPS

			Ship ship1 = new Ship("DESTROYER", gamePlayer1, location1);
			Ship ship2 = new Ship("SUBMARINE", gamePlayer1, location2);
			Ship ship3 = new Ship("CARRIER", gamePlayer2, location3);
			Ship ship4 = new Ship("BATTLESHIP", gamePlayer2, location4);
			Ship ship5 = new Ship("PATROL_BOAT", gamePlayer3, location5);

			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship5);

			// NEW SALVOES

			Salvo salvo1 = new Salvo(1, gamePlayer1, location5);
			Salvo salvo2 = new Salvo(1, gamePlayer2, location6);
			Salvo salvo3 = new Salvo(2, gamePlayer1, location7);
			Salvo salvo4 = new Salvo(2, gamePlayer2, location4);
			Salvo salvo5 = new Salvo(1, gamePlayer3, location5);

			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);





		};
	}

}
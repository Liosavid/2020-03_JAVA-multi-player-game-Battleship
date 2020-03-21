package com.codeoftheweb.salvo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository) {
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

			GamePlayer firstMatch1 = new GamePlayer(game1, player2);
			gamePlayerRepository.save(firstMatch1);
			GamePlayer firstMatch2= new GamePlayer(game1,player1);
			gamePlayerRepository.save(firstMatch2);

			GamePlayer secondMatch1 = new GamePlayer(game2, player3);
			gamePlayerRepository.save(secondMatch1);
			GamePlayer secondMatch2 = new GamePlayer(game2, player1);
			gamePlayerRepository.save(secondMatch2);

			GamePlayer thirdMatch1 = new GamePlayer(game3, player2);
			gamePlayerRepository.save(thirdMatch1);
			GamePlayer thirdMatch2 = new GamePlayer(game3, player3);
			gamePlayerRepository.save(thirdMatch2);

		};
	}

}
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
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository) {
		return (args) -> {

			// NEW PLAYERS

			playerRepository.save(new Player("lio"));
			playerRepository.save(new Player("john"));

			// NEW GAMES

Game game1 = new Game();
			gameRepository.save(game1);


Game game2 = new Game();
Date game2Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(3600));
game2.setGameCreationDate(game2Date);

             gameRepository.save(game2);

Game game3 = new Game();
Date game3Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(7200));
game3.setGameCreationDate(game3Date);

			gameRepository.save(game3);


		};
	}

}
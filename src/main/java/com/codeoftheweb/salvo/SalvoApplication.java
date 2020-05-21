package com.codeoftheweb.salvo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.CommandLineRunner;
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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {

		SpringApplication.run(SalvoApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {

			// NEW PLAYERS

			Player player1 = new Player("lio@gmail.com", passwordEncoder().encode("24"));
			Player player2 = new Player("john@gmail.com", passwordEncoder().encode("42"));
			Player player3 = new Player("hector@gmail.com", passwordEncoder().encode("kb"));
			Player player4 = new Player("t.almeida@ctu.gov",  passwordEncoder().encode("mole"));

			playerRepository.save(player1);
			playerRepository.save(player2);
			playerRepository.save(player3);
			playerRepository.save(player4);


			// NEW GAMES

Game game1 = new Game();

Game game2 = new Game();
Date game2Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(3600));
game2.setGameCreationDate(game2Date);

Game game3 = new Game();
Date game3Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(7200));
game3.setGameCreationDate(game3Date);

Game game4 = new Game();
Date game4Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(10800));
game3.setGameCreationDate(game4Date);

Game game5 = new Game();
Date game5Date = Date.from(game1.getGameCreationDate().toInstant().plusSeconds(10800));
game3.setGameCreationDate(game5Date);


			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);


		// GAMEPLAYER REPOSITORY

			GamePlayer gamePlayer1 = new GamePlayer(game1, player2);
			GamePlayer gamePlayer2= new GamePlayer(game1,player1);
			GamePlayer gamePlayer3 = new GamePlayer(game2, player2);
			GamePlayer gamePlayer4 = new GamePlayer(game2, player1);
			GamePlayer gamePlayer5 = new GamePlayer(game3, player2);
			GamePlayer gamePlayer6 = new GamePlayer(game3, player3);
			GamePlayer gamePlayer7 = new GamePlayer(game4, player2);
			GamePlayer gamePlayer8 = new GamePlayer(game5, player4);
			GamePlayer gamePlayer9 = new GamePlayer(game5, player1);

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);
			gamePlayerRepository.save(gamePlayer3);
			gamePlayerRepository.save(gamePlayer4);
			gamePlayerRepository.save(gamePlayer5);
			gamePlayerRepository.save(gamePlayer6);
			gamePlayerRepository.save(gamePlayer7);
			gamePlayerRepository.save(gamePlayer8);
			gamePlayerRepository.save(gamePlayer9);

			// NEW LOCATIONS

			List<String> location1 = new ArrayList<>(Arrays.asList("H2", "H3", "H4"));
			List<String> location2 = new ArrayList<>(Arrays.asList("G3", "G4", "G5"));
			List<String> location3 = new ArrayList<>(Arrays.asList("B4", "B5", "B6", "B7", "B8"));
			List<String> location4 = new ArrayList<>(Arrays.asList("E1", "F1", "G1", "H1"));
			List<String> location5 = new ArrayList<>(Arrays.asList("C5", "D5", "E5"));
			List<String> location6 = new ArrayList<>(Arrays.asList("H7", "H8"));
			List<String> location7 = new ArrayList<>(Arrays.asList("A1", "A2"));

			// NEW SHIPS

			Ship ship1 = new Ship("DESTROYER", gamePlayer1, location1);
			Ship ship2 = new Ship("SUBMARINE", gamePlayer1, location2);

			Ship ship14 = new Ship("PATROL_BOAT", gamePlayer1, Arrays.asList("A1", "A2"));
			Ship ship15 = new Ship("CARRIER", gamePlayer1, Arrays.asList("B1", "B2"));
			Ship ship16 = new Ship("BATTLESHIP", gamePlayer1, Arrays.asList("C1", "C2"));



			Ship ship3 = new Ship("CARRIER", gamePlayer2, location3);
			Ship ship4 = new Ship("BATTLESHIP", gamePlayer2, location4);
			Ship ship6 = new Ship("DESTROYER", gamePlayer2, location5);
			Ship ship7 = new Ship("PATROL_BOAT", gamePlayer2, location7);
			Ship ship8 = new Ship("SUBMARINE", gamePlayer2, location2);

			Ship ship22 = new Ship("CARRIER", gamePlayer3, location3);
			Ship ship23 = new Ship("BATTLESHIP", gamePlayer3, location4);
			Ship ship24 = new Ship("DESTROYER", gamePlayer3, location5);
			Ship ship25 = new Ship("PATROL_BOAT", gamePlayer3, location7);
			Ship ship26 = new Ship("SUBMARINE", gamePlayer3, location2);


			Ship ship27 = new Ship("DESTROYER", gamePlayer4, location1);
			Ship ship28 = new Ship("SUBMARINE", gamePlayer4, location2);
			Ship ship29 = new Ship("PATROL_BOAT", gamePlayer4, Arrays.asList("A1", "A2"));
			Ship ship30 = new Ship("CARRIER", gamePlayer4, Arrays.asList("B1", "B2"));
			Ship ship31 = new Ship("BATTLESHIP", gamePlayer4, Arrays.asList("C1", "C2"));




			Ship ship9 = new Ship("CARRIER", gamePlayer8, location3);
			Ship ship10 = new Ship("BATTLESHIP", gamePlayer8, location4);
			Ship ship11 = new Ship("DESTROYER", gamePlayer8, location5);
			Ship ship12 = new Ship("PATROL_BOAT", gamePlayer8, location7);
			Ship ship13 = new Ship("SUBMARINE", gamePlayer8, location2);

			Ship ship17 = new Ship("CARRIER", gamePlayer7, location3);
			Ship ship18 = new Ship("BATTLESHIP", gamePlayer7, location4);
			Ship ship19 = new Ship("DESTROYER", gamePlayer7, location5);
			Ship ship20 = new Ship("PATROL_BOAT", gamePlayer7, location7);
			Ship ship21 = new Ship("SUBMARINE", gamePlayer7, location2);


			shipRepository.save(ship1);
			shipRepository.save(ship2);
			shipRepository.save(ship3);
			shipRepository.save(ship4);
			shipRepository.save(ship6);
			shipRepository.save(ship7);
			shipRepository.save(ship8);
			shipRepository.save(ship9);
			shipRepository.save(ship10);
			shipRepository.save(ship11);
			shipRepository.save(ship12);
			shipRepository.save(ship13);
			shipRepository.save(ship14);
			shipRepository.save(ship15);
			shipRepository.save(ship16);
			shipRepository.save(ship17);
			shipRepository.save(ship18);
			shipRepository.save(ship19);
			shipRepository.save(ship20);
			shipRepository.save(ship21);
			shipRepository.save(ship22);
			shipRepository.save(ship23);
			shipRepository.save(ship24);
			shipRepository.save(ship25);
			shipRepository.save(ship26);
			shipRepository.save(ship27);
			shipRepository.save(ship28);
			shipRepository.save(ship29);
			shipRepository.save(ship30);
			shipRepository.save(ship31);


			// NEW SALVOES

			Salvo salvo1 = new Salvo(1, gamePlayer1, location3);
			Salvo salvo2 = new Salvo(1, gamePlayer2, location6);
			Salvo salvo3 = new Salvo(2, gamePlayer1, location4);
			Salvo salvo4 = new Salvo(2, gamePlayer2, location4);

			Salvo salvo5 = new Salvo(1, gamePlayer3, location3);
			Salvo salvo9 = new Salvo(1, gamePlayer3, location4);
			Salvo salvo10 = new Salvo(1, gamePlayer3, location5);
			Salvo salvo11 = new Salvo(1, gamePlayer3, location7);
			Salvo salvo12 = new Salvo(1, gamePlayer3, location2);

			Salvo salvo6 = new Salvo(3, gamePlayer1, location5);
			Salvo salvo7 = new Salvo(4, gamePlayer1, location7);
			Salvo salvo8 = new Salvo(5, gamePlayer1, location2);


			salvoRepository.save(salvo1);
			salvoRepository.save(salvo2);
			salvoRepository.save(salvo3);
			salvoRepository.save(salvo4);
			salvoRepository.save(salvo5);
			salvoRepository.save(salvo6);
			salvoRepository.save(salvo7);
			salvoRepository.save(salvo8);
			salvoRepository.save(salvo9);
			salvoRepository.save(salvo10);
			salvoRepository.save(salvo11);
			salvoRepository.save(salvo12);

			// new scores

			Score score1 = new Score(game1,player1,1.0);
			Score score2 = new Score(game1,player2,0.0);
			Score score3 = new Score(game2,player1,0.5);
			Score score4 = new Score(game2,player2,0.5);
			Score score5 = new Score(game3,player2,0.0);
			Score score6 = new Score(game3,player3,1.0);

			scoreRepository.save(score1);
			scoreRepository.save(score2);
			scoreRepository.save(score3);
			scoreRepository.save(score4);
			scoreRepository.save(score5);
			scoreRepository.save(score6);


		};
	}

}



@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;



	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByUserName(inputName);
			if (player != null) {
				return new User(player.getUserName(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				System.out.println("no player");
				throw new UsernameNotFoundException("Unknown player: " + inputName);
			}
		});
	}

}


@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.authorizeRequests()
				.antMatchers( "/favicon.ico").permitAll()
				.antMatchers( "/web/images/**").permitAll()
				.antMatchers( "/web/game.html").permitAll()
				.antMatchers( "/web/js/game.js").permitAll()
				.antMatchers("/web/games.html","api/game_view/**", "/api/games", "api/game_view", "/api/players", "api/scores", "/api/leaderboard").permitAll()
				.antMatchers("/web/js/games.js").permitAll()
				.antMatchers("/web/style/style.css").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER")

				.and()
				.formLogin();

		http.formLogin()
				.usernameParameter("userName")
				.passwordParameter("password")
				.loginPage("/api/login");

		http.logout().logoutUrl("/api/logout");



		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}


	}







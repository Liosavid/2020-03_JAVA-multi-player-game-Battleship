package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoRepository salvoRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/games")
    public Map<String, Object> returnGames(Authentication authentication) {
        Map<String, Object> dto = new LinkedHashMap<>();
        if(!isGuest(authentication)) {
            dto.put("player", makePlayersDTO(playerRepository.findByUserName(authentication.getName())));
        }

        else{

            dto.put("player", null);
        }
        dto.put("games", getAllGames());
        return dto;
    }
    public List<Map<String,Object>> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGamesDTO(game))
                .collect(Collectors.toList());
    }
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    // CREATE NEW PLAYER

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>>
    createUser(@RequestBody Player player) {
        if (player.getUserName().isEmpty()) {
            return new ResponseEntity<>(makeMap("error","No name given"), HttpStatus.FORBIDDEN);
        }
        else if (player.getPassword().isEmpty()) {
            return new ResponseEntity<>(makeMap("error","No password given"), HttpStatus.FORBIDDEN);
        }

        Player player1 = playerRepository.findByUserName(player.getUserName());
         if (player1 != null) {
            return new ResponseEntity<>(makeMap("error","Name already used"), HttpStatus.CONFLICT);
        }

        Player newPlayer = playerRepository.save(new Player(player.getUserName(), passwordEncoder.encode(player.getPassword())));
        return new ResponseEntity<>(makeMap("username", newPlayer.getUserName()), HttpStatus.CREATED);
    }


    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }


    // CREATE NEW GAME

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createGame(Authentication authentication){

        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "You need to be logged in"), HttpStatus.UNAUTHORIZED);
        }

        Game newGame = gameRepository.save(new Game());
        Player currentPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer newGamePlayer = new GamePlayer(newGame, currentPlayer);
        gamePlayerRepository.save(newGamePlayer);

        return new ResponseEntity<>(makeMap("GamePlayerID", newGamePlayer.getGamePlayerId()), HttpStatus.CREATED);
    }

// JOIN GAME

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication, @PathVariable Long gameId){

        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "You need to be logged in"), HttpStatus.UNAUTHORIZED);
        }
        Game currentGame = gameRepository.findById(gameId).orElse(null);

        if(currentGame == null){
            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
        }
        if (currentGame.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(makeMap("error", "Game is full"), HttpStatus.FORBIDDEN);
        }

        Player currentPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer newGamePlayer = new GamePlayer(currentGame, currentPlayer);
        gamePlayerRepository.save(newGamePlayer);

        return new ResponseEntity<>(makeMap("newGamePlayerID", newGamePlayer.getGamePlayerId()), HttpStatus.CREATED);
    }

// Implement a back-end controller method that can receive a list of ship objects, with locations,
// save them in your ship repository, and return a "created" response if there are no problems.

    @RequestMapping(path="/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeShips(@PathVariable Long gamePlayerId, @RequestBody Set<Ship>ships, Authentication authentication) {

        GamePlayer gameplayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        Player currentPlayer = gameplayer.getPlayer();

        if (loggedPlayer == null){
            return new ResponseEntity<>(makeMap("Error","You cannot place ships if you are not logged in"), HttpStatus.UNAUTHORIZED);
        }
        else if(gameplayer == null) {
            return new ResponseEntity<>(makeMap("Error", "This Game Player doesn't exist!"), HttpStatus.UNAUTHORIZED);

        }else if(loggedPlayer != currentPlayer) {
            return new ResponseEntity<>(makeMap("Error", "You are not the same Game Player!"), HttpStatus.UNAUTHORIZED);

        }
        else if (ships.size() != 5)
        {
            return new ResponseEntity<>(makeMap("Error","Wrong size of ships."), HttpStatus.FORBIDDEN);
        }

        else{
            if(gameplayer.getShips().size() >= 5){
                return new ResponseEntity<>(makeMap("Error","Ships are already placed in your game."), HttpStatus.FORBIDDEN);

            }else{

                for (Ship ship: ships) {
                    ship.setGamePlayer(gameplayer);
                    shipRepository.save(ship);
                }

                return new ResponseEntity<>(makeMap("Good", "Ships have been placed"), HttpStatus.CREATED);

            }
        }
    }


  // Controller method to store salvos
  // The request should return a failure response, of the appropriate type, if the user is not logged is, or is not the game player the ID refers to.
  // Implement a back-end controller method that can receive a salvo object, consisting of a turn and a list of locations.

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placeSalvoes(@PathVariable Long gamePlayerId, Authentication authentication, @RequestBody List<String>salvoLocations) {
        GamePlayer gameplayer = gamePlayerRepository.findById(gamePlayerId).orElse(null);
        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        Player currentPlayer = gameplayer.getPlayer();

        if (loggedPlayer == null) {
            return new ResponseEntity("Error, you have to be logged first!", HttpStatus.UNAUTHORIZED);
        }
        else if (gameplayer == null) {
            return new ResponseEntity<>(makeMap("Error", "This Game Player doesn't exist!"), HttpStatus.UNAUTHORIZED);
        }
        else if (loggedPlayer != currentPlayer) {
            return new ResponseEntity<>(makeMap("Error", "You are not the same Game Player!"), HttpStatus.UNAUTHORIZED);
        }

        else if(salvoLocations.size() != 5){
            return new ResponseEntity<>(makeMap("error", "You can only use 5 salvoes!"), HttpStatus.FORBIDDEN);
        }

        else if((getOpponent(gameplayer) != null) && (gameplayer.getSalvoes().size() > getOpponent(gameplayer).getSalvoes().size())){
            return new ResponseEntity<>(makeMap("Error", "You have already submitted salvoes for this turn!"), HttpStatus.FORBIDDEN);

        }

        else if ((getOpponent(gameplayer) == null) && (gameplayer.getSalvoes().size() == 1)){
            return new ResponseEntity<>(makeMap("Error", "Wait for the opponent"), HttpStatus.FORBIDDEN);

        }

        else{

                Salvo salvo = new Salvo(gameplayer.getSalvoes().size() + 1, gameplayer,salvoLocations);
                salvoRepository.save(salvo);

                return new ResponseEntity<>(makeMap("Boom!", "Your Salvoes have been sent!"), HttpStatus.CREATED);

            }

    }






    //Via DTO's - Data Transfer Objects, i can structure the way i want my data returned to me, via either
    //Maps, which are shown as Objects in JSON, or Lists, which are shown as arrays.



    private Map<String, Object> makeGamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("game_id", game.getGameId());
        dto.put("game_creation_date", game.getGameCreationDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayersDTO(gamePlayer))
                .collect(Collectors.toList()));
       // dto.put("score",game.getScores().stream().map(score -> makeScoresDTO(score))
        //        .collect(Collectors.toList()));


        return dto;
    }

    //Gets Player Information for each game


    private Map<String, Object> makeGamePlayersDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gamePlayer_id", gamePlayer.getGamePlayerId());
        dto.put("player", makePlayersDTO(gamePlayer.getPlayer()));
        Score score = gamePlayer.getScore();
        dto.put("score", score == null ? "No score" : score.getScore());



        return dto;
    }


    private Map<String, Object> makePlayersDTO(Player player) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("user_id", player.getUserId());
        dto.put("user_name", player.getUserName());
        return dto;
    }

    private Map<String, Object> makeShipsDTO(Ship ship) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("type", ship.getShipType());
        dto.put("locations", ship.getShipLocations());
        return dto;
    }

    private Map<String, Object> makeSalvoesDTO(Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gamePlayerId", salvo.getGamePlayer().getGamePlayerId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getSalvoLocations());
        return dto;
    }
    private List<Object> makeGamePlayersSalvo(GamePlayer gamePlayer) {
        return gamePlayer.getSalvoes().stream().map(salvo -> makeSalvoesDTO(salvo)).collect(Collectors.toList());
    }

    @RequestMapping("/game_view/{gpId}")
    public Map<String,Object> getGameView(Authentication authentication, @PathVariable Long gpId) {

        Player loggedPlayer = playerRepository.findByUserName(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpId).get();
        Player currentPlayer = gamePlayer.getPlayer();
        sinkingShip(gamePlayer);

        //     if (loggedPlayer == currentPlayer) {

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gameId", gamePlayer.getGame().getGameId());
        dto.put("created", gamePlayer.getGame().getGameCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gp -> makeGamePlayersDTO(gp))
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> makeShipsDTO(ship))
                .collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream().map(gp -> makeGamePlayersSalvo(gp))
                .collect(Collectors.toList()));
        dto.put("score",gamePlayer.getGame().getScores().stream().map(score -> makeScoresDTO(score))
                .collect(Collectors.toList()));
        dto.put("hitsOnOpponent", gamePlayer.getSalvoes().stream().map(salvo -> getHits(salvo)).collect(Collectors.toList()));
        dto.put("hitsOnCurrentPlayer", gamePlayer.getSalvoes().stream().map(salvo -> getHitsonCurrentPlayer(salvo)).collect(Collectors.toList()));
        dto.put("missedHits", gamePlayer.getSalvoes().stream().map(salvo -> getMissedHits(salvo)).collect(Collectors.toList()));
        dto.put("gameHistory", gamePlayer.getGame().getGamePlayers().stream().map(gp -> shipLocationEnemyInfo(gp))
                .collect(Collectors.toList()));


        return dto;

            //     }  else {

   //  return new ResponseEntity<>(makeMap("Error", "You cannot cheat!!"), HttpStatus.UNAUTHORIZED);

    }

////////////////////////////////////////////////////////////////////////////////////////////////

    //  HITS - MISSED HITS - SINKING SHIPS

    private List<String> getShipsLocations(GamePlayer gamePlayer) {

        Set<Ship> ships = gamePlayer.getShips();

        // grouping all ship locations together in one array with the flatmap

        return ships.stream()
                .map(ship -> ship.getShipLocations())
                .flatMap(cells -> cells.stream()).collect(Collectors.toList());
    }

    private List<String> getSalvoLocations(GamePlayer gamePlayer) {

        // grouping all salvo locations together in one array with the flatmap
        return gamePlayer.getSalvoes().stream()
                .map(salvo -> salvo.getSalvoLocations())
                .flatMap(cells -> cells.stream()).collect(Collectors.toList());

    }

    private List<String> getHits(Salvo salvo) {

        GamePlayer opponent = getOpponent(salvo.getGamePlayer());

        if (opponent != null) {

            // check if salvo is hit or missed

            List<String> salvoLocations = salvo.getSalvoLocations();
            List<String> ShipLocations = getShipsLocations(opponent);

            // comparing cells with salvoes from current player vs cells of opponent´s ships

            return salvoLocations.stream().filter(cell -> ShipLocations.contains(cell))
                    .collect(Collectors.toList());
        } else return null;
    }

    private List<String> getHitsonCurrentPlayer(Salvo salvo) {

        GamePlayer opponent = getOpponent(salvo.getGamePlayer());
        GamePlayer currentPlayer = getCurrentPlayer(salvo.getGamePlayer());

        if (opponent != null) {

            // check if salvo is hit or missed

            List<String> ShipLocations = getShipsLocations(currentPlayer);
            List<String> salvoLocations = getSalvoLocations(opponent);


            // comparing cells with salvoes from current player vs cells of opponent´s ships

            return salvoLocations.stream().filter(cell -> ShipLocations.contains(cell))
                    .collect(Collectors.toList());
        } else return null;
    }



    private List<String> getMissedHits(Salvo salvo) {

        GamePlayer opponent = getOpponent(salvo.getGamePlayer());

        if (opponent != null) {


            List<String> salvoLocations = salvo.getSalvoLocations();
            List<String> ShipLocations = getShipsLocations(opponent);

            // comparing cells with salvoes from current player vs cells of opponent´s ships

            return salvoLocations.stream().filter(cell -> !ShipLocations.contains(cell))
                    .collect(Collectors.toList());
        } else return null;
    }

    private List<String> hitsOnCurrentPlayer (Salvo salvo) {

        GamePlayer opponent = getOpponent(salvo.getGamePlayer());

        if (opponent != null) {


            List<String> salvoLocations = salvo.getSalvoLocations();
            List<String> ShipLocations = getShipsLocations(opponent);

            // comparing cells with salvoes from current player vs cells of opponent´s ships

            return salvoLocations.stream().filter(cell -> !ShipLocations.contains(cell))
                    .collect(Collectors.toList());
        } else return null;
    }


    private boolean shipIsFullySunk(List<String> playerSalvos, Ship ship) {
        boolean shipIsFullySunk = ship.getShipLocations().stream()
                .allMatch(locations -> playerSalvos.contains(locations));
        if (shipIsFullySunk) {
            ship.setSunk(true);
            shipRepository.save(ship);
        }
        return shipIsFullySunk;
    }

    private void sinkingShip(GamePlayer gamePlayer) {

        GamePlayer enemy = getOpponent(gamePlayer);

        if (enemy != null) {
            GamePlayer enemyPlayer = getOpponent(gamePlayer);
            Set<Ship> enemyShips = enemyPlayer.getShips();
            List<String> playerSalvos = getSalvoLocations(gamePlayer);
            enemyShips.stream().filter(ship -> !ship.isSunk())
                    .forEach(ship -> {
                        shipIsFullySunk(playerSalvos, ship);
                    });
        }
    }
    

    private Map<String, Object> shipLocationEnemyInfo (GamePlayer gamePlayer) {
        Map<String, Object> gamePlayerMap = new HashMap<>();

        gamePlayerMap.put("gamePlayerId", gamePlayer.getGamePlayerId());
        gamePlayerMap.put("shipStatus", gamePlayer.getShips().stream()
                .map(ship -> shipLocationInfo(ship)).collect(Collectors.toList()));

        return gamePlayerMap;
    }
    private Map<String,Object> shipLocationInfo (Ship ship) {
        Map<String,Object> shipMap = new LinkedHashMap<>();

        shipMap.put("type", ship.getShipType());
        shipMap.put("sunk", ship.isSunk());
        return shipMap;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////


    private Map<String, Object> makeScoresDTO(Score score) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("ScoreId", score.getScoreId());
        dto.put("score", score.getScore());
        dto.put("player",makePlayersDTO(score.getPlayer()));
        return dto;

    }



    @RequestMapping("/leaderboard")
    public Map<String, Object> getScores() {
        Map<String, Object> allPlayers = new LinkedHashMap<>();
        List<Player> players = playerRepository.findAll();
        for (Player player: players) {
Map<String, Object> scores = new LinkedHashMap<>();
if (!scores.containsKey(player.getUserName())){
   scores.put("wins", player.getScores().stream().filter(score -> score.getScore() == 1).count());
    scores.put("lost", player.getScores().stream().filter(score -> score.getScore() == 0).count());
    scores.put("draw", player.getScores().stream().filter(score -> score.getScore() == 0.5).count());
    scores.put("total", player.getScores().stream().mapToDouble(score -> score.getScore()).sum());
    scores.put("numberGames", player.getGames().stream().count());

    allPlayers.put(player.getUserName(),scores);
}
        }

        return allPlayers;

}



private GamePlayer getOpponent (GamePlayer gameplayer){
return gameplayer.getGame().getGamePlayers().stream().filter(gp -> gp.getGamePlayerId() != gameplayer.getGamePlayerId()).findFirst().orElse(null);

}


    private GamePlayer getCurrentPlayer (GamePlayer gameplayer){
        return gameplayer.getGame().getGamePlayers().stream().filter(gp -> gp.getGamePlayerId() == gameplayer.getGamePlayerId()).findFirst().orElse(null);

    }


}

package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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


    @RequestMapping("/games")
    public List<Map<String,Object>> getAllGames() {
        return gameRepository
                .findAll()
                .stream()
                .map(game -> makeGamesDTO(game))
                .collect(Collectors.toList());
    }


    private Map<String, Object> makeGamesDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("game_id", game.getGameId());
        dto.put("game_creation_date", game.getGameCreationDate());
        dto.put("gamePlayers", game.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayersDTO(gamePlayer))
                .collect(Collectors.toList()));

        return dto;
    }

    //Gets Player Information for each game
    private Map<String, Object> makeGamePlayersDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("gamePlayer_id", gamePlayer.getGamePlayerId());
        dto.put("player", makePlayersDTO(gamePlayer.getPlayer()));

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

    @RequestMapping("/game_view/{gpId}")
    public Map<String,Object> getGameView(@PathVariable Long gpId) {
        //Game g1 = gameRepository.findById(gameId).get();
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpId).get();
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gameId", gamePlayer.getGame().getGameId());
        dto.put("created", gamePlayer.getGame().getGameCreationDate());
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gp -> makeGamePlayersDTO(gp))
                .collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(ship -> makeShipsDTO(ship))
                .collect(Collectors.toList()));

        /*
        System.out.println(g1.getGameId());
            Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("gameId", g1.getGameId());
        dto.put("created", g1.getGameCreationDate());

        dto.put("gamePlayers", g1.getGamePlayers()
                .stream()
                .map(gamePlayer -> makeGamePlayersDTO(gamePlayer))
                .collect(Collectors.toList()));

        dto.put("ship", g1.getGamePlayers()
                .stream()
                .map(gp -> gp.getShips()
                        .stream()
                        .map(ship -> makeShipsDTO(ship))
                        .collect(Collectors.toList())));
*/

        return dto;


    }
}
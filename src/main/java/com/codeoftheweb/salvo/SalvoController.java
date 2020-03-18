package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
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

}
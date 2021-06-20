package com.nioya.mancala.controller;

import com.nioya.mancala.service.GameService;
import com.nioya.mancala.model.Movement;
import com.nioya.mancala.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/start")
    @ResponseBody
    public Game getStart(@RequestParam String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping("/move")
    public Game getMovement(@Valid @RequestBody Movement movement) {
        return gameService.getMove(movement);
    }


}

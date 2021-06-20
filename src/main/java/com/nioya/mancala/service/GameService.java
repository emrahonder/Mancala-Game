package com.nioya.mancala.service;

import com.nioya.mancala.model.Game;
import com.nioya.mancala.model.Movement;


public interface GameService {

    Game getGame(String gameId);

    Game getMove(Movement movement);
}

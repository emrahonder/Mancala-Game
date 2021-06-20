package com.nioya.mancala.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
    public String gameId;
    public String currentPlayer;
    public boolean isWinnerExist;
    public String winner;
    public Player player1;
    public Player player2;
}

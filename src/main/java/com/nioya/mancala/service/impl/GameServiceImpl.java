package com.nioya.mancala.service.impl;

import com.nioya.mancala.model.Game;
import com.nioya.mancala.model.Movement;
import com.nioya.mancala.model.Player;
import com.nioya.mancala.service.GameService;
import com.nioya.mancala.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    private HashMap<String, Game> games = new HashMap<>();

    @Override
    public Game getGame(String gameId) {
        if (games.containsKey(gameId)) {
            return games.get(gameId);
        }
        return games.get(createNewGame());
    }

    @Override
    public Game getMove(Movement movement) {
        Game game = this.getGame(movement.getGameId());
        if (!game.getCurrentPlayer().equals(movement.getPlayer())) return game;

        Game tempGame = game;
        int pitNumber = movement.getPitNumber();

        boolean isCurrentsTurn = false;
        switch (movement.getPlayer()) {
            case Constants.PLAYER1_KEY:

                isCurrentsTurn = calculateMovements(tempGame, pitNumber);
                if (!isCurrentsTurn) {
                    tempGame.setCurrentPlayer(Constants.PLAYER2_KEY);
                }
                log.info("Movement of " + game.getGameId() + " Turn: "+ Constants.PLAYER1_KEY);
                break;
            case Constants.PLAYER2_KEY:
                Player player1 = game.getPlayer1();
                Player player2 = game.getPlayer2();
                tempGame.setPlayer1(player2);
                tempGame.setPlayer2(player1);
                isCurrentsTurn = calculateMovements(tempGame, pitNumber);
                if (!isCurrentsTurn) {
                    tempGame.setCurrentPlayer(Constants.PLAYER1_KEY);
                }
                player1 = tempGame.getPlayer2();
                player2 = tempGame.getPlayer1();
                tempGame.setPlayer1(player1);
                tempGame.setPlayer2(player2);
                log.info("Movement of " + game.getGameId() + " Turn: "+ Constants.PLAYER2_KEY);
                break;
            default:
                return game;
        }
        boolean isWinnerExist = checkWinner(tempGame);
        tempGame.setWinnerExist(isWinnerExist);
        game = tempGame;
        return game;
    }

    private String createNewGame() {
        UUID uuid = UUID.randomUUID();
        String gameId = uuid.toString();
        games.put(gameId, this.initiateResultSet(gameId));
        log.info("New Game is created. Game ID:" + gameId);
        return gameId;
    }


    private boolean calculateMovements(Game game, int pitNumber) {
        Player currentPlayer = game.getPlayer1();
        Player counterPlayer = game.getPlayer2();

        int[] ownPits = currentPlayer.getPits();
        int stones = ownPits[pitNumber];
        boolean isMyTurn = false;


        ownPits[pitNumber] = Constants.BLANK;
        if (stones == Constants.BLANK) return true;
        while (stones != Constants.BLANK) {
            for (int i = pitNumber + 1; i < Constants.PIT; i++) {
                if (stones == Constants.BLANK) break;
                ownPits[i]++;
                stones--;
            }

            if (stones != Constants.BLANK) {
                isMyTurn = true;
                currentPlayer.treasury++;
                stones--;
            }

            if (stones != Constants.BLANK) {
                isMyTurn = false;
                int[] counterPits = counterPlayer.getPits();
                int i = 0;
                int targetIndex = (stones >= Constants.PIT) ? Constants.PIT : stones;

                for (i = 0; i < targetIndex; i++) {
                    if (stones == Constants.BLANK) break;
                    counterPits[i]++;
                    stones--;
                }
                if (counterPits[i - 1] % 2 == 0 && counterPits[i - 1] > 0) {
                    currentPlayer.treasury += counterPits[i];
                    currentPlayer.treasury += ownPits[Constants.PIT - i];
                    ownPits[Constants.PIT - i] = 0;
                    counterPits[i - 1] = 0;

                }
                counterPlayer.setPits(counterPits);
            }
            currentPlayer.setPits(ownPits);
        }

        game.setPlayer1(currentPlayer);
        game.setPlayer2(counterPlayer);
        return isMyTurn;
    }

    private Game initiateResultSet(String gameId) {
        int[] pits1 = new int[Constants.PIT];
        int[] pits2 = new int[Constants.PIT];

        Arrays.fill(pits1, Constants.STONE);
        Arrays.fill(pits2, Constants.STONE);

        return Game.builder().
                gameId(gameId).
                currentPlayer(Constants.PLAYER1_KEY).
                player1(Player.builder().pits(pits1).build()).
                player2(Player.builder().pits(pits2).build()).
                build();
    }

    private boolean checkWinner(Game game) {

        boolean isWinnerExist = isAllStonesSpent(game.getPlayer1().getPits())
                || isAllStonesSpent(game.getPlayer2().getPits());

        if (isWinnerExist) {
            decideWinner(game);
        }
        return isWinnerExist;
    }

    private void decideWinner(Game game) {
        int player1Treasury = game.getPlayer1().treasury +
                Arrays.stream(game.getPlayer1().getPits()).sum();
        int player2Treasury = game.getPlayer2().treasury +
                Arrays.stream(game.getPlayer2().getPits()).sum();
        if (player1Treasury > player2Treasury) {
            log.info("Winner of " + game.getGameId() + " is " + Constants.PLAYER1_KEY);
            game.setWinner(Constants.PLAYER1_KEY);
        } else {
            log.info("Winner of " + game.getGameId() + " is " + Constants.PLAYER2_KEY);
            game.setWinner(Constants.PLAYER2_KEY);
        }
    }

    private static boolean isAllStonesSpent(int[] pits) {
        for (int pit : pits) if (pit != 0) return false;
        return true;
    }
}

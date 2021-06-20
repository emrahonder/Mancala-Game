package com.nioya.mancala.service;

import com.nioya.mancala.model.Game;
import com.nioya.mancala.model.Movement;
import com.nioya.mancala.service.impl.GameServiceImpl;
import com.nioya.mancala.util.Constants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Test
    public void whenGameIdNotExist_shouldReturnNewGameId() {
        assertNotEquals(gameService.getGame("random").getGameId(), "random");
    }


    @Test
    public void whenGameIdExists_shouldReturnExistOne() throws Exception {
        Game game = gameService.getGame("random");
        assertEquals(gameService.getGame(game.getGameId()).getGameId(), game.getGameId());
    }


    @Test
    public void whenMovementExists_shouldReturnProperResult() throws Exception {
        Movement movement = Movement.builder().gameId("random").player(Constants.PLAYER1_KEY).pitNumber(4).build();
        Game game = gameService.getMove(movement);
        assertEquals(game.getCurrentPlayer(), Constants.PLAYER2_KEY);
        assertFalse(game.isWinnerExist);
        assertEquals(game.getPlayer1().getTreasury(), 1);

    }


}

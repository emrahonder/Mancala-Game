package com.nioya.mancala.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nioya.mancala.model.Game;
import com.nioya.mancala.model.Movement;
import com.nioya.mancala.model.Player;
import com.nioya.mancala.service.impl.GameServiceImpl;
import com.nioya.mancala.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class IntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private GameServiceImpl gameService;

    @InjectMocks
    private GameController gameController;

    private final static String gameId = "random";

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
        HashMap<String, Game> games = new HashMap<>();
        games.put(gameId, this.initiateResultSet(gameId));
        ReflectionTestUtils.setField(gameService, "games", games);

        when(gameService.getGame(any())).thenCallRealMethod();
        when(gameService.getMove(any())).thenCallRealMethod();
    }

    @Test
    public void whenGameIdExists_shouldReturnIt() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/start")
                .queryParam("gameId", "random")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameId").value(gameId));

    }

    @Test
    public void whenGameIdNotExists_shouldReturnNewGameId() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                .get("/start")
                .queryParam("gameId", "newGame")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> assertFalse("This game Id exists",
                        MockMvcResultMatchers.jsonPath("$.gameId").value(gameId).equals(gameId)));
    }

    @Test
    public void whenHasMovement_shouldUpdateGame() throws Exception {
        Movement movement = Movement.builder().
                gameId("random").
                player(Constants.PLAYER1_KEY).
                pitNumber(4).
                build();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/move")
                .content(asJsonString(movement))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameId").value(gameId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPlayer").value(Constants.PLAYER2_KEY))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gameId").value(gameId));
    }

    @Test
    public void whenMovementObjectWrong_shouldReturnError() throws Exception {
        Movement movement = Movement.builder().build();

        mockMvc.perform(MockMvcRequestBuilders
                .post("/move")
                .content(asJsonString(movement))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
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

    private static String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

package com.nioya.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
    @NotBlank
    public String gameId;

    @NotBlank
    @Pattern(regexp = "(?:player1|player2)", message = "must be player1 or player2")
    public String player;

    @Min(0)
    @Max(5)
    public int pitNumber;
}

package com.nioya.mancala.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {
    public int[] pits;
    public int treasury;
}

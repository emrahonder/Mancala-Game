package com.nioya.mancala.exceptions;

import lombok.Data;

/**
 * general model for violation in the execution flow
 */
@Data
public class Violation {
    private final String fieldName;
    private final String message;
}

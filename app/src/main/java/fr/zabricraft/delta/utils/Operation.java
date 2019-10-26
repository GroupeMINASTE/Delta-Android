package fr.zabricraft.delta.utils;

import java.util.HashMap;

import fr.zabricraft.delta.tokens.Token;

public enum Operation {

    // Values
    addition("+"), subtraction("-"), multiplication("*"), division("/"), modulo("%"), power("^"), root("√"), equals("="), unequals("!="), greaterThan(">"), lessThan("<"), greaterThanOrEquals(">="), lessThanOrEquals("<="), list(","), vector(";");

    // Properties
    String rawValue;

    // Constructor
    Operation(String rawValue) {
        this.rawValue = rawValue;
    }

    // Operation from String
    public static Operation from(String string) {
        // Iterate values
        for (Operation value : Operation.values()) {
            // If it's the value we want
            if (string.equals(value.rawValue)) {
                return value;
            }
        }

        // Nothing found
        return null;
    }

    // Get precedence
    public int getPrecedence() {
        if (this == Operation.power) {
            return 3;
        }
        if (this == Operation.multiplication || this == Operation.division || this == Operation.modulo) {
            return 2;
        }
        return 1;
    }

    // Join with two tokens
    public Token join(Token left, Token right) {
        // Check for equations
        if (this == Operation.equals || this == Operation.unequals || this == Operation.greaterThan || this == Operation.lessThan || this == Operation.greaterThanOrEquals || this == Operation.lessThanOrEquals) {
            return new Equation(left, right, this);
        }

        // Simple expression
        return left.apply(this, right, new HashMap<String, Token>(), true);
    }

}

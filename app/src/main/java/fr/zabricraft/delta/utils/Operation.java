package fr.zabricraft.delta.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.tokens.Equation;
import fr.zabricraft.delta.tokens.Function;
import fr.zabricraft.delta.tokens.Token;
import fr.zabricraft.delta.tokens.Variable;
import fr.zabricraft.delta.tokens.Vector;

public enum Operation {

    // Values
    addition("+"), subtraction("-"), multiplication("*"), division("/"), modulo("%"), power("^"), root("√"), equals("="), unequals("!="), greaterThan(">"), lessThan("<"), greaterThanOrEquals(">="), lessThanOrEquals("<="), list1(","), list2(";"), function("f");

    // Properties
    public final String rawValue;

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
        if (this == Operation.function) {
            return 4;
        }
        if (this == Operation.power || this == Operation.root) {
            return 3;
        }
        if (this == Operation.multiplication || this == Operation.division || this == Operation.modulo) {
            return 2;
        }
        return 1;
    }

    // Join with two tokens
    public Token join(Token left, Token right, List<String> ops, Map<String, Token> inputs) {
        // Check for equations
        if (this == Operation.equals || this == Operation.unequals || this == Operation.greaterThan || this == Operation.lessThan || this == Operation.greaterThanOrEquals || this == Operation.lessThanOrEquals) {
            return new Equation(left, right, this);
        }

        // Check for function
        if (this == Operation.function) {
            if (left instanceof Variable) {
                // From left
                return new Function(((Variable) left).getName(), right);
            }
        }

        // Check for lists
        if (ops.contains("{") && (this == Operation.list1 || this == Operation.list2)) {
            if (left instanceof fr.zabricraft.delta.tokens.List) {
                // From left
                List<Token> values = new ArrayList<>(((fr.zabricraft.delta.tokens.List) left).getValues());
                values.add(right);
                return new fr.zabricraft.delta.tokens.List(values);
            } else if (right instanceof fr.zabricraft.delta.tokens.List) {
                // From right
                List<Token> values = new ArrayList<>(((fr.zabricraft.delta.tokens.List) right).getValues());
                values.add(left);
                return new fr.zabricraft.delta.tokens.List(values);
            } else {
                // From new tokens
                return new fr.zabricraft.delta.tokens.List(Arrays.asList(left, right));
            }
        }

        // Check for vectors
        if (ops.contains("(") && (this == Operation.list1 || this == Operation.list2)) {
            if (left instanceof Vector) {
                // From left
                List<Token> values = new ArrayList<>(((Vector) left).getValues());
                values.add(right);
                return new Vector(values);
            } else if (right instanceof Vector) {
                // From right
                List<Token> values = new ArrayList<>(((Vector) right).getValues());
                values.add(left);
                return new Vector(values);
            } else {
                // From new tokens
                return new Vector(Arrays.asList(left, right));
            }
        }

        // Simple expression
        return left.apply(this, right, inputs, true);
    }

}

package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Variable extends Token {

    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        // Check if an input corresponds to this variable
        if (inputs.containsKey(name)) {
            return inputs.get(name).compute(inputs, ComputeMode.simplify);
        }

        // No input found
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        // Compute right
        right = right.compute(inputs, mode);

        // Power
        if (operation == Operation.power) {
            // Check for i
            if (name.equals("i")) {
                // If right is a number
                if (right instanceof Number) {
                    // i^0 = 1
                    if (((Number) right).getValue() % 4 == 0) {
                        return new Number(1);
                    }
                    // i^2 = -1
                    if (((Number) right).getValue() % 4 == 2) {
                        return new Number(-1);
                    }
                    // i^3 = -i
                    if (((Number) right).getValue() % 4 == 3) {
                        return new Product(new Number(-1), this);
                    }
                    // Simplified power of i
                    return new Power(this, new Number(((Number) right).getValue() % 4)).compute(inputs, mode);
                }
            }
            // Check for e
            if (name.equals("e")) {
                // If right is a number
                if (right instanceof Number) {
                    // e^0 = 1
                    if (((Number) right).getValue() == 0) {
                        return new Number(1);
                    }
                }
            }

            return new Power(this, right);
        }

        // Delegate to default
        return defaultApply(operation, right, inputs, mode);
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 2;
    }

    public Token opposite() {
        return new Product(this, new Number(-1));
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        // Exp
        if (name.equals("e")) {
            // give an approximated value
            return Math.exp(1);
        }
        // Pi
        if (name.equals("π")) {
            // Value of pi
            return Math.PI;
        }

        return null;
    }

}

package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Root extends Token {

    private Token token;
    private Token power;

    public Root(Token token, Token power) {
        this.token = token;
        this.power = power;
    }

    public Token getToken() {
        return token;
    }

    public Token getPower() {
        return power;
    }

    public String toString() {
        return "√(" + token.toString() + ")";
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        Token token = this.token.compute(inputs, format);
        Token power = this.power.compute(inputs, format);

        return token.apply(Operation.root, power, inputs, format);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Power
        if (operation == Operation.power) {
            // Check if power is the same
            if (right instanceof Number && power instanceof Number && ((Number) right).getValue() == ((Number) power).getValue()) {
                // Undo the root
                return token;
            }

            return new Power(this, right);
        }

        // Root
        if (operation == Operation.root) {
            return new Root(token, new Product(power, right).compute(inputs, format));
        }

        // Delegate to default
        return defaultApply(operation, right, inputs, format);
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        return new Product(this, new Number(-1));
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public Double asDouble() {
        Double token = this.token.asDouble();
        Double power = this.power.asDouble();

        if (token != null && power != null) {
            return Math.pow(token, 1 / power);
        }

        return null;
    }

}

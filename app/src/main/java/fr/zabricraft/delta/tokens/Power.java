package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Power extends Token {

    private Token token;
    private Token power;

    public Power(Token token, Token power) {
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
        return (token.needBrackets(Operation.division) ? "(" + token.toString() + ")" : token.toString()) + " ^ " + (power.needBrackets(Operation.division) ? "(" + power.toString() + ")" : power.toString());
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        Token token = this.token.compute(inputs, mode);
        Token power = this.power.compute(inputs, mode);

        // Check power
        if (power instanceof Number) {
            // x^1 is x
            if (((Number) power).getValue() == 1) {
                return token;
            }
        }
        if (power instanceof Fraction) {
            // x^1/y is ^y√(x)
            Token number = power.inverse().compute(inputs, mode);
            if (number instanceof Number) {
                return new Root(token, number).compute(inputs, mode);
            }
        }

        return token.apply(Operation.power, power, inputs, mode);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        // Product
        if (operation == Operation.multiplication) {
            // Token and right are the same
            if (token.equals(right)) {
                return new Power(token, new Sum(power, new Number(1))).compute(inputs, mode);
            }
        }

        // Delegate to default
        return defaultApply(operation, right, inputs, mode);
    }

    public boolean needBrackets(Operation operation) {
        return operation.getPrecedence() >= Operation.power.getPrecedence();
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

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        Double token = this.token.asDouble();
        Double power = this.power.asDouble();

        if (token != null && power != null) {
            return Math.pow(token, power);
        }

        return null;
    }

}

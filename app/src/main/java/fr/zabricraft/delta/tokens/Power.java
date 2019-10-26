package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Power implements Token {

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

    public Token compute(Map<String, Token> inputs, boolean format) {
        Token token = this.token.compute(inputs, format);
        Token power = this.power.compute(inputs, format);

        // Check power
        if (power instanceof Number) {
            // x^1 is x
            if (((Number) power).getValue() == 1) {
                return token;
            }
        }
        if (power instanceof Fraction) {
            // x^1/y is ^y√(x)
            Token number = power.inverse().compute(inputs, format);
            if (number instanceof Number) {
                return new Root(token, number).compute(inputs, format);
            }
        }

        return token.apply(Operation.power, power, inputs, format);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Sum
        if (operation == Operation.addition) {
            return new Sum(this, right);
        }

        // Difference
        if (operation == Operation.subtraction) {
            return new Sum(this, right.opposite()).compute(inputs, format);
        }

        // Product
        if (operation == Operation.multiplication) {
            return new Product(this, right);
        }

        // Fraction
        if (operation == Operation.division) {
            return new Fraction(this, right);
        }

        // Modulo
        if (operation == Operation.modulo) {
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
            return new Power(this, new Product(power, right));
        }

        // Root
        if (operation == Operation.root) {
            return new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
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

    public Double asDouble() {
        Double token = this.token.asDouble();
        Double power = this.power.asDouble();

        if (token != null && power != null) {
            return Math.pow(token, power);
        }

        return null;
    }

}

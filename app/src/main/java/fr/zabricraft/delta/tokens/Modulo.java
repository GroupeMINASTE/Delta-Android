package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Modulo implements Token {

    private Token dividend;
    private Token divisor;

    public Modulo(Token dividend, Token divisor) {
        this.dividend = dividend;
        this.divisor = divisor;
    }

    public Token getDividend() {
        return dividend;
    }

    public Token getDivisor() {
        return divisor;
    }

    public String toString() {
        return (dividend.needBrackets(Operation.division) ? "(" + dividend.toString() + ")" : dividend.toString()) + " % " + (divisor.needBrackets(Operation.division) ? "(" + divisor.toString() + ")" : divisor.toString());
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        Token dividend = this.dividend.compute(inputs, format);
        Token divisor = this.divisor.compute(inputs, format);

        // Check dividend
        if (dividend instanceof Number) {
            // 0 % x is 0
            if (((Number) dividend).getValue() == 0) {
                return dividend;
            }
        }

        // Check divisor
        if (divisor instanceof Number) {
            // x % 0 is calcul error
            if (((Number) divisor).getValue() == 0) {
                return new CalculError();
            }
        }

        // Apply to simplify
        return dividend.apply(Operation.modulo, divisor, inputs, format);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // If addition
        if (operation == Operation.addition) {
            // Right is a sum
            if (right instanceof Sum) {
                List<Token> values = new ArrayList<>(((Sum) right).getValues());
                values.add(this);
                return new Sum(values);
            }

            // Return the sum
            return new Sum(this, right);
        }

        // Difference
        if (operation == Operation.subtraction) {
            return new Sum(this, right.opposite());
        }

        // Product
        if (operation == Operation.multiplication) {
            // Right is a product
            if (right instanceof Product) {
                List<Token> values = new ArrayList<>(((Product) right).getValues());
                values.add(this);
                return new Product(values);
            }

            return new Product(this, right);
        }

        // Fraction
        if (operation == Operation.division) {
            return new Fraction(this, right);
        }

        // Modulo
        if (operation == Operation.modulo) {
            // Return the modulo
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
            // Return the power
            return new Power(this, right);
        }

        // Root
        if (operation == Operation.root) {
            // Return the root
            return new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return operation.getPrecedence() >= Operation.modulo.getPrecedence();
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
        Double dividend = this.dividend.asDouble();
        Double divisor = this.divisor.asDouble();

        if (dividend != null && divisor != null) {
            return dividend % divisor;
        }

        return null;
    }

}

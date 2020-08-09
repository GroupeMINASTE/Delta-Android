package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Modulo extends Token {

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

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        Token dividend = this.dividend.compute(inputs, mode);
        Token divisor = this.divisor.compute(inputs, mode);

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
        return dividend.apply(Operation.modulo, divisor, inputs, mode);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        return defaultApply(operation, right, inputs, mode);
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

    public boolean equals(Token right) {
        return defaultEquals(right);
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

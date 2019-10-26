package fr.zabricraft.delta.tokens;

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


}

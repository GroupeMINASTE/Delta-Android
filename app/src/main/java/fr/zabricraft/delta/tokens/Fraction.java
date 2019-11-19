package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.Operation;

public class Fraction implements Token {

    private Token numerator;
    private Token denominator;

    public Fraction(Token numerator, Token denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public Token getNumerator() {
        return numerator;
    }

    public Token getDenominator() {
        return denominator;
    }

    public String toString() {
        if (numerator instanceof Number && denominator instanceof Number && IntExtension.isPowerOfTen(((Number) denominator).getValue())) {
            // Print it as decimal
            return String.valueOf(((double) ((Number) numerator).getValue()) / ((double) ((Number) denominator).getValue()));
        }

        // Print it as a fraction
        return (numerator.needBrackets(Operation.division) ? "(" + numerator.toString() + ")" : numerator.toString()) + " / " + (denominator.needBrackets(Operation.division) ? "(" + denominator.toString() + ")" : denominator.toString());
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        Token numerator = this.numerator.compute(inputs, format);
        Token denominator = this.denominator.compute(inputs, format);

        // Check numerator
        if (numerator instanceof Number) {
            // 0/x is 0
            if (((Number) numerator).getValue() == 0) {
                return numerator;
            }
        }

        // Check denominator
        if (denominator instanceof Number) {
            // x/1 is x
            if (((Number) denominator).getValue() == 1) {
                return numerator;
            }

            // x/0 is calcul error
            if (((Number) denominator).getValue() == 0) {
                return new CalculError();
            }
        }

        // Apply to simplify
        return numerator.apply(Operation.division, denominator, inputs, format);
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

            // If we keep format
            if (format) {
                return new Sum(this, right);
            }

            // Right is a fraction
            if (right instanceof Fraction) {
                // a/b + c/d = (ad+cb)/bd
                return new Fraction(new Sum(new Product(this.getNumerator(), ((Fraction) right).getDenominator()), new Product(((Fraction) right).getNumerator(), this.getDenominator())), new Product(this.getDenominator(), ((Fraction) right).getDenominator())).compute(inputs, false);
            }

            // Right is anything else
            // a/b + c = (a+cb)/b
            return new Fraction(new Sum(getNumerator(), new Product(right, getDenominator())), getDenominator()).compute(inputs, false);
        }

        // If subtraction
        if (operation == Operation.subtraction) {
            // If we keep format
            if (format) {
                return new Sum(this, right.opposite());
            }

            // Right is a fraction
            if (right instanceof Fraction) {
                // a/b - c/d = (ad-cb)/bd
                return new Fraction(new Sum(new Product(this.getNumerator(), ((Fraction) right).getDenominator()), new Product(((Fraction) right).getNumerator(), this.getDenominator()).opposite()), new Product(this.getDenominator(), ((Fraction) right).getDenominator())).compute(inputs, false);
            }

            // Right is anything else
            // a/b - c = (a-cb)/b
            return new Fraction(new Sum(getNumerator(), new Product(right, getDenominator()).opposite()), getDenominator()).compute(inputs, false);
        }

        // If product
        if (operation == Operation.multiplication) {
            // Right is a product
            if (right instanceof Product) {
                List<Token> values = new ArrayList<>(((Product) right).getValues());
                values.add(this);
                return new Product(values);
            }

            // If we keep format
            if (format) {
                return new Product(this, right);
            }

            // Right is a fraction
            if (right instanceof Fraction) {
                // a/b * c/d = ac/bd
                return new Fraction(new Product(this.getNumerator(), ((Fraction) right).getNumerator()), new Product(this.getDenominator(), ((Fraction) right).getDenominator())).compute(inputs, false);
            }

            // Right is anything else
            // a/b * c = ac/b
            return new Fraction(new Product(right, getNumerator()), getDenominator()).compute(inputs, false);
        }

        // If fraction
        if (operation == Operation.division) {
            // If we keep format
            if (format) {
                return new Fraction(this, right);
            }

            // Multiply by its inverse
            return new Product(this, right.inverse()).compute(inputs, false);
        }

        // Modulo
        if (operation == Operation.modulo) {
            // Return the modulo
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
            // If we keep format
            if (format) {
                return new Power(this, right);
            }

            // Apply power to numerator and denominator
            return new Fraction(new Power(getNumerator(), right), new Power(getDenominator(), right)).compute(inputs, false);
        }

        // Root
        if (operation == Operation.root) {
            // If we keep format
            if (format) {
                return new Root(this, right);
            }

            // Apply root to numerator and denominator
            return new Fraction(new Root(getNumerator(), right), new Root(getDenominator(), right)).compute(inputs, false);
        }

        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return operation.getPrecedence() >= Operation.division.getPrecedence();
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        return new Fraction(new Product(new Number(-1), getNumerator()), getDenominator()).compute(new HashMap<String, Token>(), false);
    }

    public Token inverse() {
        return new Fraction(getDenominator(), getNumerator());
    }

    public Double asDouble() {
        Double numerator = this.numerator.asDouble();
        Double denominator = this.denominator.asDouble();

        if (numerator != null && denominator != null) {
            return numerator / denominator;
        }

        return null;
    }

}

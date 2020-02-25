package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Variable implements Token {

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

    public Token compute(Map<String, Token> inputs, boolean format) {
        // Check if an input corresponds to this variable
        if (inputs.containsKey(name)) {
            return inputs.get(name).compute(inputs, false);
        }

        // No input found
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Sum
        if (operation == Operation.addition) {
            // Right is a sum
            if (right instanceof Sum) {
                List<Token> values = new ArrayList<>(((Sum) right).getValues());
                values.add(this);
                return new Sum(values);
            }

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
                    if (((Number) right).getValue() % 4 == 2) {
                        return new Product(new Number(-1), this);
                    }
                    // Simplified power of i
                    return new Power(this, new Number(((Number) right).getValue() % 4)).compute(inputs, format);
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

        // Root
        if (operation == Operation.root) {
            return new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
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

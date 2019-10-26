package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Sum implements Token {

    private java.util.List<Token> values;

    public Sum(java.util.List<Token> values) {
        this.values = values;
    }

    public Sum(Token... values) {
        this.values = Arrays.asList(values);
    }

    public java.util.List<Token> getValues() {
        return values;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();

        for (Token value : values) {
            // Initialization
            String asString = value.toString();
            boolean op = false;
            boolean minus = false;

            // Check if not empty
            if (string.length() != 0) {
                op = true;
            }

            // Check if we need to keep operator
            if (op && asString.startsWith("-")) {
                // Remove minus from string to have it instead of plus
                minus = true;
                asString = asString.substring(1);
            }

            // Add operator if required
            if (op) {
                if (minus) {
                    string.append(" - ");
                } else {
                    string.append(" + ");
                }
            }

            // Check for brackets
            string.append(asString);
        }

        return string.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        // Compute all values
        java.util.List<Token> values = new ArrayList<>();
        for (Token value : this.values) {
            values.add(value.compute(inputs, format));
        }

        // Some required vars
        int index = 0;

        // Iterate values
        while (index < values.size()) {
            // Get value
            Token value = values.get(index);

            // Check if value is a sum
            if (value instanceof Sum) {
                // Add values to self
                values.addAll(((Sum) value).getValues());

                // Remove current value
                values.remove(index);
                index--;
            } else {
                // Iterate to add it to another value
                int i = 0;
                while (i < values.size()) {
                    // Check if it's not the same index
                    if (i != index) {
                        // Get another value
                        Token otherValue = values.get(i);

                        // Sum them
                        Token sum = value.apply(Operation.addition, otherValue, inputs, format);

                        // If it is simpler than a sum
                        if (!(sum instanceof Sum)) {
                            // Update values
                            value = sum;
                            values.set(index, value);

                            // Remove otherValue
                            values.remove(i);

                            // Update indexes
                            index -= index >= i ? 1 : 0;
                            i--;
                        }
                    }

                    // Increment i
                    i++;
                }

                // Check for zero (0 + x is x)
                if (value instanceof Number && ((Number) value).getValue() == 0) {
                    // Remove zero
                    values.remove(index);
                    index--;
                }
            }

            // Increment index
            index++;
        }

        // If only one value left
        if (values.size() == 1) {
            return values.get(0);
        }

        // If empty
        if (values.isEmpty()) {
            return new Number(0);
        }

        // Return the simplified sum
        return new Sum(values);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // If addition
        if (operation == Operation.addition) {
            // Add token to sum
            java.util.List<Token> values = new ArrayList<>(getValues());
            values.add(right);
            return new Sum(values);
        }

        // If subtraction
        if (operation == Operation.subtraction) {
            // Add token to sum
            java.util.List<Token> values = new ArrayList<>(getValues());
            values.add(right.opposite());
            return new Sum(values);
        }

        // If product
        if (operation == Operation.multiplication) {
            // Add token to product
            return new Product(this, right);
        }

        // If fraction
        if (operation == Operation.division) {
            // Add token to fraction
            return new Fraction(this, right);
        }

        // Modulo
        if (operation == Operation.modulo) {
            // Return the modulo
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
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
        return operation.getPrecedence() >= Operation.addition.getPrecedence();
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        List<Token> values = new ArrayList<>();
        for (Token value : getValues()) {
            values.add(value.opposite());
        }
        return new Sum(values);
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public Double asDouble() {
        return null;
    }

}

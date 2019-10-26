package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Product implements Token, Comparator<Token> {

    private List<Token> values;

    public Product(List<Token> values) {
        this.values = values;
    }

    public Product(Token... values) {
        this.values = Arrays.asList(values);
    }

    public List<Token> getValues() {
        return values;
    }

    public String toString() {
        StringBuilder string = new StringBuilder();
        Collections.sort(values, this);

        for (Token value : values) {
            // Initialization
            boolean brackets = value.needBrackets(Operation.multiplication);
            String asString = value.toString();
            boolean op = false;

            // Check if not empty and not ending by a minus
            if (string.length() != 0 && string.charAt(0) != '-') {
                op = true;
            }

            // Check if we need to keep operator
            if (brackets) {
                // No operator if we already have brackets
                op = false;
            } else if (value instanceof Variable) {
                // No operator for variables
                op = false;
            } else if (value instanceof Power && !(((Power) value).getToken() instanceof Number)) {
                // No operator if we have power of not a number
                op = false;
            } else if (value instanceof Root) {
                // No operators for roots
                op = false;
            }

            // Check for -1 and 1
            if (value instanceof Number && (((Number) value).getValue() == -1 || ((Number) value).getValue() == 1)) {
                // Remove the 1
                asString = asString.substring(0, asString.length() - 1);
            }

            // Add operator if required
            if (op) {
                string.append(" * ");
            }

            // Check for brackets
            if (brackets) {
                string.append("(");
                string.append(asString);
                string.append(")");
            } else {
                string.append(asString);
            }
        }

        return string.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        // Compute all values
        List<Token> values = new ArrayList<>();
        for (Token value : this.values) {
            values.add(value.compute(inputs, format));
        }
        Collections.sort(values, this);

        // Some required vars
        int index = 0;

        // Iterate values
        while (index < values.size()) {
            // Get value
            Token value = values.get(index);

            // Check if value is a product
            if (value instanceof Product) {
                // Add values to self
                values.addAll(((Product) value).getValues());

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

                        // Multiply them
                        Token product = value.apply(Operation.multiplication, otherValue, inputs, format);

                        // If it is simpler than a product
                        if (!(product instanceof Product)) {
                            // Update values
                            value = product;
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

                // Check for one (1x is x)
                if (value instanceof Number && ((Number) value).getValue() == 1) {
                    // Remove one
                    values.remove(index);
                    index--;
                }

                // Check for zero (0x is 0)
                else if (value instanceof Number && ((Number) value).getValue() == 0) {
                    // Return zero
                    return value;
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
            return new Number(1);
        }

        // Return the simplified product
        return new Product(values);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // If addition
        if (operation == Operation.addition) {
            // TODO: Check for common factor

            // Add token to sum
            return new Sum(this, right);
        }

        // If subtraction
        if (operation == Operation.subtraction) {
            // Add token to sum
            return new Sum(this, right.opposite()).compute(inputs, format);
        }

        // If product
        if (operation == Operation.multiplication) {
            // Add token to product
            List<Token> values = new ArrayList<>(getValues());
            values.add(right);
            return new Product(values);
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
            List<Token> values = new ArrayList<>();
            for (Token value : getValues()) {
                values.add(new Power(value, right));
            }
            return new Product(values);
        }

        // Root
        if (operation == Operation.root) {
            List<Token> values = new ArrayList<>();
            for (Token value : getValues()) {
                values.add(new Root(value, right));
            }
            return new Product(values);
        }

        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return operation.getPrecedence() >= Operation.multiplication.getPrecedence();
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        List<Token> values = new ArrayList<>(getValues());
        values.add(new Number(-1));
        return new Product(values);
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public Double asDouble() {
        return null;
    }

    public int compare(Token t0, Token t1) {
        return t0.getMultiplicationPriority() - t1.getMultiplicationPriority();
    }

}

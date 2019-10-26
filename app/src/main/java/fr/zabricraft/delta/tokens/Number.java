package fr.zabricraft.delta.tokens;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.extensions.IntExtension;
import fr.zabricraft.delta.utils.Operation;

public class Number implements Token {

    private int value;

    public Number(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return String.valueOf(value);
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Sum
        if (operation == Operation.addition) {
            // If value is 0
            if (value == 0) {
                // 0 + x is x
                return right;
            }

            // Right is a number
            if (right instanceof Number) {
                return new Number(value + ((Number) right).value);
            }

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
            // If value is 0
            if (value == 0) {
                // 0 - x is - x
                return right.opposite();
            }

            // Right is a number
            if (right instanceof Number) {
                return new Number(value - ((Number) right).value);
            }

            // Return the sum
            return new Sum(this, right.opposite());
        }

        // Product
        if (operation == Operation.multiplication) {
            // If value is 1
            if (value == 1) {
                // It's 1 time right, return right
                return right;
            }

            // If value is 0
            if (value == 0) {
                // 0 * x is 0
                return this;
            }

            // Right is a number
            if (right instanceof Number) {
                return new Number(value * ((Number) right).value);
            }

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
                // a/b * c = ac/b
                return new Fraction(new Product(this, ((Fraction) right).getNumerator()), ((Fraction) right).getDenominator()).compute(inputs, false);
            }

            // Right is a sum
            if (right instanceof Sum) {
                List<Token> values = new ArrayList<>();

                for (Token token : ((Sum) right).getValues()) {
                    values.add(new Product(token, this));
                }

                return new Sum(values).compute(inputs, false);
            }

            // Right is a vector
            if (right instanceof Vector) {
                return right.apply(operation, this, inputs, false);
            }

            // Return the product
            return new Product(this, right);
        }

        // Fraction
        if (operation == Operation.division) {
            // If value is 0
            if (value == 0 && !(right instanceof Number)) {
                // 0 / x is 0
                return this;
            }

            // Right is a number
            if (right instanceof Number) {
                // If right is 0
                if (((Number) right).getValue() == 0) {
                    // x/0 is calcul error
                    return new CalculError();
                }

                // Multiple so division is an integer
                if (value % ((Number) right).value == 0) {
                    return new Number(value / ((Number) right).value);
                }

                // Get the greatest common divisor
                int gcd = IntExtension.greatestCommonDivisor(value, ((Number) right).value);

                // If it's greater than one
                if (gcd > 1) {
                    int numerator = value / gcd;
                    int denominator = ((Number) right).value / gcd;

                    // Return the simplified fraction
                    return new Fraction(new Number(numerator), new Number(denominator));
                }
            }

            // Return the fraction
            return new Fraction(this, right);
        }

        // Modulo
        if (operation == Operation.modulo) {
            // Right is a number
            if (right instanceof Number) {
                // If right is 0
                if (((Number) right).getValue() == 0) {
                    // x/0 is calcul error
                    return new CalculError();
                }

                return new Number(value % ((Number) right).value);
            }

            // Return the modulo
            return new Modulo(this, right);
        }

        // Power
        if (operation == Operation.power) {
            // Right is a number
            if (right instanceof Number) {
                // 0^0 is calcul error
                if (value == 0 && ((Number) right).value == 0) {
                    return new CalculError();
                }

                // Apply power to number
                return new Number((int) Math.pow(value, ((Number) right).value));
            }

            // Return the power
            return new Power(this, right);
        }

        // Root
        if (operation == Operation.root) {
            // Apply root
            if (right instanceof Number) {
                if (value >= 0) {
                    // Positive
                    double value = Math.pow(this.value, 1 / ((Number) right).value);

                    if (Double.isInfinite(value) || Double.isNaN(value)) {
                        // Calcul error
                        return new CalculError();
                    } else if (value == Math.floor(value)) {
                        // Simplified root
                        return new Number((int) value);
                    }
                } else {
                    // Negative
                    double value = Math.pow(-this.value, 1 / ((Number) right).value);

                    if (Double.isInfinite(value) || Double.isNaN(value)) {
                        // Calcul error
                        return new CalculError();
                    } else if (value == Math.floor(value)) {
                        // Simplified root
                        return new Product(new Number((int) value), new Variable("i"));
                    } else {
                        // Root of negative as i * sqrt(-value)
                        return new Product(new Root(new Number(-this.value), right), new Variable("i"));
                    }
                }
            }

            // Return root
            new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 3;
    }

    public Token opposite() {
        return new Number(-value);
    }

    public Token inverse() {
        return new Fraction(new Number(1), this);
    }

    public Double asDouble() {
        return (double) value;
    }

}

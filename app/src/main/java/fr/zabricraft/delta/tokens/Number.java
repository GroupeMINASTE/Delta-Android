package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.extensions.LongExtension;
import fr.zabricraft.delta.utils.Operation;

public class Number extends Token {

    private long value;

    public Number(long value) {
        this.value = value;
    }

    public long getValue() {
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
                long gcd = LongExtension.greatestCommonDivisor(value, ((Number) right).value);

                // If it's greater than one
                if (gcd > 1) {
                    long numerator = value / gcd;
                    long denominator = ((Number) right).value / gcd;

                    // Return the simplified fraction
                    return new Fraction(new Number(numerator), new Number(denominator));
                }
            }
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
                if (((Number) right).value >= 0) {
                    return new Number((long) Math.pow(value, ((Number) right).value));
                } else {
                    return new Number((long) Math.pow(value, -((Number) right).value)).inverse();
                }
            }
        }

        // Root
        if (operation == Operation.root) {
            // Apply root
            if (right instanceof Number) {
                if (value >= 0) {
                    // Positive
                    double value = Math.pow(this.value, 1.0 / ((Number) right).value);

                    if (Double.isInfinite(value) || Double.isNaN(value)) {
                        // Calcul error
                        return new CalculError();
                    } else if (value == Math.floor(value)) {
                        // Simplified root
                        return new Number((long) value);
                    }
                } else {
                    // Negative
                    double value = Math.pow(-this.value, 1.0 / ((Number) right).value);

                    if (Double.isInfinite(value) || Double.isNaN(value)) {
                        // Calcul error
                        return new CalculError();
                    } else if (value == Math.floor(value)) {
                        // Simplified root
                        return new Product(new Number((long) value), new Variable("i"));
                    } else {
                        // Root of negative as i * sqrt(-value)
                        return new Product(new Root(new Number(-this.value), right), new Variable("i"));
                    }
                }
            }
        }

        // Delegate to default
        return defaultApply(operation, right, inputs, format);
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

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        return (double) value;
    }

}

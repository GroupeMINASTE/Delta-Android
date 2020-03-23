package fr.zabricraft.delta.tokens;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public abstract class Token implements Serializable {

    public abstract String toString();

    public abstract Token compute(Map<String, Token> inputs, boolean format);

    public abstract Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format);

    public abstract boolean needBrackets(Operation operation);

    public abstract int getMultiplicationPriority();

    public abstract Token opposite();

    public abstract Token inverse();

    public abstract Double asDouble();

    public Token defaultApply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        // Compute right
        right = right.compute(inputs, format);

        // Sum
        if (operation == Operation.addition) {
            // Right is a sum
            if (right instanceof Sum) {
                java.util.List<Token> values = new ArrayList<>(((Sum) right).getValues());
                values.add(this);
                return new Sum(values);
            }

            return new Sum(this, right);
        }

        // Difference
        if (operation == Operation.subtraction) {
            return new Sum(this, right.opposite()).compute(inputs, format);
        }

        // Product
        if (operation == Operation.multiplication) {
            // Right is a product
            if (right instanceof Product) {
                List<Token> values = new ArrayList<>(((Product) right).getValues());
                values.add(this);
                return new Product(values);
            }

            // Left and right are the same
            if (toString().equals(right.toString())) {
                return new Power(this, new Number(2)).compute(inputs, format);
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
            return new Root(this, right);
        }

        // Unknown, return a calcul error
        return new CalculError();
    }

}

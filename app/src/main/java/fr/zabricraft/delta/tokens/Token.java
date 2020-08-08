package fr.zabricraft.delta.tokens;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.zabricraft.delta.extensions.LongExtension;
import fr.zabricraft.delta.utils.Operation;

public abstract class Token implements Serializable {

    public abstract String toString();

    public abstract Token compute(Map<String, Token> inputs, boolean format);

    public abstract Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format);

    public abstract boolean needBrackets(Operation operation);

    public abstract int getMultiplicationPriority();

    public abstract Token opposite();

    public abstract Token inverse();

    public abstract boolean equals(Token right);

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

            // Left and right are the same
            if (equals(right)) {
                return new Product(this, new Number(2)).compute(inputs, format);
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
            if (equals(right)) {
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
            // Left and right are products
            Product leftProduct = this instanceof Product ? ((Product) this) : new Product(this);
            Product rightProduct = right instanceof Product ? ((Product) right) : new Product(right);

            // Check for a common factor
            ArrayList<Token> leftValues = new ArrayList<>(leftProduct.getValues());
            ArrayList<Token> rightValues = new ArrayList<>(rightProduct.getValues());
            int leftIndex = 0;
            while (leftIndex < leftValues.size()) {
                // Iterate right values
                int rightIndex = 0;
                while (rightIndex < rightValues.size()) {
                    // Check if left and right are the same
                    if (leftValues.get(leftIndex).equals(rightValues.get(rightIndex))) {
                        // We have a common factor
                        leftValues.remove(leftIndex);
                        leftValues.add(leftIndex, new Number(1));
                        rightValues.remove(rightIndex);
                        rightValues.add(rightIndex, new Number(1));
                    }

                    // Check if both are numbers with gcd != 1
                    if (leftValues.get(leftIndex) instanceof Number && rightValues.get(rightIndex) instanceof Number) {
                        Number leftNumber = ((Number) leftValues.get(leftIndex));
                        Number rightNumber = ((Number) rightValues.get(rightIndex));
                        long gcd = LongExtension.greatestCommonDivisor(leftNumber.getValue(), rightNumber.getValue());

                        if (gcd != 1) {
                            // We have a common factor
                            leftValues.remove(leftIndex);
                            leftValues.add(leftIndex, new Number(leftNumber.getValue() / gcd));
                            rightValues.remove(rightIndex);
                            rightValues.add(rightIndex, new Number(rightNumber.getValue() / gcd));
                        }
                    }

                    // Increment
                    rightIndex++;
                }

                // Increment
                leftIndex++;
            }

            // Return the fraction
            return new Fraction(new Product(leftValues).compute(inputs, format), new Product(rightValues).compute(inputs, format));
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

    public boolean defaultEquals(Token right) {
        // Compare value (if possible)
        Double leftDouble = asDouble();
        Double rightDouble = right.asDouble();
        if (leftDouble != null && rightDouble != null) {
            return leftDouble.equals(rightDouble);
        }

        // Compare string
        return toString().equals(right.toString());
    }

}

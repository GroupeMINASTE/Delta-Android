package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Equation extends Token {

    private Token left;
    private Token right;
    private Operation operation;

    public Equation(Token left, Token right, Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public Token getLeft() {
        return left;
    }

    public Token getRight() {
        return right;
    }

    public Operation getOperation() {
        return operation;
    }

    public boolean isTrue(Map<String, Token> inputs) {
        Token left = this.left.compute(inputs, ComputeMode.simplify);
        Token right = this.right.compute(inputs, ComputeMode.simplify);

        // Equals
        if (operation == Operation.equals) {
            return left.equals(right);
        }
        // Unequals
        else if (operation == Operation.unequals) {
            return !left.equals(right);
        }

        // Other operations with value
        Double leftDouble = left.asDouble();
        Double rightDouble = right.asDouble();
        if (leftDouble != null && rightDouble != null) {
            if (operation == Operation.greaterThan) {
                return leftDouble > rightDouble;
            } else if (operation == Operation.lessThan) {
                return leftDouble < rightDouble;
            } else if (operation == Operation.greaterThanOrEquals) {
                return leftDouble >= rightDouble;
            } else if (operation == Operation.lessThanOrEquals) {
                return leftDouble <= rightDouble;
            }
        }

        return false;
    }

    public String toString() {
        return left.toString() + " " + operation.rawValue + " " + right.toString();
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        Token left = this.left.compute(inputs, mode);
        Token right = this.right.compute(inputs, mode);

        return new Equation(left, right, operation);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        // Unknown, return a calcul error
        return new CalculError();
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        return new Equation(left.opposite(), right.opposite(), operation);
    }

    public Token inverse() {
        return new Equation(left.inverse(), right.inverse(), operation);
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        return null;
    }
}

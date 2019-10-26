package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class Equation implements Token {

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
        Double left = this.left.compute(inputs, false).asDouble();
        Double right = this.right.compute(inputs, false).asDouble();

        if (left != null && right != null) {
            if (operation == Operation.equals) {
                return left.equals(right);
            } else if (operation == Operation.unequals) {
                return !left.equals(right);
            } else if (operation == Operation.greaterThan) {
                return left > right;
            } else if (operation == Operation.lessThan) {
                return left < right;
            } else if (operation == Operation.greaterThanOrEquals) {
                return left >= right;
            } else if (operation == Operation.lessThanOrEquals) {
                return left <= right;
            }
        }

        return false;
    }

    public String toString() {
        return left.toString() + " " + operation.rawValue + " " + right.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        Token left = this.left.compute(inputs, format);
        Token right = this.right.compute(inputs, format);

        return new Equation(left, right, operation);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
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

    public Double asDouble() {
        return null;
    }
}

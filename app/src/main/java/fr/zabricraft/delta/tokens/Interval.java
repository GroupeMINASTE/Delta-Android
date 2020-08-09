package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.ComputeMode;
import fr.zabricraft.delta.utils.Operation;

public class Interval extends Token {

    private Token left;
    private Token right;
    private boolean left_closed;
    private boolean right_closed;

    public Interval(Token left, Token right, boolean left_closed, boolean right_closed) {
        this.left = left;
        this.right = right;
        this.left_closed = left_closed;
        this.right_closed = right_closed;
    }

    public Token getLeft() {
        return left;
    }

    public Token getRight() {
        return right;
    }

    public boolean isLeftClosed() {
        return left_closed;
    }

    public boolean isRightClosed() {
        return right_closed;
    }

    public String toString() {
        return (left_closed ? "[" : "]") + left.toString() + ", " + right.toString() + (right_closed ? "]" : "[");
    }

    public Token compute(Map<String, Token> inputs, ComputeMode mode) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, ComputeMode mode) {
        // Compute right
        //right = right.compute(inputs, format);

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
        // Unknown
        return this;
    }

    public Token inverse() {
        // Unknown
        return this;
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        return null;
    }

}

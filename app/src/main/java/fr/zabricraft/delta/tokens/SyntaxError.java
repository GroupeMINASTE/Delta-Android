package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class SyntaxError extends Token {

    public String toString() {
        return "Syntax error";
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        return this;
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return 1;
    }

    public Token opposite() {
        return this;
    }

    public Token inverse() {
        return this;
    }

    public Double asDouble() {
        return null;
    }

}

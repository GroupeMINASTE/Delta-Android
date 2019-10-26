package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class FormattedToken implements Token {

    private Token token;

    public FormattedToken(Token token) {
        this.token = token;
    }

    public String toString() {
        return token.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        return token.compute(inputs, true);
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        return token.apply(operation, right, inputs, true);
    }

    public boolean needBrackets(Operation operation) {
        return token.needBrackets(operation);
    }

    public int getMultiplicationPriority() {
        return token.getMultiplicationPriority();
    }

    public Token opposite() {
        return token.opposite();
    }

    public Token inverse() {
        return token.inverse();
    }

    public Double asDouble() {
        return token.asDouble();
    }

}

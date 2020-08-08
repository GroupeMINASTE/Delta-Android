package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public class FunctionDeclaration extends Token {

    private String variable;
    private Token token;

    public FunctionDeclaration(String variable, Token token) {
        this.variable = variable;
        this.token = token;
    }

    public String getVariable() {
        return variable;
    }

    public Token getToken() {
        return token;
    }

    public String toString() {
        return token.toString();
    }

    public Token compute(Map<String, Token> inputs, boolean format) {
        return this;
    }

    public Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format) {
        return new FunctionDeclaration(variable, token.apply(operation, right, inputs, format));
    }

    public boolean needBrackets(Operation operation) {
        return false;
    }

    public int getMultiplicationPriority() {
        return token.getMultiplicationPriority();
    }

    public Token opposite() {
        return new FunctionDeclaration(variable, token.opposite());
    }

    public Token inverse() {
        return new FunctionDeclaration(variable, token.opposite());
    }

    public boolean equals(Token right) {
        return defaultEquals(right);
    }

    public Double asDouble() {
        return token.asDouble();
    }

}

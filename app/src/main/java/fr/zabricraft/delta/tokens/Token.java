package fr.zabricraft.delta.tokens;

import java.util.Map;

import fr.zabricraft.delta.utils.Operation;

public interface Token {

    String toString();

    Token compute(Map<String, Token> inputs, boolean format);

    Token apply(Operation operation, Token right, Map<String, Token> inputs, boolean format);

    boolean needBrackets(Operation operation);

    int getMultiplicationPriority();

    Token opposite();

    Token inverse();

    Double asDouble();

}
